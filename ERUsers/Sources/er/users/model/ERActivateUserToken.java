package er.users.model;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSDictionary;

import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;
import er.users.ERUserAction;
import er.users.model.enums.ERUserActivationStatus;

/**
 * This class is used to store a user activation token. When a user account is
 * created, an email is sent to the user. The user follows a link in the email
 * back to the site to activate the user account. This confirms that the email
 * address supplied for password recovery is valid.
 */
public class ERActivateUserToken extends er.users.model.eogen._ERActivateUserToken {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERActivateUserToken.class);

	public static final String TOKEN_KEY = "token";

	public static final ERActivateUserTokenClazz<ERActivateUserToken> clazz = new ERActivateUserTokenClazz<ERActivateUserToken>();

	public static class ERActivateUserTokenClazz<T extends ERActivateUserToken> extends
			er.users.model.eogen._ERActivateUserToken._ERActivateUserTokenClazz<T> {
		/* more clazz methods here */
	}

	/**
	 * Initializes the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
		_setValueForPrimaryKey(UUID.randomUUID().toString(), TOKEN_KEY);
	}

	/**
	 * Generates the href necessary to activate the related user.
	 * 
	 * @param context
	 *            the context
	 * @return the href string
	 */
	public String tokenHrefInContext(WOContext context) {
		NSDictionary<String, Object> queryDict = new NSDictionary<String, Object>(primaryKey(), ERUserAction.TOKEN_KEY);
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

	public ERUser validateUser(ERUser value) {
		// Only allow token to be set on pre-activated users
		if (!ERUserActivationStatus.PRE_ACTIVATION.equals(value.activationStatus())) {
			ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
			ERXValidationException ex = factory.createCustomException(this, USER_KEY, value,
					"NotPreActivatedUserException");
			throw ex;
		}
		return value;
	}

}
