package er.auth.processing;

import com.webobjects.appserver.WOActionResults;

/**
 * Interface implemented by authentication request classes that allow
 * credential recovery.
 */
public interface ERCredentialRecoveryConfig {
	/**
	 * @return The page where the user can recover credentials.
	 */
	public WOActionResults credentialRecoveryPage();
}
