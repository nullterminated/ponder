package er.users.delegates;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2W;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.D2WPage;
import com.webobjects.directtoweb.ERD2WUtilities;
import com.webobjects.directtoweb.InspectPageInterface;
import com.webobjects.directtoweb.NextPageDelegate;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSValidation;

import er.corebl.ERCoreBL;
import er.corebl.mail.ERCMailState;
import er.corebl.mail.ERCMailer;
import er.corebl.model.ERCMailAddress;
import er.corebl.model.ERCMailMessage;
import er.directtoweb.ERD2WFactory;
import er.directtoweb.delegates.ERDBranchDelegate;
import er.directtoweb.delegates.ERDBranchInterface;
import er.directtoweb.interfaces.ERDMessagePageInterface;
import er.directtoweb.pages.ERD2WPage;
import er.extensions.foundation.ERXConfigurationManager;
import er.extensions.localization.ERXLocalizer;
import er.users.components.ERUserHtmlEmail;
import er.users.model.ERUser;

public class CreateUserController extends ERDBranchDelegate {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Saves the created ERUser. If successful, an email is sent to activate the
	 * ERUser and a success message page is returned. If unsuccessful, returns
	 * the current page.
	 * 
	 * @param sender
	 *            the sender
	 * @return the next page
	 */
	public WOComponent createUser(WOComponent sender) {
		WOComponent nextPage = sender.context().page();
		ERUser user = (ERUser) object(sender);
		D2WContext c = d2wContext(sender);
		ERD2WPage page = (ERD2WPage) ERD2WUtilities.enclosingComponentOfClass(sender, InspectPageInterface.class);
		ERXLocalizer loc = ERXLocalizer.currentLocalizer();

		if (user.editingContext() == null) {
			String message = loc.localizedTemplateStringForKeyWithObject("ERD2WInspect.alreadyAborted", c);
			page.setErrorMessage(message);
			page.clearValidationFailed();
			return nextPage;
		}

		if (!page.errorMessages().isEmpty()) {
			page.setErrorMessage(null);
			return nextPage;
		}

		boolean saveSuccessful = false;
		EOEditingContext ec = user.editingContext();
		try {
			ec.saveChanges();
			saveSuccessful = true;
		} catch (NSValidation.ValidationException e) {
			page.setErrorMessage(loc.localizedTemplateStringForKeyWithObject("CouldNotSave", e));
			page.validationFailedWithException(e, e.object(), "saveChangesExceptionKey");
		}

		if (saveSuccessful) {
			// Send an activation email for the ERUser.
			WOContext ctx = (WOContext) c.valueForKeyPath("session.context.clone");
			String templateName = (String) c.valueForKey("templateNameForUserActivationEmail");
			WOComponent component = WOApplication.application().pageWithName(templateName, ctx);
			ERUserHtmlEmail emailComponent = (ERUserHtmlEmail) component;
			emailComponent.setUser(user);
			
			String subject = loc.localizedStringForKey("ERDefaultUserActivationHtmlEmail.subject");
			String plainMessageString = loc.localizedStringForKey("ERDefaultUserActivationHtmlEmail.plainMessage");
			String plainMessage = plainMessageString + "\n\n" + user.activateUserHrefInContext(component.context());
			String htmlMessage = component.generateResponse().contentString();

			//TODO fix from address generation. Include uuid in address comment for bounce detection
			String hostName = ERXConfigurationManager.defaultManager().hostName();
			String fromString = WOApplication.application().name() + "-" + hostName + "@" + ERCoreBL.sharedInstance().problemEmailDomain();
			ERCMailAddress from = ERCMailAddress.clazz.addressForEmailString(ec, fromString);
			
			ERCMailAddress toAddress = ERCMailAddress.clazz.addressForEmailString(ec, user.emailAddress());
			NSArray<ERCMailAddress> to = new NSArray<ERCMailAddress>(toAddress);
			
			ERCMailMessage mailMessage = ERCMailMessage.clazz.composeMailMessage(ec, ERCMailState.READY_TO_BE_SENT, from, null, to, null, null, subject, htmlMessage, plainMessage, null, null);
			
			try {
				ec.saveChanges();
				ERCMailer.INSTANCE.sendMailMessage(mailMessage);
			} catch(Exception e) {
				NSMutableDictionary<String, Object> extraInfo = new NSMutableDictionary<String, Object>();
				extraInfo.setObjectForKey("Failed to send activation email!", "status");
				extraInfo.setObjectForKey(user, "user");
				extraInfo.setObjectForKey(user.activateUserToken(), "token");
				ERCoreBL.sharedInstance().reportException(e, extraInfo);
			}

			// Return a success message page to the user.
			D2W d2w = ERD2WFactory.factory();
			WOComponent newPage = d2w.pageForConfigurationNamed("MessageERUser", sender.session());
			ERDMessagePageInterface messagePage = (ERDMessagePageInterface) newPage;
			String message = loc.localizedTemplateStringForKeyWithObject("CreateUserSuccessMessage", c);
			messagePage.setMessage(message);
			messagePage.setNextPage(_nextPageFromDelegate(page));
			nextPage = newPage;

		}

		return nextPage;
	}

	/**
	 * Cancels the creation of an ERUser
	 * 
	 * @param sender
	 *            the sender
	 * @return the next page
	 */
	public WOComponent cancel(WOComponent sender) {
		EOEnterpriseObject eo = object(sender);
		ERD2WPage page = (ERD2WPage) ERD2WUtilities.enclosingComponentOfClass(sender, InspectPageInterface.class);
		EOEditingContext ec = eo != null ? eo.editingContext() : null;
		if (ec != null) {
			ec.revert();
		}
		return _nextPageFromDelegate(page);
	}

	/**
	 * Returns the next page from the D2WPage
	 * 
	 * @param page
	 *            the D2WPage
	 * @return the next page
	 */
	protected WOComponent _nextPageFromDelegate(D2WPage page) {
		WOComponent nextPage = null;
		NextPageDelegate delegate = page.nextPageDelegate();
		if (delegate != null) {
			if (!((delegate instanceof ERDBranchDelegate) && (((ERDBranchInterface) page).branchName() == null))) {
				/*
				 * we assume here, because nextPage() in ERDBranchDelegate is
				 * final, we can't do something reasonable when none of the
				 * branch buttons was selected. This allows us to throw a branch
				 * delegate at any page, even when no branch was taken
				 */
				nextPage = delegate.nextPage(page);
			}
		}
		if (nextPage == null) {
			nextPage = page.nextPage();
		}
		return nextPage;
	}
}
