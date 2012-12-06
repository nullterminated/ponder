package er.auth.processing;

import com.webobjects.appserver.WOComponent;
import com.webobjects.eocontrol.EOEnterpriseObject;


public abstract class ERTwoFactorAuthenticationConfig implements ERAuthenticationConfig {
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
		this.delegate = delegate;
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
	 * @param user the user eo
	 * @return true if the passwords match
	 */
	public abstract boolean verifyPassword(EOEnterpriseObject user, String enteredPassword);
	
	/**
	 * Determines if the user is allowed to log in
	 * @param eo the user
	 * @return true if the user is permitted to log in
	 */
	public abstract boolean allowLogin(EOEnterpriseObject eo);
}
