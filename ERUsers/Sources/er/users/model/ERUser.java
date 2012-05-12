package er.users.model;

import java.util.Random;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

import er.corebl.model.ERCPreference;
import er.corebl.preferences.ERCoreUserInterface;
import er.extensions.crypting.ERXCrypto;
import er.extensions.eof.ERXFetchSpecification;
import er.extensions.eof.ERXKey;
import er.extensions.net.ERXEmailValidator;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;

/**
 * The user class.
 */
public class ERUser extends er.users.model.eogen._ERUser implements ERCoreUserInterface {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;
	
	private static final ERXEmailValidator emailValidator = new ERXEmailValidator(false, false);

	private static final Logger log = Logger.getLogger(ERUser.class);

	public static final ERXKey<ERCPreference> PREFERENCES = new ERXKey<ERCPreference>("preferences");
	public static final ERXKey<String> CLEAR_PASSWORD = new ERXKey<String>("clearPassword");

	public static final String PREFERENCES_KEY = PREFERENCES.key();
	public static final String CLEAR_PASSWORD_KEY = CLEAR_PASSWORD.key();

	private transient String clearPassword;
	private ERCredential newCredential;

	public static final ERUserClazz<ERUser> clazz = new ERUserClazz<ERUser>();

	public static class ERUserClazz<T extends ERUser> extends er.users.model.eogen._ERUser._ERUserClazz<T> {
		/* more clazz methods here */
	}

	/**
	 * Initializes the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
		DateTime dt = new DateTime();
		setDateCreated(dt);
		setLastSeen(dt);
	}

	/**
	 * @return the clear text password
	 */
	public String clearPassword() {
		willRead();
		return clearPassword;
	}

	/**
	 * Sets the clear text password
	 * @param clearPassword the clear text password
	 */
	public void setClearPassword(String clearPassword) {
		willChange();
		log.debug("clearPassword updated");
		this.clearPassword = clearPassword;
	}

	@Override
	@SuppressWarnings("unchecked")
	public NSArray<ERCPreference> preferences() {
		return (NSArray<ERCPreference>) storedValueForKey(PREFERENCES_KEY);
	}

	@Override
	public void setPreferences(NSArray<ERCPreference> preferences) {
		takeStoredValueForKey(preferences, PREFERENCES_KEY);
	}

	@Override
	public void newPreference(ERCPreference preference) {
		addObjectToBothSidesOfRelationshipWithKey(preference, PREFERENCES_KEY);
	}

	/**
	 * Returns a string value for the clear password after passing it through a
	 * one way hash function.
	 * 
	 * @param clearPassword
	 *            the clear text password
	 * @return the hashed password
	 */
	public String hashedPassword(String clearPassword) {
		String sha = ERXCrypto.sha512Encode(clearPassword + salt());
		return sha;
	}

	/**
	 * @param clearPassword
	 *            the clear text password to compare
	 * @return true if the hashing clearPassword matches the stored hash
	 */
	public boolean hashMatches(String clearPassword) {
		return password().equals(hashedPassword(clearPassword));
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

	/**
	 * Creates a new {@link er.users.model.ERCredential ERCredential} for the
	 * user if a clearPassword is detected, then it sets the new password to the
	 * hashed clearPassword.
	 */
	public void willUpdate() {
		super.willUpdate();
		if (clearPassword != null) {
			setSalt(generateSalt());
			String hash = hashedPassword(clearPassword);
			if (newCredential == null) {
				newCredential = createCredentialsRelationship();
			}
			newCredential.setPassword(hash);
			newCredential.setSalt(salt());
			setPassword(hash);
		}
	}

	/**
	 * Creates a new {@link er.users.model.ERCredential ERCredential} for the
	 * user if a clearPassword is detected, then it sets the new password to the
	 * hashed clearPassword.
	 */
	public void willInsert() {
		super.willInsert();
		if (clearPassword != null) {
			setSalt(generateSalt());
			String hash = hashedPassword(clearPassword);
			if (newCredential == null) {
				newCredential = createCredentialsRelationship();
			}
			newCredential.setPassword(hash);
			newCredential.setSalt(salt());
			setPassword(hash);
		}
	}

	/**
	 * Clears the clear text password and new credential.
	 */
	public void willRevert() {
		super.willRevert();
		clearPassword = null;
		newCredential = null;
	}

	/**
	 * Clears the clear text password and new credential.
	 */
	public void didUpdate() {
		super.didUpdate();
		clearPassword = null;
		newCredential = null;
	}

	/**
	 * Ensures a password is long enough and not recently used.
	 * 
	 * @param clearPassword
	 *            the clear text password
	 * @return the validated clear text password
	 */
	public String validateClearPassword(final String clearPassword) {
		ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
		ERXValidationException ex = null;

		// Check length
		if (clearPassword.length() < 8) {
			ex = factory.createCustomException(this, CLEAR_PASSWORD_KEY, clearPassword, "PasswordLengthException");
			throw ex;
		}
		
		EOQualifier userQualifier = ERCredential.USER.eq(this);

		// Check previous passwords
		DateTime oneMonth = new DateTime().minusMonths(1);
		EOQualifier q = ERCredential.DATE_CREATED.greaterThan(oneMonth).and(userQualifier);
		NSArray<EOSortOrdering> sort = ERCredential.DATE_CREATED.descs();

		// Fetch all credentials used in the last month
		ERXFetchSpecification<ERCredential> fs = new ERXFetchSpecification<ERCredential>(ERCredential.ENTITY_NAME, q, sort);
		NSArray<ERCredential> pastMonth = fs.fetchObjects(editingContext());

		// Fetch the last three credentials used
		ERXFetchSpecification<ERCredential> fsLimit = new ERXFetchSpecification<ERCredential>(ERCredential.ENTITY_NAME, userQualifier, sort);
		fsLimit.setFetchLimit(3);
		NSArray<ERCredential> lastThree = fsLimit.fetchObjects(editingContext());

		// Examine whichever is greatest
		NSArray<ERCredential> pastCredentials = pastMonth.count() > 3 ? pastMonth : lastThree;
		for (ERCredential past : pastCredentials) {
			if (past.hashMatches(clearPassword)) {
				ex = factory.createCustomException(this, CLEAR_PASSWORD_KEY, clearPassword, "PreviousPasswordException");
				throw ex;
			}
		}

		return clearPassword;
	}
	
	public String validateEmailAddress(String emailAddress) {
		if(!emailValidator.isValidEmailAddress(emailAddress, 100, true)) {
			ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
			ERXValidationException ex = factory.createException(this, EMAIL_ADDRESS_KEY, emailAddress, ERXValidationException.InvalidValueException);
			throw ex;
		}
		return emailAddress;
	}
}
