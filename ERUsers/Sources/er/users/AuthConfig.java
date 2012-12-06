package er.users;

import com.webobjects.eocontrol.EOEnterpriseObject;

import er.auth.processing.ERTwoFactorAuthenticationConfig;
import er.auth.processing.ERTwoFactorAuthenticationDelegate;
import er.users.model.ERUser;
import er.users.model.enums.ERUserActivationStatus;

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
	
	/**
	 * Returns true if the user is activated
	 */
	@Override
	public boolean allowLogin(EOEnterpriseObject eo) {
		ERUser user = (ERUser)eo;
		return ERUserActivationStatus.ACTIVATED.equals(user.activationStatus());
	}
}
