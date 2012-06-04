package er.users.delegates;

import com.webobjects.appserver.WOComponent;

import er.auth.ERAuthDirectAction;
import er.auth.processing.ERTwoFactorAuthenticationDelegate;

public class AuthenticationDelegate implements ERTwoFactorAuthenticationDelegate {
	public WOComponent successPage() {
		// TODO check to see if credentials are too old.
		WOComponent page = ERAuthDirectAction.lastPreviousPageFromRequest();
		return page;
	}

	public WOComponent failurePage() {
		WOComponent page = ERAuthDirectAction.previousPageFromRequest();
		return page;
	}
}
