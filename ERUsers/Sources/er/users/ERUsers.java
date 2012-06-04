package er.users;

import java.util.Random;

import org.apache.log4j.Logger;

import er.auth.ERAuth;
import er.auth.model.ERTwoFactorAuthenticationRequest;
import er.auth.processing.ERTwoFactorAuthenticationConfig;
import er.auth.processing.ERTwoFactorAuthenticationDelegate;
import er.corebl.ERCoreBL;
import er.extensions.ERXExtensions;
import er.extensions.ERXFrameworkPrincipal;
import er.extensions.crypting.ERXCrypto;
import er.javamail.ERJavaMail;
import er.users.delegates.AuthenticationDelegate;
import er.users.model.ERUser;

public class ERUsers extends ERXFrameworkPrincipal {
	public final static Class<?>[] REQUIRES = new Class[] { ERXExtensions.class, ERAuth.class, ERCoreBL.class, ERJavaMail.class };

	public static final Logger log = Logger.getLogger(ERUsers.class);

	protected static volatile ERUsers sharedInstance;

    // Registers the class as the framework principal
    static {
    	log.debug("Static Initializer for ERUsers");
    	setUpFrameworkPrincipalClass (ERUsers.class);
    }

    public static ERUsers sharedInstance() {
        if (sharedInstance == null) {
        	synchronized (ERUsers.class) {
        		if(sharedInstance == null) {
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

		//Set up the auth config and delegate
		ERTwoFactorAuthenticationDelegate delegate = new AuthenticationDelegate();
		ERTwoFactorAuthenticationConfig config = new AuthConfig(ERUser.ENTITY_NAME, ERUser.USERNAME_KEY, ERUser.CLEAR_PASSWORD_KEY, ERUser.PASSWORD_KEY, delegate);
		ERTwoFactorAuthenticationRequest.clazz.setAuthenticationConfig(config);
	}

	/**
	 * Generates a new random 128 byte base64 encoded salt value
	 * 
	 * @return the salt
	 */
	public String generateSalt() {
		Random random = new Random();
		byte[] bytes = new byte[128];
		random.nextBytes(bytes);
		String salt = ERXCrypto.base64Encode(bytes);
		return salt;
	}

}
