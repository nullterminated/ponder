package er.r2d2w.delegates;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.directtoweb.D2W;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.NextPageDelegate;
import com.webobjects.eoaccess.EODatabaseDataSource;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EODataSource;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;

import er.directtoweb.pages.ERD2WPage;
import er.extensions.appserver.ERXSession;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXQ;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.localization.ERXLocalizer;
import er.javamail.ERMailDeliveryHTML;

public class SimpleResetDelegate implements NextPageDelegate {
	
	public WOComponent nextPage(WOComponent sender) {
		WOComponent nextPage = sender.context().page();
		ERD2WPage page = (ERD2WPage)sender;
		
		EODataSource ds = (EODataSource)sender.valueForKey("queryDataSource");		
		if(ds == null) {
			// User canceled
			return page.nextPage();
		}
		
		WODisplayGroup dg = (WODisplayGroup)sender.valueForKey("displayGroup");
		D2WContext c = page.d2wContext();

		String usernameKey = (String)c.valueForKeyPath("userIDAttribute.name");
		String usernameValue = (String)dg.queryMatch().valueForKey(usernameKey);
		
		// No value entered
		if(ERXStringUtilities.stringIsNullOrEmpty(usernameValue)) {
			noUserID(c, usernameKey, sender);
			return nextPage;
		}
		
		EOQualifier q = ERXQ.equals(usernameKey, usernameValue);
		EOFetchSpecification f = new EOFetchSpecification(page.entityName(), q, null);
		EODatabaseDataSource dds = new EODatabaseDataSource(page.editingContext(),page.entityName());
		dds.setFetchSpecification(f);
		NSArray objects = dds.fetchObjects();
		
		int count = objects.count();

		switch(count) {
		case 0:
			userIDNotFound(c, usernameKey, sender);
			break;
		case 1:
			EOEnterpriseObject user = (EOEnterpriseObject)objects.objectAtIndex(0);
			String path = (String)c.valueForKeyPath("userContactAttribute.name");
			String mailToAddress = (String)user.valueForKey(path);
			
			//For some reason, the EC needs to be changed to prevent deadlocking
			//on the nextPage of the success message page. So give it a new one.
			user = EOUtilities.localInstanceOfObject(ERXEC.newEditingContext(), user);

			if(mailToAddress != null) {
				String mailFromAddress = (String)c.valueForKey("mailFromAddress");
				if(mailFromAddress == null) {
					mailFromAddress = "noreply@" + WOApplication.application().host();
				}
				ERXSession session = (ERXSession)sender.session();
				ERMailDeliveryHTML mail = new ERMailDeliveryHTML();
				mail.newMail();
				mail.setCharset((String)c.valueForKey("mailCharacterEncoding"));
				ERXLocalizer loc = ERXLocalizer.currentLocalizer();
				String reminder = loc.localizedTemplateStringForKeyWithObject("R2D2W.passwordReminderMessage", c);
				String message = loc.localizedTemplateStringForKeyWithObject("R2D2W.messageSent", c);
				String subject = loc.localizedTemplateStringForKeyWithObject("R2D2W.passwordReminderSubject", c);
				
				
				WOComponent htmlPage = D2W.factory().pageForConfigurationNamed("MailUserReset", session);
				htmlPage.takeValueForKey(user, "object");
				htmlPage.takeValueForKey(reminder, "message");
				mail.setComponent(htmlPage);
				
				WOComponent plainPage = D2W.factory().pageForConfigurationNamed("MailPlainUserReset", session);
				plainPage.takeValueForKey(user, "object");
				plainPage.takeValueForKey(reminder, "message");
				mail.setAlternativeComponent(plainPage);

				try {
					mail.setFromAddress(mailFromAddress);
					mail.setSubject(subject);
					mail.setToAddress(mailToAddress);
					mail.sendMail();
				} catch (Exception e) {
					e.printStackTrace();
				}

				WOComponent messagePage = D2W.factory().pageForConfigurationNamed("MessageSent", session);
				messagePage.takeValueForKey(message, "message");
				WOComponent returnPage = (WOComponent)page.valueForKey("nextPage");
				if(returnPage == null) { returnPage = D2W.factory().defaultPage(session); }
				messagePage.takeValueForKey(returnPage, "nextPage");
				nextPage = messagePage;
			} else {
				noAddressFound(c, usernameKey, sender);
			}
			break;
		default:
			throw new IllegalStateException(
					"More than one user found for password reminder. " + 
					"Please ensure usernames are unique. queryMatch: " + 
					dg.queryMatch() + "; defaultStringMatchFormat: " + 
					dg.defaultStringMatchFormat() + "; defaultStringMatchOperator" + 
					dg.defaultStringMatchOperator());
		}

		return nextPage;
	}
	
	private static final String _DISPLAY_USERNAME_KEY = "displayNameForUserID";
	
	private NSDictionary<String, String> displayNames(D2WContext c, String usernameKey) {
		NSMutableDictionary<String, String> displayNames = new NSMutableDictionary<String, String>();
		c.setPropertyKey(usernameKey);
		displayNames.put(_DISPLAY_USERNAME_KEY, c.displayNameForProperty());
		return displayNames;
	}
	
	private void noUserID(D2WContext c, String usernameKey, WOComponent sender) {
		NSDictionary<String, String> displayNames = displayNames(c, usernameKey);
		String errorMessage = ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("R2D2W.missingUserIDForReminder", displayNames);
		sender.takeValueForKey(errorMessage, "errorMessage");
	}
	
	private void userIDNotFound(D2WContext c, String usernameKey, WOComponent sender) {
		NSDictionary<String, String> displayNames = displayNames(c, usernameKey);
		String errorMessage = ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("R2D2W.noUserFound", displayNames);
		sender.takeValueForKey(errorMessage, "errorMessage");
	}
	
	private void noAddressFound(D2WContext c, String usernameKey, WOComponent sender) {
		NSDictionary<String, String> displayNames = displayNames(c, usernameKey);
		String errorMessage = ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("R2D2W.noUserAddressFound", displayNames);
		sender.takeValueForKey(errorMessage, "errorMessage");
	}
	
}
