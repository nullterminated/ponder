package er.authexample;

import er.auth.processing.ERTwoFactorAuthenticationConfig;
import er.auth.processing.ERTwoFactorAuthenticationDelegate;
import er.authexample.model.User;

public class AuthConfig extends ERTwoFactorAuthenticationConfig {

	public AuthConfig(String userEntityName, String usernameKeyPath, String passwordKeyPath, String storedPasswordKeyPath, ERTwoFactorAuthenticationDelegate delegate) {
		super(userEntityName, usernameKeyPath, storedPasswordKeyPath, delegate);
	}

	/**
	 * Overriden to verify password stored as a hash
	 */
	@Override
	public boolean verifyPassword(String enteredPassword, Object storedPassword) {
		String hash = User.hashedPassword(enteredPassword);
		return storedPassword.equals(hash);
	}
}
