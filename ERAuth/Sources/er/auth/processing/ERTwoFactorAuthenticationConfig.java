package er.auth.processing;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;


public class ERTwoFactorAuthenticationConfig implements ERAuthenticationConfig, ERCredentialRecoveryConfig {
	protected final String userEntityName;
	protected final String usernameKeyPath;
	protected final String storedPasswordKeyPath;
	protected final ERTwoFactorAuthenticationDelegate delegate;
	
	public ERTwoFactorAuthenticationConfig(String userEntityName, String usernameKeyPath, String storedPasswordKeyPath) {
		this(userEntityName, usernameKeyPath, storedPasswordKeyPath, null);
	}
	
	public ERTwoFactorAuthenticationConfig(String userEntityName, String usernameKeyPath, String storedPasswordKeyPath, ERTwoFactorAuthenticationDelegate delegate) {
		this.userEntityName = userEntityName;
		this.usernameKeyPath = usernameKeyPath;
		this.storedPasswordKeyPath = storedPasswordKeyPath;
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
	
	public String storedPasswordKeyPath() {
		return storedPasswordKeyPath;
	}
	
	/**
	 * Verifies that the password entered matches the password stored in the database.
	 * @param enteredPassword the entered password
	 * @param storedPassword the stored password
	 * @return true if the passwords match
	 */
	public boolean verifyPassword(String enteredPassword, Object storedPassword) {
		return storedPassword.equals(enteredPassword);
	}
}
