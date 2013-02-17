package er.users.delegates;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2W;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.D2WPage;
import com.webobjects.directtoweb.ERD2WUtilities;
import com.webobjects.directtoweb.EditPageInterface;
import com.webobjects.directtoweb.NextPageDelegate;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;

import er.corebl.ERCoreBL;
import er.corebl.mail.ERCMailState;
import er.corebl.mail.ERCMailer;
import er.corebl.model.ERCMailAddress;
import er.corebl.model.ERCMailMessage;
import er.directtoweb.ERD2WFactory;
import er.directtoweb.delegates.ERDBranchDelegate;
import er.directtoweb.delegates.ERDBranchInterface;
import er.directtoweb.interfaces.ERDMessagePageInterface;
import er.directtoweb.pages.ERD2WQueryPage;
import er.extensions.eof.ERXEC;
import er.extensions.foundation.ERXConfigurationManager;
import er.extensions.localization.ERXLocalizer;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;
import er.users.components.ERUserHtmlEmail;
import er.users.model.ERUser;

public class ForgotPasswordController extends ERDBranchDelegate {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	public WOComponent resetPassword(WOComponent sender) {
		ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
		ERD2WQueryPage page = ERD2WUtilities.enclosingComponentOfClass(sender, ERD2WQueryPage.class);
		EOEditingContext ec = ERXEC.newEditingContext();
		String username = (String) page.displayGroup().queryMatch().objectForKey(ERUser.USERNAME_KEY);
		if(username == null) {
			ERXValidationException ex = factory.createCustomException(null, ERUser.USERNAME_KEY, username, "NullUsernameException");
			page.validationFailedWithException(ex, username, ERUser.USERNAME_KEY);
			return null;
		}

		EOQualifier q = ERUser.USERNAME.eq(username);
		
		//Duplicate users by username should be impossible due to the unique index
		ERUser user = ERUser.clazz.objectsMatchingQualifier(ec, q).lastObject();
		if(user == null) {
			ERXValidationException ex = factory.createCustomException(null, ERUser.USERNAME_KEY, username, "UsernameNotFound");
			page.validationFailedWithException(ex, username, ERUser.USERNAME_KEY);
			return null;
		}
		
		if(user.resetRequestDate() == null) {
			//Set request date and token
			user.setResetRequestDate(new DateTime());
			user.setResetToken(UUID.randomUUID().toString());
			ec.saveChanges();
		}
		
		DateTime bypassDate = user.resetRequestDate().plus(outOfBandWaitingPeriod());
		if(bypassDate.compareTo(new DateTime()) > 0) {
			// TODO Send email
			// Send an activation email for the ERUser.
			ERXLocalizer loc = ERXLocalizer.currentLocalizer();
			D2WContext c = d2wContext(sender);
			WOContext ctx = (WOContext) c.valueForKeyPath("session.context.clone");
			String templateName = (String) c.valueForKey("templateNameForPasswordResetEmail");
			WOComponent component = WOApplication.application().pageWithName(templateName, ctx);
			ERUserHtmlEmail emailComponent = (ERUserHtmlEmail) component;
			emailComponent.setUser(user);
			
			String subject = loc.localizedStringForKey("ERDefaultResetPasswordHtmlEmail.subject");
			String plainMessageString = loc.localizedStringForKey("ERDefaultResetPasswordHtmlEmail.plainMessage");
			String plainMessage = plainMessageString + "\n\n" + user.resetPasswordHrefInContext(component.context());
			String htmlMessage = component.generateResponse().contentString();

			//TODO fix from address generation. Include uuid in address comment for bounce detection
			String hostName = ERXConfigurationManager.defaultManager().hostName();
			String fromString = WOApplication.application().name() + "-" + hostName + "@" + ERCoreBL.sharedInstance().problemEmailDomain();
			ERCMailAddress from = ERCMailAddress.clazz.addressForEmailString(ec, fromString);
			
			ERCMailAddress toAddress = user.mailAddress();
			NSArray<ERCMailAddress> to = new NSArray<ERCMailAddress>(toAddress);
			
			ERCMailMessage mailMessage = ERCMailMessage.clazz.composeMailMessage(ec, ERCMailState.READY_TO_BE_SENT, from, null, to, null, null, subject, htmlMessage, plainMessage, null, null);
			
			try {
				ec.saveChanges();
				ERCMailer.INSTANCE.sendMailMessage(mailMessage);
			} catch(Exception e) {
				NSMutableDictionary<String, Object> extraInfo = new NSMutableDictionary<String, Object>();
				extraInfo.setObjectForKey("Failed to send reset password email!", "status");
				extraInfo.setObjectForKey(user, "user");
				extraInfo.setObjectForKey(user.resetToken(), "token");
				ERCoreBL.sharedInstance().reportException(e, extraInfo);
			}

			// Return a success message page to the user.
			D2W d2w = ERD2WFactory.factory();
			WOComponent newPage = d2w.pageForConfigurationNamed("MessageERUser", sender.session());
			ERDMessagePageInterface messagePage = (ERDMessagePageInterface) newPage;
			String message = loc.localizedTemplateStringForKeyWithObject("ResetPasswordSentMessage", c);
			messagePage.setMessage(message);
			messagePage.setNextPage(_nextPageFromDelegate(page));
			return newPage;

		} else {
			// Go directly to challenge questions
			D2W d2w = ERD2WFactory.factory();
			WOComponent result = d2w.pageForConfigurationNamed("ResetERUserPassword", page.session());
			EditPageInterface epi = (EditPageInterface) result;
			epi.setObject(user);
			epi.setNextPage(_nextPageFromDelegate(page));
			return result;
		}
	}
	
	/**
	 * Provided so that subclasses can provide a different waiting period.
	 * 
	 * @return the waiting period
	 */
	protected Duration outOfBandWaitingPeriod() {
		return Duration.standardDays(ERUser.clazz.outOfBandDays());
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
