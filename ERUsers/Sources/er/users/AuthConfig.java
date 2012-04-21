package er.users;

import com.webobjects.eocontrol.EOEnterpriseObject;

import er.auth.processing.ERTwoFactorAuthenticationConfig;
import er.auth.processing.ERTwoFactorAuthenticationDelegate;
import er.users.model.ERUser;

public class AuthConfig extends ERTwoFactorAuthenticationConfig {

	public AuthConfig(String userEntityName, String usernameKeyPath, String passwordKeyPath, String storedPasswordKeyPath, ERTwoFactorAuthenticationDelegate delegate) {
		super(userEntityName, usernameKeyPath, storedPasswordKeyPath, delegate);
	}

	/**
	 * Verifies password entered matches the stored hash
	 */
	@Override
	public boolean verifyPassword(EOEnterpriseObject eo, String enteredPassword) {
		ERUser user = (ERUser)eo;
		return user.hashMatches(enteredPassword);
	}
}
