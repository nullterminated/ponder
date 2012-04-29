package er.users.model;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.webobjects.eocontrol.EOEditingContext;

import er.extensions.crypting.ERXCrypto;

/**
 * ERCredential records the old password hashes so that password reuse
 * can be limited.
 */
public class ERCredential extends er.users.model.eogen._ERCredential {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERCredential.class);

	public static final ERCredentialClazz<ERCredential> clazz = new ERCredentialClazz<ERCredential>();

	public static class ERCredentialClazz<T extends ERCredential> extends
			er.users.model.eogen._ERCredential._ERCredentialClazz<T> {
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
	 *            the clear text password to hash
	 * @return true if the hashing clearPassword matches the stored hash
	 */
	public boolean hashMatches(String clearPassword) {
		return password().equals(hashedPassword(clearPassword));
	}

}
