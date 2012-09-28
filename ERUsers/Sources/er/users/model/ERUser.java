package er.users.model;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSValidation;

import er.corebl.model.ERCPreference;
import er.corebl.preferences.ERCoreUserInterface;
import er.extensions.eof.ERXFetchSpecification;
import er.extensions.foundation.ERXArrayUtilities;
import er.extensions.foundation.ERXProperties;
import er.extensions.net.ERXEmailValidator;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;
import er.users.ERUserAction;
import er.users.ERUsers;
import er.users.model.enums.ERUserActivationStatus;

/**
 * The user class.
 */
public class ERUser extends er.users.model.eogen._ERUser implements ERCoreUserInterface {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	private static final ERXEmailValidator emailValidator = new ERXEmailValidator(false, false);
	private static final String[] QUERY_KEYS = new String[]{ERUserAction.TOKEN_KEY, ERUserAction.USERNAME_KEY};

	private static final Logger log = Logger.getLogger(ERUser.class);

	public static final String CLEAR_PASSWORD_KEY = "clearPassword";
	
	public static final String SUBTYPE_VALUE = "ERUser";

	protected ERCredential newCredential;

	public static final ERUserClazz<ERUser> clazz = new ERUserClazz<ERUser>();

	public static class ERUserClazz<T extends ERUser> extends er.users.model.eogen._ERUser._ERUserClazz<T> {
		/* more clazz methods here */
		public static final String REQUIRED_CHALLENGE_RESPONSES_PROP = "er.users.model.ERUser.clazz.requiredChallengeResponses";
		
		public static final String MIN_PASSWORD_LENGTH_PROP = "er.users.model.ERUser.clazz.minimumPasswordLength";
		
		public static final String PASSWORD_REUSE_COUNT_PROP = "er.users.model.ERUser.clazz.passwordReuseCount";
		
		public static final String PASSWORD_REUSE_DAYS_PROP = "er.users.model.ERUser.clazz.passwordReuseDays";

		public static final String OUT_OF_BAND_DAYS_PROP = "er.users.model.ERUser.clazz.outOfBandDays";
		
		/**
		 * @return The number of challenge responses required for password reset
		 */
		public int requiredChallengeResponses() {
			return ERXProperties.intForKeyWithDefault(REQUIRED_CHALLENGE_RESPONSES_PROP, 3);
		}

		/**
		 * @return The minimum length required for valid passwords
		 */
		public int minimumPasswordLength() {
			return ERXProperties.intForKeyWithDefault(MIN_PASSWORD_LENGTH_PROP, 8);
		}
		
		/**
		 * @return The minimum number of different passwords used before a password may be reused
		 */
		public int passwordReuseCount() {
			return ERXProperties.intForKeyWithDefault(PASSWORD_REUSE_COUNT_PROP, 3);
		}
		
		/**
		 * @return The minimum number of days that must elapse before a password may be reused
		 */
		public int passwordReuseDays() {
			return ERXProperties.intForKeyWithDefault(PASSWORD_REUSE_DAYS_PROP, 30);
		}
		
		/**
		 * @return The minimum number of days a user must wait to bypass the out of band password reset
		 */
		public int outOfBandDays() {
			return ERXProperties.intForKeyWithDefault(OUT_OF_BAND_DAYS_PROP, 7);
		}
	}

	/**
	 * Initializes the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	@Override
	public void init(EOEditingContext ec) {
		super.init(ec);
		setSubtype(SUBTYPE_VALUE);
		DateTime dt = new DateTime();
		setDateCreated(dt);
		setActivationStatus(ERUserActivationStatus.PRE_ACTIVATION);
		setActivateUserToken(UUID.randomUUID().toString());
		for (int i = 0, count = clazz.requiredChallengeResponses(); i < count; ++i) {
			ERChallengeResponse cr = ERChallengeResponse.clazz.createAndInsertObject(ec);
			addObjectToBothSidesOfRelationshipWithKey(cr, CHALLENGE_RESPONSES_KEY);
		}
	}

	/**
	 * Sets the {@link #password()} from the clear password. Copies the value
	 * into a new {@link ERCredential} to allow the prevention of password
	 * reuse.
	 * 
	 * @param clearPassword
	 *            the clear text password
	 */
	public void setClearPassword(String clearPassword) {
		willChange();
		log.debug("Updating password");
		if (newCredential == null) {
			newCredential = ERCredential.clazz.createAndInsertObject(editingContext());
			addObjectToBothSidesOfRelationshipWithKey(newCredential, CREDENTIALS_KEY);
		}
		String hashedPassword = ERUsers.sharedInstance().hashedPlaintext(clearPassword);
		newCredential.setPassword(hashedPassword);
		setPassword(hashedPassword);
	}

	/**
	 * Overridden to return null for clearPassword
	 */
	@Override
	public Object handleQueryWithUnboundKey(String key) {
		if("clearPassword".equals(key)) {
			return null;
		}
		return super.handleQueryWithUnboundKey(key);
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
	 * @param clearPassword
	 *            the clear text password to compare
	 * @return true if the hashing clearPassword matches the stored hash
	 */
	public boolean hashMatches(String clearPassword) {
		return ERUsers.sharedInstance().hashMatches(clearPassword, password());
	}
	
	/**
	 * This method sets answers to null on related challenge responses for any
	 * response that has a duplicated challenge question.
	 */
	private void clearDuplicateChallengeResponses() {
		NSDictionary<ERChallengeQuestion, NSArray<ERChallengeResponse>> grouped = 
				ERXArrayUtilities.arrayGroupedByKeyPath(challengeResponses(), ERChallengeResponse.CHALLENGE_QUESTION);
		// Using Object key since null grouping key is a string...
		for(Object key : grouped.allKeys()) {
			NSArray<ERChallengeResponse> responses = grouped.objectForKey(key);
			if(responses.count() > 1) {
				// Reset answers to null if the same question is chosen twice.
				responses.takeValueForKey(null, ERChallengeResponse.ANSWER_KEY);
			}
		}
	}

	/**
	 * Generates the url necessary to activate the related user.
	 * 
	 * @param context
	 *            the context
	 * @return the href string
	 */
	public String activateUserHrefInContext(WOContext context) {
		if(activateUserToken() != null) {
			Object[] queryValues = new Object[]{activateUserToken(), username()};
			NSDictionary<String, Object> queryDict = new NSDictionary<String, Object>(queryValues, QUERY_KEYS);
			boolean completeUrls = context.doesGenerateCompleteURLs();
			if (!completeUrls) {
				context.generateCompleteURLs();
			}
			try {
				return context.directActionURLForActionNamed("ERUserAction/activateUser", queryDict);
			} finally {
				if (!completeUrls) {
					context.generateRelativeURLs();
				}
			}
		}
		throw new IllegalStateException("No user activation token");
	}
	
	/**
	 * Generates the url necessary to reset the user's password
	 * 
	 * @param context
	 *            the context
	 * @return the href string
	 */
	public String resetPasswordHrefInContext(WOContext context) {
		if(resetToken() != null) {
			Object[] queryValues = new Object[]{resetToken(), username()};
			NSDictionary<String, Object> queryDict = new NSDictionary<String, Object>(queryValues, QUERY_KEYS);
			boolean completeUrls = context.doesGenerateCompleteURLs();
			if (!completeUrls) {
				context.generateCompleteURLs();
			}
			try {
				return context.directActionURLForActionNamed("ERUserAction/resetPassword", queryDict);
			} finally {
				if (!completeUrls) {
					context.generateRelativeURLs();
				}
			}
		}
		throw new IllegalStateException("No reset password token");
	}
	
	/**
	 * Activates the user by setting the user's activation status to
	 * ACTIVATED and setting the activation token to null;
	 */
	public void activateUser() {
		setActivateUserToken(null);
		setActivationStatus(ERUserActivationStatus.ACTIVATED);
	}

	/**
	 * Overridden to ensure challenge questions are not duplicated.
	 */
	@Override
	public void willInsert() {
		super.willInsert();
		
		//Ensure challenge questions are not duplicated
		clearDuplicateChallengeResponses();
	}
	
	/**
	 * Overridden to ensure challenge questions are not duplicated.
	 */
	@Override
	public void willUpdate() {
		super.willUpdate();

		// Ensure challenge questions are not duplicated
		clearDuplicateChallengeResponses();
	}

	/**
	 * Clears the new credential.
	 */
	@Override
	public void willRevert() {
		super.willRevert();
		newCredential = null;
	}

	/**
	 * Clears the new credential.
	 */
	@Override
	public void didUpdate() {
		super.didUpdate();
		newCredential = null;
	}

	/**
	 * Ensures the user has an authentication token if it is pre-activated
	 */
	@Override
	public void validateForSave() throws NSValidation.ValidationException {
		ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
		// Make sure all security questions are unique
		NSDictionary<ERChallengeQuestion, NSArray<ERChallengeResponse>> grouped = 
				ERXArrayUtilities.arrayGroupedByKeyPath(challengeResponses(), ERChallengeResponse.CHALLENGE_QUESTION);
		// Using Object key since null grouping key is a string...
		for(Object key : grouped.allKeys()) {
			if(!(key instanceof String) && grouped.objectForKey(key).count() > 1) {
				ERXValidationException ex = factory.createException(this, CHALLENGE_RESPONSES_KEY, challengeResponses(), "UniqueConstraintException.challengequestionid_userid_idx");
				throw ex;
			}
		}
		if (ERUserActivationStatus.PRE_ACTIVATION.equals(activationStatus()) && activateUserToken() == null) {
			ERXValidationException ex = factory.createCustomException(this, "MissingActivationToken");
			throw ex;
		}
		super.validateForSave();
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
		
		// Check for null
		if (clearPassword == null) {
			ex = factory.createException(this, CLEAR_PASSWORD_KEY, clearPassword, ERXValidationException.NullPropertyException);
			throw ex;
		}

		// Check length
		if (clearPassword.length() < clazz().minimumPasswordLength()) {
			ex = factory.createCustomException(this, CLEAR_PASSWORD_KEY, clearPassword, "PasswordLengthException");
			throw ex;
		}

		EOQualifier userQualifier = ERCredential.USER.eq(this);

		// Check previous passwords
		DateTime days = new DateTime().minusDays(clazz().passwordReuseDays());
		EOQualifier q = ERCredential.DATE_CREATED.greaterThan(days).and(userQualifier);
		NSArray<EOSortOrdering> sort = ERCredential.DATE_CREATED.descs();

		// Fetch all credentials used in the specified time period
		ERXFetchSpecification<ERCredential> fs = new ERXFetchSpecification<ERCredential>(ERCredential.ENTITY_NAME, q, sort);
		NSArray<ERCredential> timeResult = fs.fetchObjects(editingContext());

		// Fetch the last credentials used limited by reuse count
		ERXFetchSpecification<ERCredential> fsLimit = new ERXFetchSpecification<ERCredential>(ERCredential.ENTITY_NAME, userQualifier, sort);
		fsLimit.setFetchLimit(clazz().passwordReuseCount());
		NSArray<ERCredential> countResult = fsLimit.fetchObjects(editingContext());

		// Examine whichever is greatest
		NSArray<ERCredential> pastCredentials = timeResult.count() > countResult.count() ? timeResult : countResult;
		for (ERCredential past : pastCredentials) {
			if (past.hashMatches(clearPassword)) {
				ex = factory.createCustomException(this, CLEAR_PASSWORD_KEY, clearPassword, "PreviousPasswordException");
				throw ex;
			}
		}

		return clearPassword;
	}
	
	public String validateEmailAddress(String emailAddress) {
		if (!emailValidator.isValidEmailAddress(emailAddress, 100, true)) {
			ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
			ERXValidationException ex = factory.createException(this, EMAIL_ADDRESS_KEY, emailAddress,
					ERXValidationException.InvalidValueException);
			throw ex;
		}
		return emailAddress;
	}

	public NSArray<ERChallengeResponse> validateChallengeResponses(NSArray<ERChallengeResponse> value) {
		ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
		if (value.count() < clazz.requiredChallengeResponses()) {
			ERXValidationException ex = factory.createException(this, CHALLENGE_RESPONSES_KEY, value, "MinimumChallengeResponses");
			// TODO set the minimum number in the exception context?
			throw ex;
		}
		return value;
	}
}
