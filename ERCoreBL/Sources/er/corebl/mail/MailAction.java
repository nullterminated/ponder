package er.corebl.mail;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResourceManager;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSTimestamp;

import er.corebl.components.DefaultUnsubscribePage;
import er.corebl.model.ERCMailAddress;
import er.corebl.model.ERCMailMessage;
import er.extensions.appserver.ERXApplication;
import er.extensions.appserver.ERXDirectAction;
import er.extensions.eof.ERXEC;
import er.extensions.net.ERXEmailValidator;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;

public class MailAction extends ERXDirectAction {
	public static final String UUID_KEY = "uuid";
	public static final String ADDRESS_KEY = "email";
	
	private static UnsubscribeDelegate unsubscribeDelegate = new DefaultUnsubscribeDelegate();

	public MailAction(WORequest r) {
		super(r);
	}

	public WOActionResults readAction() {
		String uuid = request().stringFormValueForKey(UUID_KEY);
		
		if(uuid != null) {
			EOEditingContext ec = ERXEC.newEditingContext();
			ec.lock();
			try {
				ERCMailMessage message = ERCMailMessage.clazz.objectMatchingKeyAndValue(ec, ERCMailMessage.UUID_KEY, uuid);
				if(message != null) {
					message.setDateRead(new NSTimestamp());
					if(message.mailRecipients().count() == 1) {
						ERCMailAddress address = message.mailRecipients().lastObject().mailAddress();
						address.setVerificationState(ERCMailAddressVerification.VERIFIED);
					}
					ec.saveChanges();
				}
			} catch (Exception e) {
				/* Ignore exceptions */
				log.warn("Error setting read date on mail message with uuid: " + uuid, e);
			} finally {
				ec.unlock();
			}
		}
		
		WOResourceManager rm = WOApplication.application().resourceManager();
		byte[] gif = rm.bytesForResourceNamed("blank.gif", "ERCoreBL", null);
		WOResponse response = new WOResponse();
		response.setStatus(200);
		response.setContent(gif);
        response.setHeader("image/gif", "Content-Type");
        response.setHeader(String.valueOf(gif.length), "Content-Length");
		return response;
	}
	
	public WOActionResults unsubscribeAction() {
		String messageId = request().stringFormValueForKey(UUID_KEY);
		String email = request().stringFormValueForKey(ADDRESS_KEY);
		
		EOEditingContext ec = ERXEC.newEditingContext();
		ec.lock();
		try {
			if(messageId != null) {
				ERCMailMessage message = ERCMailMessage.clazz.objectMatchingKeyAndValue(ec, ERCMailMessage.MESSAGE_ID_KEY, messageId);
				if(message != null) {
					return unsubscribeDelegate().unsubscribeMessage(message);
				} else {
					return unsubscribeDelegate().formPage();
				}
			}
			if(email != null) {
				boolean isValid = unsubscribeDelegate().validEmail(email);
				if(isValid) {
					ERCMailAddress address = ERCMailAddress.clazz.addressForEmailString(ec, email);
					return unsubscribeDelegate().unsubscribeAddress(address);
				} else {
					ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
					ERXValidationException ex = factory.createCustomException(null, "InvalidUnsubscribeAddress");
					ex.setContext(new NSDictionary<String, String>(email, "emailAddress"));
					return unsubscribeDelegate().error(ex);
				}
			}
			return unsubscribeDelegate().formPage();
		} catch (Exception e) {
			//Report error to user
			return unsubscribeDelegate().error(e);
		} finally {
			ec.unlock();
		}
	}
	
	public interface UnsubscribeDelegate {
		public WOActionResults unsubscribeMessage(ERCMailMessage message);
		public WOActionResults unsubscribeAddress(ERCMailAddress emailAddress);
		public WOActionResults formPage();
		public WOActionResults error(Exception e);
		public boolean validEmail(String email);
	}
	
	public static class DefaultUnsubscribeDelegate implements UnsubscribeDelegate {
		private final ERXEmailValidator validator = new ERXEmailValidator(false, false);

		@Override
		public WOActionResults unsubscribeMessage(ERCMailMessage message) {
			if(message.mailRecipients().count() == 1) {
				ERCMailAddress address = message.mailRecipients().lastObject().mailAddress();
				address.setVerificationState(ERCMailAddressVerification.VERIFIED);
				return unsubscribeAddress(address);
			} else {
				return formPage();
			}
		}
		
		@Override
		public WOActionResults unsubscribeAddress(ERCMailAddress emailAddress) {
			EOEditingContext ec = emailAddress.editingContext();
			emailAddress.setStopReason(ERCMailStopReason.UNSUBSCRIBED);
			ec.saveChanges();
			WOResponse response = new WOResponse();
			response.setStatus(200);
			response.setContent(emailAddress + " has been unsubscribed.");
			return response;
		}

		@Override
		public WOActionResults formPage() {
			WOComponent component = ERXApplication.instantiatePage(DefaultUnsubscribePage.class.getName());
			return component;
		}

		@Override
		public WOActionResults error(Exception e) {
			DefaultUnsubscribePage page = (DefaultUnsubscribePage) formPage();
			page.setErrorMessage(e.getMessage());
			return page;
		}

		@Override
		public boolean validEmail(String email) {
			return validator.isValidEmailString(email);
		}

	}
	
	public static UnsubscribeDelegate unsubscribeDelegate() {
		return unsubscribeDelegate;
	}
	
	public static void setUnsubscribeDelegate(UnsubscribeDelegate delegate) {
		if(delegate == null) { throw new IllegalArgumentException("Delegate cannot be null"); }
		unsubscribeDelegate = delegate;
	}
}
