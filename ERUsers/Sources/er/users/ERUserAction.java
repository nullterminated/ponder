package er.users;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.directtoweb.D2W;
import com.webobjects.directtoweb.EditPageInterface;
import com.webobjects.eocontrol.EOEditingContext;

import er.directtoweb.ERD2WDirectAction;
import er.directtoweb.ERD2WFactory;
import er.directtoweb.interfaces.ERDErrorPageInterface;
import er.directtoweb.interfaces.ERDMessagePageInterface;
import er.extensions.ERXExtensions;
import er.extensions.appserver.ERXBrowser;
import er.extensions.appserver.ERXBrowserFactory;
import er.extensions.eof.ERXEC;
import er.extensions.localization.ERXLocalizer;
import er.users.model.ERUser;

public class ERUserAction extends ERD2WDirectAction {
	
	public static final String TOKEN_KEY = "token";
	public static final String USERNAME_KEY = "username";
	
	public ERUserAction(WORequest r) {
		super(r);
	}

	public WOActionResults activateUserAction() {
		// check for robot user agents
		ERXBrowser browser = ERXBrowserFactory.factory().browserMatchingRequest(request());
		if(browser.isRobot()) {
			WOResponse response = new WOResponse();
			response.setStatus(403);
			response.setContent("Search engine robots are forbidden.");
			return response;
		}
		
		// find the matching token
		ERUser user = null;
		String token = request().stringFormValueForKey(TOKEN_KEY);
		String username = request().stringFormValueForKey(USERNAME_KEY);
		if(username != null && token != null) {
			EOEditingContext ec = ERXEC.newEditingContext();
			user = ERUser.clazz.objectMatchingKeyAndValue(ec, ERUser.USERNAME_KEY, username);
		}
		
		ERXLocalizer loc = ERXLocalizer.localizerForRequest(request());
		
		// Return an error page if the user is not found or the token does not match
		if (user == null || ERXExtensions.safeDifferent(token, user.activateUserToken())) {
			WOActionResults result = dynamicPageForActionNamed("Error");
			ERDErrorPageInterface epi = (ERDErrorPageInterface) result;
			String message = loc.localizedStringForKey("ERUserAction.activateTokenNotFound");
			epi.setMessage(message);
			return result;
		}
		
		// Activate the user related to the token
		user.activateUser();
		try {
			user.editingContext().saveChanges();
		} catch (Exception e) {
			// Return an error page if save changes fails.
			WOActionResults result = dynamicPageForActionNamed("Error");
			ERDErrorPageInterface epi = (ERDErrorPageInterface) result;
			String message = loc.localizedStringForKey("ERUserAction.couldNotSave");
			epi.setMessage(message);
			epi.setException(e);
			return result;
		}
		
		// return success page
		D2W d2w = ERD2WFactory.factory();
		WOComponent result = d2w.pageForConfigurationNamed("MessageERUser", session());
		ERDMessagePageInterface mpi = (ERDMessagePageInterface) result;
		String message = loc.localizedStringForKey("ERUserAction.activationSuccess");
		mpi.setMessage(message);
		return result;
	}
	
	public WOActionResults resetPasswordAction() {
		// check for robot user agents
		ERXBrowser browser = ERXBrowserFactory.factory().browserMatchingRequest(request());
		if(browser.isRobot()) {
			WOResponse response = new WOResponse();
			response.setStatus(403);
			response.setContent("Search engine robots are forbidden.");
			return response;
		}
		
		// find the matching token
		ERUser user = null;
		String token = request().stringFormValueForKey(TOKEN_KEY);
		String username = request().stringFormValueForKey(USERNAME_KEY);
		if(username != null && token != null) {
			EOEditingContext ec = ERXEC.newEditingContext();
			user = ERUser.clazz.objectMatchingKeyAndValue(ec, ERUser.USERNAME_KEY, username);
		}
		
		ERXLocalizer loc = ERXLocalizer.localizerForRequest(request());
		
		// Return an error page if the user is not found or the token does not match
		if (user == null || ERXExtensions.safeDifferent(token, user.resetToken())) {
			WOActionResults result = dynamicPageForActionNamed("Error");
			ERDErrorPageInterface epi = (ERDErrorPageInterface) result;
			String message = loc.localizedStringForKey("ERUserAction.resetTokenNotFound");
			epi.setMessage(message);
			return result;
		}
		
		// return reset page
		D2W d2w = ERD2WFactory.factory();
		WOComponent result = d2w.pageForConfigurationNamed("ResetERUserPassword", session());
		EditPageInterface epi = (EditPageInterface) result;
		epi.setObject(user);
		//TODO use defaultAction or default D2W page instead
		epi.setNextPage(pageWithName("Main"));
		return result;
	}
}
