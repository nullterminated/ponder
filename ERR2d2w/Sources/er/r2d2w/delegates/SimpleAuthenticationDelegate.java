package er.r2d2w.delegates;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.D2WPage;
import com.webobjects.directtoweb.NextPageDelegate;
import com.webobjects.eoaccess.EODatabaseDataSource;
import com.webobjects.eocontrol.EODataSource;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;

import er.directtoweb.pages.ERD2WPage;
import er.extensions.appserver.ERXSession;
import er.extensions.eof.ERXQ;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.localization.ERXLocalizer;

public class SimpleAuthenticationDelegate implements NextPageDelegate {
    public static final Logger log = Logger.getLogger(SimpleAuthenticationDelegate.class);

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
		
		String passwordKey = (String)c.valueForKeyPath("userCredentialAttribute.name");
		String passwordValue = (String)dg.queryMatch().valueForKey(passwordKey);
		
		if(ERXStringUtilities.stringIsNullOrEmpty(usernameValue) ||
				ERXStringUtilities.stringIsNullOrEmpty(passwordValue)) {
			missingCredentials(c, usernameKey, passwordKey, sender);
			return nextPage;
		}
		
		EOQualifier q = ERXQ.and(ERXQ.equals(usernameKey, usernameValue) ,ERXQ.equals(passwordKey, passwordValue));
		EOFetchSpecification f = new EOFetchSpecification(page.entityName(), q, null);
		EODatabaseDataSource dds = new EODatabaseDataSource(page.editingContext(),page.entityName());
		dds.setFetchSpecification(f);
		NSArray objects = dds.fetchObjects();
		
		int count = objects.count();
		
		switch(count) {
		case 0:
			invalidCredentials(c, usernameKey, passwordKey, sender);
			break;
		case 1:
			EOEnterpriseObject user = (EOEnterpriseObject)objects.objectAtIndex(0);
			ERXSession session = (ERXSession)sender.session();
			session.setObjectForKey(user, "user");
			didAuthenticate(sender);
			nextPage = page.nextPage();
			break;
		default:
			log.debug(
					"More than one user found for login. " + 
					"Please ensure usernames are unique. queryMatch: " + 
					dg.queryMatch() + "; defaultStringMatchFormat: " + 
					dg.defaultStringMatchFormat() + "; defaultStringMatchOperator" + 
					dg.defaultStringMatchOperator());
			throw new IllegalStateException("More than one user found for login");
		}

		return nextPage;
	}
	
	public void didAuthenticate(WOComponent sender) {
		D2WContext c = ((D2WPage)sender).d2wContext();
		ERXSession session = (ERXSession)sender.session();
		EOEnterpriseObject user = (EOEnterpriseObject)session.objectForKey("user");
		
		// Sets the session language to the user's prefered language if one exists
		Object lang = c.valueForKeyPath("userLanguageAttribute.name");
		Object userLang =(lang != null)?user.valueForKey(lang.toString()):null;
		String sessionLang = (userLang != null)?userLang.toString():ERXLocalizer.currentLocalizer().language();
		session.setLanguage(sessionLang);
	}
	
	private static final String _DISPLAY_USERNAME_KEY = "displayNameForUserID";
	private static final String _DISPLAY_PASSWORD_KEY = "displayNameForUserCredential";
	
	private NSDictionary<String, String> displayNames(D2WContext c, String usernameKey, String passwordKey) {
		NSMutableDictionary<String, String> displayNames = new NSMutableDictionary<String, String>();
		c.setPropertyKey(usernameKey);
		displayNames.put(_DISPLAY_USERNAME_KEY, c.displayNameForProperty());
		c.setPropertyKey(passwordKey);
		displayNames.put(_DISPLAY_PASSWORD_KEY, c.displayNameForProperty());
		return displayNames;
	}

	private void invalidCredentials(D2WContext c, String usernameKey, String passwordKey, WOComponent sender) {
		NSDictionary<String, String> displayNames = displayNames(c, usernameKey, passwordKey);
		String errorMessage = ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("R2D2W.invalidLoginCredentials", displayNames);
		sender.takeValueForKey(errorMessage, "errorMessage");
	}

	private void missingCredentials(D2WContext c, String usernameKey, String passwordKey, WOComponent sender) {
		NSDictionary<String, String> displayNames = displayNames(c, usernameKey, passwordKey);
		String errorMessage = ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("R2D2W.missingLoginCredentials", displayNames);
		sender.takeValueForKey(errorMessage, "errorMessage");
	}
}
