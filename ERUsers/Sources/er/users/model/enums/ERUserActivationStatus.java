package er.users.model.enums;

public enum ERUserActivationStatus {
	/**
	 * Status of a new user before it is activated
	 */
	PRE_ACTIVATION, 
	
	/**
	 * An active user
	 */
	ACTIVATED, 
	
	/**
	 * The user needs to take some action to re-activate his account.
	 */
	DISABLED, 
	
	/**
	 * An administrator needs to take some action to re-activate the user.
	 */
	DEACTIVATED;
}
