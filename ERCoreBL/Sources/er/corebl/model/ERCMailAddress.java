package er.corebl.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.corebl.mail.ERCMailAddressVerification;
import er.extensions.eof.ERXBatchFetchUtilities;
import er.extensions.eof.ERXEC;
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
				EOEditingContext newEc = ERXEC.newEditingContext(ec.parentObjectStore());
				result = createAndInsertObject(newEc);
				result.setEmailAddress(email);
				try {
					newEc.saveChanges();
					result = (ERCMailAddress) result.localInstanceIn(ec);
				} catch (RuntimeException e) {
					// CHECKME only catch unique index exceptions?
					result = objectMatchingKeyAndValue(ec, EMAIL_ADDRESS_KEY, email);
					if(result == null) {
						throw e;
					}
				}
			}
			return result;
		}
		
		public NSArray<ERCMailAddress> addressesForEmailStrings(EOEditingContext ec, String... addresses) {
			return addressesForEmailStrings(ec, new NSArray<String>(addresses));
		}
		
		public NSArray<ERCMailAddress> addressesForEmailStrings(EOEditingContext ec, NSArray<String> emailStrings) {
			NSMutableArray<ERCMailAddress> addresses = new NSMutableArray<ERCMailAddress>();
			for(String emailString: emailStrings) {
				ERCMailAddress address = addressForEmailString(ec, emailString);
				addresses.add(address);
			}
			return addresses.immutableClone();
		}
		
		public NSArray<ERCMailAddress> filterAddressesForCategory(NSArray<ERCMailAddress> addresses, ERCMailCategory category) {
			EOQualifier qualifier = null;
			if(category != null) {
				ERXBatchFetchUtilities.batchFetch(addresses, OPT_IN_CATEGORIES_KEY);
				qualifier = OPT_IN_CATEGORIES.containsObject(category);
			}
			return EOQualifier.filteredArrayWithQualifier(addresses, qualifier);
		}
	}

	/**
	 * Initialize the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
		setVerificationState(ERCMailAddressVerification.UNVERIFIED);
	}

	public String validateEmailAddress(String value) {
		if(!clazz().emailValidator().isValidEmailString(value)) {
			ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
			throw factory.createException(this, EMAIL_ADDRESS_KEY, value, ERXValidationException.InvalidValueException);
		}
		return value;
	}
}
