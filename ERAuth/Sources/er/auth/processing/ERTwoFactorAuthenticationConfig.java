package er.auth.processing;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;


public class ERTwoFactorAuthenticationConfig implements ERAuthenticationConfig, ERCredentialRecoveryConfig {
	protected final String userEntityName;
	protected final String usernameKeyPath;
	protected final String passwordKeyPath;
	protected final ERTwoFactorAuthenticationDelegate delegate;
	
	public ERTwoFactorAuthenticationConfig(String userEntityName, String usernameKeyPath, String passwordKeyPath) {
		this(userEntityName, usernameKeyPath, passwordKeyPath, null);
	}
	
	public ERTwoFactorAuthenticationConfig(String userEntityName, String usernameKeyPath, String passwordKeyPath, ERTwoFactorAuthenticationDelegate delegate) {
		this.userEntityName = userEntityName;
		this.usernameKeyPath = usernameKeyPath;
		this.passwordKeyPath = passwordKeyPath;
		this.delegate = delegate == null?new ERTwoFactorAuthenticationDelegate():delegate;
	}
	
	@Override
	public String userEntityName() {
		return userEntityName;
	}
	
	@Override
	public WOComponent loginFailurePage() {
		return delegate.failurePage();
	}
	
	@Override
	public WOActionResults credentialRecoveryPage() {
		return delegate.credentialRecoveryPage();
	}

	@Override
	public WOComponent loginSuccessPage() {
		return delegate.successPage();
	}

	public String usernameKeyPath() {
		return usernameKeyPath;
	}
	
	public String passwordKeyPath() {
		return passwordKeyPath;
	}
	
	public boolean verifyPassword(String enteredPassword, Object storedPassword) {
		return storedPassword.equals(enteredPassword);
	}
}
