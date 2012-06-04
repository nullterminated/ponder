package er.users;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSDictionary;

import er.directtoweb.ERD2WDirectAction;
import er.directtoweb.interfaces.ERDErrorPageInterface;
import er.directtoweb.interfaces.ERDMessagePageInterface;
import er.extensions.appserver.ERXBrowser;
import er.extensions.appserver.ERXBrowserFactory;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXEOControlUtilities;
import er.extensions.localization.ERXLocalizer;
import er.users.model.ERActivateUserToken;
import er.users.model.enums.ERUserActivationStatus;

public class ERUserAction extends ERD2WDirectAction {
	
	public static final String TOKEN_KEY = "token";
	
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
		EOEnterpriseObject eo = null;
		String token = request().stringFormValueForKey(TOKEN_KEY);
		if(token != null) {
			EOEditingContext ec = ERXEC.newEditingContext();
			eo = ERXEOControlUtilities.objectWithPrimaryKeyValue(ec, ERActivateUserToken.ENTITY_NAME, token, null);
		}
		
		ERXLocalizer loc = ERXLocalizer.localizerForRequest(request());
		
		// Return an error page if the token is not found
		if (eo == null) {
			WOActionResults result = dynamicPageForActionNamed("Error");
			ERDErrorPageInterface epi = (ERDErrorPageInterface) result;
			NSDictionary<String, Object> messageContext = NSDictionary.emptyDictionary();
			if(token != null) {
				messageContext = new NSDictionary<String, Object>(token, "token");
			}
			String message = loc.localizedTemplateStringForKeyWithObject("ERUserAction.tokenNotFound", messageContext);
			epi.setMessage(message);
			return result;
		}
		
		// Activate the user related to the token
		ERActivateUserToken aut = (ERActivateUserToken) eo;
		aut.user().setActivationStatus(ERUserActivationStatus.ACTIVATED);
		try {
			eo.editingContext().saveChanges();
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
		WOActionResults result = dynamicPageForActionNamed("MessageERUser");
		ERDMessagePageInterface mpi = (ERDMessagePageInterface) result;
		String message = loc.localizedStringForKey("ERUserAction.activationSuccess");
		mpi.setMessage(message);
		return result;
	}
}
