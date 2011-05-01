package er.authexample.model;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

import er.corebusinesslogic.ERCPreference;
import er.corebusinesslogic.ERCoreUserInterface;
import er.extensions.crypting.ERXCrypto;
import er.extensions.eof.ERXFetchSpecification;
import er.extensions.eof.ERXKey;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;

public class User extends er.authexample.model.base._User implements ERCoreUserInterface {
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(User.class);

	public static final ERXKey<ERCPreference> PREFERENCES = new ERXKey<ERCPreference>("preferences");
	public static final ERXKey<String> CLEAR_PASSWORD = new ERXKey<String>("clearPassword");

	public static final String PREFERENCES_KEY = PREFERENCES.key();
	public static final String CLEAR_PASSWORD_KEY = CLEAR_PASSWORD.key();

	private transient String clearPassword;
	private transient Credential newCredential;

	public static final UserClazz<User> clazz = new UserClazz<User>();

	public static class UserClazz<T extends User> extends er.authexample.model.base._User._UserClazz<T> {
		/* more clazz methods here */
	}

	/**
	 * Initializes the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
		setDateCreated(new DateTime());
	}

	public String clearPassword() {
		willRead();
		return clearPassword;
	}

	public void setClearPassword(String clearPassword) {
		willChange();
		this.clearPassword = clearPassword;
	}

	@SuppressWarnings("rawtypes")
	public NSArray preferences() {
		return (NSArray) storedValueForKey(PREFERENCES_KEY);
	}

	@SuppressWarnings("rawtypes")
	public void setPreferences(NSArray preferences) {
		takeStoredValueForKey(preferences, PREFERENCES_KEY);
	}

	public void newPreference(EOEnterpriseObject preference) {
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
	public static String hashedPassword(String clearPassword) {
		String sha = ERXCrypto.sha512Encode(clearPassword);
		return sha;
	}

	/**
	 * Creates a new {@link er.authexample.model.Credential Credential} for the
	 * user if a clearPassword is detected, then it sets the new password to the
	 * hashed clearPassword.
	 */
	public void willUpdate() {
		super.willUpdate();
		if (clearPassword != null) {
			String hash = hashedPassword(clearPassword);
			if (newCredential == null) {
				newCredential = createCredentialsRelationship();
				newCredential.setPassword(hash);
				setPassword(hash);
			} else if (!hash.equals(newCredential.password())) {
				newCredential.setPassword(hash);
				setPassword(hash);
			}
		}
	}
	
	/**
	 * Creates a new {@link er.authexample.model.Credential Credential} for the
	 * user if a clearPassword is detected, then it sets the new password to the
	 * hashed clearPassword.
	 */
	public void willInsert() {
		super.willInsert();
		if (clearPassword != null) {
			String hash = hashedPassword(clearPassword);
			if (newCredential == null) {
				newCredential = createCredentialsRelationship();
				newCredential.setPassword(hash);
				setPassword(hash);
			} else if (!hash.equals(newCredential.password())) {
				newCredential.setPassword(hash);
				setPassword(hash);
			}
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
	 * Ensures a password is strong enough and not recently used.
	 * 
	 * @param clearPassword
	 *            the clear text password
	 * @return the validated clear text password
	 */
	public String validateClearPassword(String clearPassword) {
		ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
		ERXValidationException ex = null;
		
		// Check length
		if (clearPassword.length() < 8) {
			ex = factory.createCustomException(this, CLEAR_PASSWORD_KEY, clearPassword, "PasswordLengthException");
			throw ex;
		}

		// Check previous passwords
		DateTime oneMonth = new DateTime().minusMonths(1);
		EOQualifier q = Credential.DATE_CREATED.greaterThan(oneMonth);
		NSArray<EOSortOrdering> sort = Credential.DATE_CREATED.descs();

		// Fetch all credentials used in the last month
		ERXFetchSpecification<Credential> fs = new ERXFetchSpecification<Credential>(Credential.ENTITY_NAME, q, sort);
		NSArray<Credential> pastMonth = fs.fetchObjects(editingContext());

		// Fetch the last three credentials used
		ERXFetchSpecification<Credential> fsLimit = new ERXFetchSpecification<Credential>(Credential.ENTITY_NAME, null,
				sort);
		fsLimit.setFetchLimit(3);
		NSArray<Credential> lastThree = fsLimit.fetchObjects(editingContext());

		// Examine whichever is greatest
		NSArray<Credential> pastCredentials = pastMonth.count() > 3 ? pastMonth : lastThree;
		String hash = hashedPassword(clearPassword);
		for (Credential past : pastCredentials) {
			if (hash.equals(past.password())) {
				ex = factory.createCustomException(this, CLEAR_PASSWORD_KEY, clearPassword, "PreviousPasswordException");
				throw ex;
			}
		}

		return clearPassword;
	}

}
