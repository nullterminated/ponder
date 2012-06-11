package er.users;

import org.apache.log4j.Logger;

import er.auth.ERAuth;
import er.auth.model.ERTwoFactorAuthenticationRequest;
import er.auth.processing.ERTwoFactorAuthenticationConfig;
import er.auth.processing.ERTwoFactorAuthenticationDelegate;
import er.corebl.ERCoreBL;
import er.extensions.ERXExtensions;
import er.extensions.ERXFrameworkPrincipal;
import er.extensions.crypting.BCrypt;
import er.extensions.foundation.ERXProperties;
import er.javamail.ERJavaMail;
import er.users.delegates.AuthenticationDelegate;
import er.users.model.ERUser;

public class ERUsers extends ERXFrameworkPrincipal {
	public final static Class<?>[] REQUIRES = new Class[] { ERXExtensions.class, ERAuth.class, ERCoreBL.class,
			ERJavaMail.class };

	public static final Logger log = Logger.getLogger(ERUsers.class);

	protected static volatile ERUsers sharedInstance;

	// Registers the class as the framework principal
	static {
		log.debug("Static Initializer for ERUsers");
		setUpFrameworkPrincipalClass(ERUsers.class);
	}

	public static ERUsers sharedInstance() {
		if (sharedInstance == null) {
			synchronized (ERUsers.class) {
				if (sharedInstance == null) {
					sharedInstance = sharedInstance(ERUsers.class);
				}
			}
		}
		return sharedInstance;
	}

	@Override
	public void finishInitialization() {
		// Set up the user preferences relationship
		ERCoreBL.sharedInstance().addPreferenceRelationshipToActorEntity(ERUser.ENTITY_NAME);

		// Set up the auth config and delegate
		ERTwoFactorAuthenticationDelegate delegate = new AuthenticationDelegate();
		ERTwoFactorAuthenticationConfig config = new AuthConfig(ERUser.ENTITY_NAME, ERUser.USERNAME_KEY,
				ERUser.CLEAR_PASSWORD_KEY, ERUser.PASSWORD_KEY, delegate);
		ERTwoFactorAuthenticationRequest.clazz.setAuthenticationConfig(config);
	}

	/**
	 * The log2 number of rounds of hashing to apply. Larger values require more
	 * work. Default value is 10. Can be set using a property.
	 * 
	 * @property er.users.ERUsers.logRounds
	 * @return the log rounds value
	 */
	public int logRounds() {
		return ERXProperties.intForKeyWithDefault("er.users.ERUsers.logRounds", 10);
	}

	/**
	 * Generates a new BCrypt salt value using {@link ERUsers#logRounds()}
	 * 
	 * @return the salt
	 */
	public String generateSalt() {
		return BCrypt.gensalt(logRounds());
	}

	/**
	 * Returns a string value for the plain text after passing it through a one
	 * way hash function.
	 * 
	 * @param plaintext
	 *            the clear text input
	 * @return the hashed output
	 */
	public String hashedPlaintext(String plaintext) {
		String salt = ERUsers.sharedInstance().generateSalt();
		return BCrypt.hashpw(plaintext, salt);
	}

	/**
	 * @param plaintext
	 *            the plain text input to compare
	 * @return true if the hashed plain text matches the stored hash
	 */
	public boolean hashMatches(String plaintext, String hash) {
		return BCrypt.checkpw(plaintext, hash);
	}

}
