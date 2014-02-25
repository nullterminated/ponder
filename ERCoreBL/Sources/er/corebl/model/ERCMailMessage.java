package er.corebl.model;

import javax.mail.MessagingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.webobjects.appserver.WOComponent;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import er.corebl.mail.ERCMailAddressVerification;
import er.corebl.mail.ERCMailRecipientType;
import er.corebl.mail.ERCMailState;
import er.extensions.eof.ERXFetchSpecificationBatchIterator;
import er.extensions.validation.ERXValidationFactory;
import er.javamail.ERMailDelivery;
import er.javamail.ERMailDeliveryHTML;
import er.javamail.ERMailDeliveryPlainText;

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
		 * Compose a mail message.
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
		 * @param htmlMessage
		 *            text of the HTML message
		 * @param plainMessage
		 *            text of the plain text message
		 * @param attachments
		 *            the mail attachments
		 * @param category
		 *            the mail message category
		 * @param ec
		 *            editing context to create the mail message in.
		 * @return created mail message for the given parameters
		 */
		public T composeMailMessage(EOEditingContext ec,
									ERCMailState state,
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
			
			if(replyTo != null) {
				mailMessage.addObjectToBothSidesOfRelationshipWithKey(replyTo, REPLY_TO_ADDRESS_KEY);
			}
			
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
			if(subject != null && subject.length() > subjectLength) {
				subject = subject.substring(0, subjectLength);
			}
			mailMessage.validateTakeValueForKeyPath(subject, SUBJECT_KEY);
			
			mailMessage.setHtmlMessage(htmlMessage);
			mailMessage.setPlainMessage(plainMessage);
			
			if(attachments != null) {
				mailMessage.addObjectsToBothSidesOfRelationshipWithKey(attachments, MAIL_ATTACHMENTS_KEY);
			}
			
			if(category != null) {
				mailMessage.addObjectToBothSidesOfRelationshipWithKey(category, MAIL_CATEGORY_KEY);
			}
			
			mailMessage.setState(state);
			
			return mailMessage;
		}
		
		/**
		 * Compose a mail message.
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
		 * @param htmlComponent
		 *            component for the HTML message
		 * @param plainComponent
		 *            component for the plain text message
		 * @param attachments
		 *            the mail attachments
		 * @param category
		 *            the mail message category
		 * @param ec
		 *            editing context to create the mail message in.
		 * @return created mail message for the given parameters
		 */
		public T composeComponentMailMessage(EOEditingContext ec,
				ERCMailState state,
				ERCMailAddress from,
				ERCMailAddress replyTo,
				NSArray<ERCMailAddress> to,
				NSArray<ERCMailAddress> cc,
				NSArray<ERCMailAddress> bcc,
				String subject,
				WOComponent htmlComponent,
				WOComponent plainComponent,
				NSArray<ERCMailAttachment> attachments,
				ERCMailCategory category) {
			
			String htmlMessage = htmlComponent == null?null:componentContentWithFullURLs(htmlComponent);
			String plainMessage = plainComponent == null?null:componentContentWithFullURLs(plainComponent);
			
			return composeMailMessage(ec, state, from, replyTo, to, cc, bcc, subject, htmlMessage, plainMessage, attachments, category);
		}
		
		/**
		 * Compose a mail message.
		 *
		 * @param from
		 *            email address
		 * @param to
		 *            email address
		 * @param cc
		 *            email address
		 * @param bcc
		 *            email address
		 * @param subject
		 *            of the message
		 * @param htmlMessage
		 *            text of the HTML message
		 * @param plainMessage
		 *            text of the plain text message
		 * @param ec
		 *            editing context to create the mail message in.
		 * @return created mail message for the given parameters
		 */
		public T composeMailMessage(EOEditingContext ec,
				String from,
				String replyTo,
				String to,
				String cc,
				String bcc,
				String subject,
				String htmlMessage,
				String plainMessage) {
			
			return composeMailMessage(ec,
					ERCMailState.READY_TO_BE_SENT,
					from==null?null:ERCMailAddress.clazz.addressForEmailString(ec, from),
					replyTo==null?null:ERCMailAddress.clazz.addressForEmailString(ec, replyTo),
					to==null?null:ERCMailAddress.clazz.addressesForEmailStrings(ec, to),
					cc==null?null:ERCMailAddress.clazz.addressesForEmailStrings(ec, cc),
					bcc==null?null:ERCMailAddress.clazz.addressesForEmailStrings(ec, bcc),
					subject,
					htmlMessage,
					plainMessage,
					null,
					null);
		}
		
		/**
		 * Compose a mail message.
		 *
		 * @param from
		 *            email address
		 * @param to
		 *            email address
		 * @param subject
		 *            of the message
		 * @param htmlMessage
		 *            text of the HTML message
		 * @param plainMessage
		 *            text of the plain text message
		 * @param ec
		 *            editing context to create the mail message in.
		 * @return created mail message for the given parameters
		 */
		public T composeMailMessage(EOEditingContext ec,
				String from,
				String to,
				String subject,
				String htmlMessage,
				String plainMessage) {
			
			return composeMailMessage(ec, from, null, to, null, null, subject, htmlMessage, plainMessage);
		}
		
		public ERXFetchSpecificationBatchIterator batchIteratorForUnsentMessages() {
			EOQualifier q = STATE.eq(ERCMailState.READY_TO_BE_SENT);
			EOFetchSpecification fs = new EOFetchSpecification(ENTITY_NAME, q, null);
			return new ERXFetchSpecificationBatchIterator(fs);
		}
		
		public String componentContentWithFullURLs(WOComponent component) {
			boolean complete = component.context().doesGenerateCompleteURLs();
			if(!complete) {
				component.context().generateCompleteURLs();
			}
			try {
				String content = component.generateResponse().contentString();
				return content;
			} finally {
				if(!complete) {
					component.context().generateRelativeURLs();
				}
			}
		}
	}
	
	/**
	 * Initialize the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
		setState(ERCMailState.DRAFT);
		setUuid(java.util.UUID.randomUUID().toString());
	}
	
	public ERMailDelivery createMailDeliveryForMailMessage()
			throws MessagingException {
		
		ERMailDelivery mail = null;
		if (htmlClob() != null) {
			ERMailDeliveryHTML html = ERMailDeliveryHTML.newMailDelivery();
			html.setHTMLContent(htmlClob().message());
			if (plainClob() != null) {
				html.setHiddenPlainTextContent(plainClob().message());
			}
			mail = html;
		} else {
			ERMailDeliveryPlainText plain = new ERMailDeliveryPlainText();
			plain.setTextContent(plainClob().message());
			mail = plain;
		}

		mail.setSubject(subject());
		mail.setFromAddress(fromAddress().emailAddress());
		if (replyToAddress() != null) {
			mail.setReplyToAddress(replyToAddress().emailAddress());
		}

		if (!mailAttachments().isEmpty()) {
			for (ERCMailAttachment attachment : mailAttachments()) {
				if (mail instanceof ERMailDeliveryHTML && attachment.isInline().booleanValue()) {
					mail.addInlineAttachment(attachment.mailAttachment());
				} else {
					mail.addAttachment(attachment.mailAttachment());
				}
			}
		}

		NSArray<ERCMailAddress> toAddresses = toAddresses();
		if (!toAddresses.isEmpty()) {
			mail.setToAddresses(ERCMailAddress.EMAIL_ADDRESS.arrayValueInObject(toAddresses));
		}

		NSArray<ERCMailAddress> ccAddresses = ccAddresses();
		if (!ccAddresses.isEmpty()) {
			mail.setCCAddresses(ERCMailAddress.EMAIL_ADDRESS.arrayValueInObject(ccAddresses));
		}

		NSArray<ERCMailAddress> bccAddresses = bccAddresses();
		if (!bccAddresses.isEmpty()) {
			mail.setBCCAddresses(ERCMailAddress.EMAIL_ADDRESS.arrayValueInObject(bccAddresses));
		}

		return mail;
	}

	public void addToRecipients(ERCMailAddress address, ERCMailRecipientType type) {
		EOQualifier qualifier = ERCMailRecipient.MAIL_ADDRESS.eq(address);
		NSArray<ERCMailRecipient> recipients = mailRecipients(qualifier);
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
	
	public boolean hasUnverifiedRecipients() {
		return !unverifiedRecipients().isEmpty();
	}
	
	public NSArray<ERCMailRecipient> unverifiedRecipients() {
		EOQualifier q = ERCMailRecipient.MAIL_ADDRESS.dot(ERCMailAddress.VERIFICATION_STATE).eq(ERCMailAddressVerification.UNVERIFIED);
		NSArray<ERCMailRecipient> unverified = mailRecipients(q);
		return unverified;
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
	
	public void setException(Throwable t) {
		setState(ERCMailState.EXCEPTION);
		String message = t.getMessage();
		int max = clazz().entity().attributeNamed(EXCEPTION_REASON_KEY).width();
		if(message != null && message.length() > max) {
			message = message.substring(0, max);
		}
		setExceptionReason(message);
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
