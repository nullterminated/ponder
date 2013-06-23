package er.corebl.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import er.corebl.mail.ERCMailRecipientType;

public class ERCMailRecipient extends er.corebl.model.eogen._ERCMailRecipient {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERCMailRecipient.class);

	public static final ERCMailRecipientClazz<ERCMailRecipient> clazz = new ERCMailRecipientClazz<ERCMailRecipient>();

	public static class ERCMailRecipientClazz<T extends ERCMailRecipient> extends
			er.corebl.model.eogen._ERCMailRecipient._ERCMailRecipientClazz<T> {
		/* more clazz methods here */
		
		public NSArray<ERCMailRecipient> recipientsForMessageAndType(ERCMailMessage message, ERCMailRecipientType type) {
			EOQualifier q = MAIL_MESSAGE.eq(message).and(RECIPIENT_TYPE.eq(type));
			NSArray<ERCMailRecipient> recipients = EOQualifier.filteredArrayWithQualifier(message.mailRecipients(), q);
			return recipients;
		}
		
		public NSArray<ERCMailAddress> addressesForMessageAndType(ERCMailMessage message, ERCMailRecipientType type) {
			NSArray<ERCMailRecipient> recipients = recipientsForMessageAndType(message, type);
			NSArray<ERCMailAddress> addresses = MAIL_ADDRESS.arrayValueInObject(recipients);
			return addresses;
		}
	}

	/**
	 * Initialize the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
	}

}
