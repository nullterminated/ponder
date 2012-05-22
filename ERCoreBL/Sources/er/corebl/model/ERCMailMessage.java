package er.corebl.model;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import er.corebl.mail.ERCMailRecipientType;
import er.corebl.mail.ERCMailState;
import er.extensions.eof.ERXFetchSpecificationBatchIterator;
import er.extensions.eof.ERXQ;
import er.extensions.validation.ERXValidationFactory;

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
		 * @param subject
		 *            of the message
		 * @param message
		 *            text of the message
		 * @param ec
		 *            editing context to create the mail message in.
		 * @return created mail message for the given parameters
		 */
		public T composeMailMessage(EOEditingContext ec, 
									ERCMailAddress from, 
									ERCMailAddress replyTo, 
									NSArray<ERCMailAddress> to,
									NSArray<ERCMailAddress> cc, 
									NSArray<ERCMailAddress> bcc, 
									String subject, 
									String htmlMessage, 
									String plainMessage, 
									NSArray<ERCMailAttachment> attachments, 
									ERCMailCategory category) {
			
			T mailMessage = createAndInsertObject(ec);
			
			mailMessage.addObjectToBothSidesOfRelationshipWithKey(from, FROM_ADDRESS_KEY);
			mailMessage.addObjectToBothSidesOfRelationshipWithKey(replyTo, REPLY_TO_ADDRESS_KEY);
			
			/*
			 * Setting in this order intentionally. To overrides CC overrides BCC
			 */
			if(bcc != null && !bcc.isEmpty()) {
				mailMessage.addToRecipients(bcc, ERCMailRecipientType.BCC);
			}
			if(cc != null && !cc.isEmpty()) {
				mailMessage.addToRecipients(cc, ERCMailRecipientType.CC);
			}
			if(to != null && !to.isEmpty()) {
				mailMessage.addToRecipients(to, ERCMailRecipientType.TO);
			}
			
			//Make sure the subject doesn't cause a max length validation exception
			int subjectLength = entity().attributeNamed(SUBJECT_KEY).width();
			if(subject.length() > subjectLength) {
				subject = subject.substring(0, subjectLength);
			}
			mailMessage.validateTakeValueForKeyPath(subject, SUBJECT_KEY);
			
			mailMessage.setHtmlMessage(htmlMessage);
			mailMessage.setPlainMessage(plainMessage);
			
			mailMessage.addObjectsToBothSidesOfRelationshipWithKey(attachments, MAIL_ATTACHMENTS_KEY);
			mailMessage.addObjectToBothSidesOfRelationshipWithKey(category, MAIL_CATEGORY_KEY);
			mailMessage.setState(ERCMailState.READY_TO_BE_SENT);
			
			return mailMessage;
		}
		
		public ERXFetchSpecificationBatchIterator batchIteratorForUnsentMessages() {
			EOQualifier q = STATE.eq(ERCMailState.READY_TO_BE_SENT);
			EOFetchSpecification fs = new EOFetchSpecification(ENTITY_NAME, q, null);
			return new ERXFetchSpecificationBatchIterator(fs);
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
	
	public void addToRecipients(ERCMailAddress address, ERCMailRecipientType type) {
		EOQualifier qualifier = 
				ERCMailRecipient.MAIL_ADDRESS.eq(address).and(ERCMailRecipient.MAIL_MESSAGE.eq(this));
		NSArray<ERCMailRecipient> recipients = 
				ERCMailRecipient.clazz.objectsMatchingQualifier(editingContext(), qualifier);
		if(recipients.count() > 1) {
			//Due to the unique index, there should be only 0 or 1
			String message = "More than one recipient was found for ERCMailMessage: " + this + " with address: " + address;
			throw new IllegalStateException(message);
		}
		ERCMailRecipient recipient = recipients.lastObject();
		if(recipient == null) {
			recipient = ERCMailRecipient.clazz.createAndInsertObject(editingContext());
			recipient.addObjectToBothSidesOfRelationshipWithKey(this, ERCMailRecipient.MAIL_MESSAGE_KEY);
			recipient.addObjectToBothSidesOfRelationshipWithKey(address, ERCMailRecipient.MAIL_ADDRESS_KEY);
		}
		recipient.setRecipientType(type);
	}
	
	public void addToRecipients(NSArray<ERCMailAddress> addresses, ERCMailRecipientType type) {
		for(ERCMailAddress address : addresses) {
			addToRecipients(address, type);
		}
	}
	
	public void removeOptOutRecipients() {
		EOQualifier q = ERCMailRecipient.MAIL_ADDRESS.dot(ERCMailAddress.IS_ACTIVE).isFalse();
		if(mailCategory() != null) {
			q = ERXQ.or(q, ERCMailRecipient.MAIL_ADDRESS.dot(ERCMailAddress.OPT_IN_CATEGORIES).containsObject(mailCategory()));
		}
		NSArray<ERCMailRecipient> optOut = EOQualifier.filteredArrayWithQualifier(mailRecipients(), q);
		removeObjectsFromBothSidesOfRelationshipWithKey(optOut, MAIL_RECIPIENTS_KEY);
	}
	
	public void setHtmlMessage(String value) {
		boolean isBlank = StringUtils.isBlank(value);
		if(isBlank && htmlClob() != null) {
			removeObjectFromBothSidesOfRelationshipWithKey(htmlClob(), HTML_CLOB_KEY);
		} else if(!isBlank) {
			ERCMailClob clob = ERCMailClob.clazz.createAndInsertObject(editingContext());
			clob.setMessage(value);
			addObjectToBothSidesOfRelationshipWithKey(clob, HTML_CLOB_KEY);
		}
	}
	
	public void setPlainMessage(String value) {
		boolean isBlank = StringUtils.isBlank(value);
		if(isBlank && plainClob() != null) {
			removeObjectFromBothSidesOfRelationshipWithKey(plainClob(), PLAIN_CLOB_KEY);
		} else if (!isBlank) {
			ERCMailClob clob = ERCMailClob.clazz.createAndInsertObject(editingContext());
			clob.setMessage(value);
			addObjectToBothSidesOfRelationshipWithKey(clob, PLAIN_CLOB_KEY);
		}
	}
	
	public NSArray<ERCMailAddress> addresses(ERCMailRecipientType type) {
		return ERCMailRecipient.clazz.addressesForMessageAndType(this, type);
	}
	
	public NSArray<ERCMailAddress> toAddresses() {
		return addresses(ERCMailRecipientType.TO);
	}
	
	public NSArray<ERCMailAddress> ccAddresses() {
		return addresses(ERCMailRecipientType.CC);
	}
	
	public NSArray<ERCMailAddress> bccAddresses() {
		return addresses(ERCMailRecipientType.BCC);
	}
	
	public String validateSubject(String value) {
		//CHECKME strip line breaks. Anything else?
		if(value.indexOf("\r") >= 0 || value.indexOf("\n") >= 0) {
			value = value.replaceAll("\\r\\n|\\r|\\n", " ");
		}
		return value;
	}
	
	public void validateForSave() {
		super.validateForSave();
		ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
		if(ERCMailState.READY_TO_BE_SENT.equals(state())) {
			if(toAddresses().isEmpty()) {
				throw factory.createCustomException(this, "RequiredToAddressException");
			}
			if(htmlClob() == null && plainClob() == null) {
				throw factory.createCustomException(this, "RequiredMessageTextException");
			}
		}
	}
}
