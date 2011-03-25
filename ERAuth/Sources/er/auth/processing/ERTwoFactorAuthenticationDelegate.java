package er.auth.processing;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOResponse;

import er.auth.ERAuthDirectAction;

public class ERTwoFactorAuthenticationDelegate {
	public WOComponent successPage() {
		WOComponent page = ERAuthDirectAction.lastPreviousPageFromRequest();
		return page;
	}
	
	public WOComponent failurePage() {
		WOComponent page = ERAuthDirectAction.previousPageFromRequest();
		return page;
	}
	
	public WOActionResults credentialRecoveryPage() {
		WOResponse response = new WOResponse();
		response.setStatus(501);
		response.setContent("Not Implemented");
		return response;
	}
}
