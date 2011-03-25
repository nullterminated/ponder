package er.auth.processing;

import com.webobjects.appserver.WOComponent;


public interface ERAuthenticationConfig {
	/**
	 * @return The entity name of the user entity.
	 */
	public String userEntityName();
	
	/**
	 * @return the page displayed when authentication fails
	 */
	public WOComponent loginFailurePage();
	
	/**
	 * @return the page displayed when authentication succeeds
	 */
	public WOComponent loginSuccessPage();
}
