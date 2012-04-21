package er.auth.processing;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;

public interface ERTwoFactorAuthenticationDelegate {
	public WOComponent successPage();
	
	public WOComponent failurePage();
	
	public WOActionResults credentialRecoveryPage();
}
