package er.authexample;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOResponse;

import er.auth.ERAuthDirectAction;
import er.auth.processing.ERTwoFactorAuthenticationDelegate;

public class AuthenticationDelegate extends ERTwoFactorAuthenticationDelegate {
	public WOComponent successPage() {
		//TODO check to see if credentials are too old.
		WOComponent page = ERAuthDirectAction.lastPreviousPageFromRequest();
		return page;
	}
	
	public WOComponent failurePage() {
		//TODO check for maximum number of failures for username/ip
		WOComponent page = ERAuthDirectAction.previousPageFromRequest();
		return page;
	}
	
	public WOActionResults credentialRecoveryPage() {
		//TODO implement credential recovery page
		WOResponse response = new WOResponse();
		response.setStatus(501);
		response.setContent("Not Implemented");
		return response;
	}
}
