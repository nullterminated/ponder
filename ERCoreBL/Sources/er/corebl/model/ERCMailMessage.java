package er.corebl.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.corebl.mail.ERCMailState;

public class ERCMailMessage extends er.corebl.model.eogen._ERCMailMessage {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERCMailMessage.class);

	public static final ERCMailMessageClazz<ERCMailMessage> clazz = new ERCMailMessageClazz<ERCMailMessage>();

	public static class ERCMailMessageClazz<T extends ERCMailMessage> extends
			er.corebl.model.eogen._ERCMailMessage._ERCMailMessageClazz<T> {

		/**
		 * Composes a mail message.
		 * 
		 * @param from
		 *            email address
		 * @param to
		 *            email addresses
		 * @param cc
		 *            email addresses
		 * @param bcc
		 *            email addresses
		 * @param title
		 *            of the message
		 * @param message
		 *            text of the message
		 * @param ec
		 *            editing context to create the mail message in.
		 * @return created mail message for the given parameters
		 */
		public T composeMailMessage(EOEditingContext ec, 
									String from, 
									NSArray<ERCMailAddress> to,
									NSArray<ERCMailAddress> cc, 
									NSArray<ERCMailAddress> bcc, 
									String title, 
									String htmlMessage, 
									String plainMessage, 
									NSArray<ERCMailAttachment> attachements, 
									ERCMailCategory category) {
			
			T mailMessage = createAndInsertObject(ec);
			int titleLength = entity().attributeNamed(TITLE_KEY).width();
			if(title.length() > titleLength) {
				title = title.substring(0, titleLength);
			}
			mailMessage.validateTakeValueForKeyPath(title, TITLE_KEY);
			mailMessage.validateTakeValueForKeyPath(from, FROM_ADDRESS_KEY);
			mailMessage.validateTakeValueForKeyPath(category, MAIL_CATEGORY_KEY);
			mailMessage.validateTakeValueForKeyPath(ERCMailState.READY_TO_BE_SENT, STATE_KEY);
			mailMessage.setText(message);

			return mailMessage;
		}
	}

	/**
	 * Initializes the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
		setState(ERCMailState.DRAFT);
		setUuid(java.util.UUID.randomUUID().toString());
	}
}
