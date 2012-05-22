package er.corebl.mail;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import er.corebl.model.ERCMailAddress;
import er.corebl.model.ERCMailAttachment;
import er.corebl.model.ERCMailMessage;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXFetchSpecificationBatchIterator;
import er.javamail.ERMailDelivery;
import er.javamail.ERMailDeliveryHTML;
import er.javamail.ERMailDeliveryPlainText;

public enum ERCMailer {
	INSTANCE;

	private static final Logger log = Logger.getLogger(ERCMailer.class);

	public void processOutgoingMail() {
		log.debug("Begin processing outgoing mail");
		ERXFetchSpecificationBatchIterator iterator = ERCMailMessage.clazz.batchIteratorForUnsentMessages();

		EOEditingContext ec = ERXEC.newEditingContext();
		iterator.setEditingContext(ec);
		ec.lock();
		try {
			iterator.batchCount();
		} finally {
			ec.unlock();
		}
		ec.dispose();
		while (iterator.hasNextBatch()) {
			EOEditingContext temp = ERXEC.newEditingContext();
			temp.lock();
			try {
				iterator.setEditingContext(temp);
				sendMailMessages(iterator.nextBatch());
			} finally {
				temp.unlock();
			}
			temp.dispose();
		}

		log.debug("Finished processing outgoing mail.");
	}

	public void sendMailMessages(NSArray<ERCMailMessage> mailMessages) {
		if (mailMessages == null) {
			return;
		}
		mailMessages = ERCMailMessage.STATE.eq(ERCMailState.READY_TO_BE_SENT).filtered(mailMessages);
		if (mailMessages.isEmpty()) {
			return;
		}
		log.info("Sending " + mailMessages.count() + " mail message(s).");
		for (ERCMailMessage mailMessage : mailMessages) {
			if (log.isDebugEnabled()) {
				log.debug("Sending mail message: " + mailMessage);
			}

			try {
				mailMessage.setState(ERCMailState.PROCESSING);
				mailMessage.editingContext().saveChanges();

				ERMailDelivery delivery = createMailDeliveryForMailMessage(mailMessage);
				delivery.sendMail();
				mailMessage.setState(ERCMailState.SENT);
				mailMessage.setDateSent(new NSTimestamp());

			} catch (EOGeneralAdaptorException ge) {
				mailMessage.editingContext().revert();
			} catch (NoRecipientException e) {
				mailMessage.setState(ERCMailState.OPT_OUT);
			} catch (Exception e) {
				mailMessage.setState(ERCMailState.EXCEPTION);
				mailMessage.setExceptionReason(e.getMessage());
			} finally {
				if (mailMessage.editingContext().hasChanges()) {
					try {
						mailMessage.editingContext().saveChanges();
					} catch (RuntimeException e) {
						log.error("Runtime exception during save!", e);
						throw e;
					}
				}
			}

		}
	}

	public ERMailDelivery createMailDeliveryForMailMessage(ERCMailMessage message) 
			throws MessagingException, NoRecipientException {

		ERMailDelivery mail = null;
		if (message.htmlClob() != null) {
			ERMailDeliveryHTML html = ERMailDeliveryHTML.newMailDelivery();
			html.setHTMLContent(message.htmlClob().message());
			if (message.plainClob() != null) {
				html.setHiddenPlainTextContent(message.plainClob().message());
			}
			mail = html;
		} else {
			ERMailDeliveryPlainText plain = new ERMailDeliveryPlainText();
			plain.setTextContent(message.plainClob().message());
			mail = plain;
		}

		mail.setSubject(message.title());
		mail.setFromAddress(message.fromAddress().emailAddress());
		if (message.replyToAddress() != null) {
			mail.setReplyToAddress(message.replyToAddress().emailAddress());
		}

		if (!message.mailAttachments().isEmpty()) {
			for (ERCMailAttachment attachment : message.mailAttachments()) {
				if (mail instanceof ERMailDeliveryHTML && attachment.isInline().booleanValue()) {
					mail.addInlineAttachment(attachment.mailAttachment());
				} else {
					mail.addAttachment(attachment.mailAttachment());
				}
			}
		}

		message.removeOptOutRecipients();

		NSArray<ERCMailAddress> toAddresses = message.toAddresses();
		if (toAddresses.isEmpty()) {
			throw new NoRecipientException("No active or opt in 'to' recipients for this mail message.");
		}
		mail.setToAddresses(ERCMailAddress.EMAIL_ADDRESS.arrayValueInObject(toAddresses));

		NSArray<ERCMailAddress> ccAddresses = message.ccAddresses();
		if (!ccAddresses.isEmpty()) {
			mail.setCCAddresses(ERCMailAddress.EMAIL_ADDRESS.arrayValueInObject(ccAddresses));
		}

		NSArray<ERCMailAddress> bccAddresses = message.bccAddresses();
		if (!bccAddresses.isEmpty()) {
			mail.setBCCAddresses(ERCMailAddress.EMAIL_ADDRESS.arrayValueInObject(bccAddresses));
		}

		return mail;
	}
}
