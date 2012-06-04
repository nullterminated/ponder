package er.users.model;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

import er.extensions.crypting.ERXCrypto;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;
import er.users.ERUsers;

public class ERChallengeResponse extends er.users.model.eogen._ERChallengeResponse {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(ERChallengeResponse.class);

	public static final String CLEAR_ANSWER_KEY = "clearAnswer";

	public static final ERChallengeResponseClazz<ERChallengeResponse> clazz = new ERChallengeResponseClazz<ERChallengeResponse>();

	public static class ERChallengeResponseClazz<T extends ERChallengeResponse> extends
			er.users.model.eogen._ERChallengeResponse._ERChallengeResponseClazz<T> {
		/* more clazz methods here */
	}

	/**
	 * Initializes the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
		setSalt(ERUsers.sharedInstance().generateSalt());
	}

	/**
	 * Set's the answer to the challenge response from the clear text answer.
	 * This should only be set once.
	 * 
	 * @param clearAnswer
	 *            the clear text answer
	 */
	public void setClearAnswer(String clearAnswer) {
		log.debug("setting answer");
		if(answer() != null) {
			throw new IllegalArgumentException("The answer for this challenge response is already set: " + this);
		}
		String answer = hashedAnswer(clearAnswer);
		setAnswer(answer);
	}
	
	public Object handleQueryWithUnboundKey(String key) {
		if("clearAnswer".equals(key)) {
			return null;
		}
		return super.handleQueryWithUnboundKey(key);
	}

	/**
	 * Returns a string value for the clear answer after passing it through a
	 * one way hash function.
	 * 
	 * @param clearAnswer
	 *            the clear text answer
	 * @return the hashed answer
	 */
	public String hashedAnswer(String clearAnswer) {
		String sha = ERXCrypto.sha512Encode(clearAnswer + salt());
		return sha;
	}

	/**
	 * @param clearAnswer
	 *            the clear text answer to compare
	 * @return true if the hashing clearAnswer matches the stored hash
	 */
	public boolean hashMatches(String clearAnswer) {
		return answer().equals(hashedAnswer(clearAnswer));
	}
	
	/**
	 * Ensures that the clear answer is not null or empty.
	 * 
	 * @param clearAnswer
	 * @return
	 */
	public String validateClearAnswer(String clearAnswer) {
		if(StringUtils.isBlank(clearAnswer)) {
			ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
			ERXValidationException ex = factory.createException(this, CLEAR_ANSWER_KEY, clearAnswer, ERXValidationException.NullPropertyException);
			throw ex;
		}
		return clearAnswer;
	}

}
