package er.corebl.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

import er.extensions.net.ERXEmailValidator;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;

public class ERCMailAddress extends er.corebl.model.eogen._ERCMailAddress {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERCMailAddress.class);

	public static final ERCMailAddressClazz<ERCMailAddress> clazz = new ERCMailAddressClazz<ERCMailAddress>();

	public static class ERCMailAddressClazz<T extends ERCMailAddress> extends
			er.corebl.model.eogen._ERCMailAddress._ERCMailAddressClazz<T> {
		/* more clazz methods here */
		
		protected ERXEmailValidator emailValidator;
		
		public ERXEmailValidator emailValidator() {
			if(emailValidator == null) {
				emailValidator = new ERXEmailValidator(true, true);
			}
			return emailValidator;
		}
		
		public ERCMailAddress addressForEmailString(EOEditingContext ec, String email) {
			ERCMailAddress result = objectMatchingKeyAndValue(ec, EMAIL_ADDRESS_KEY, email);
			if(result == null) {
				result = createAndInsertObject(ec);
				result.setEmailAddress(email);
			}
			return result;
		}
	}

	/**
	 * Initializes the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
		setIsActive(Boolean.TRUE);
	}

	public String validateEmailAddress(String value) {
		if(!clazz.emailValidator().isValidEmailAddress(value, 100, true)) {
			ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
			throw factory.createException(this, EMAIL_ADDRESS_KEY, value, ERXValidationException.InvalidValueException);
		}
		return value;
	}
}
