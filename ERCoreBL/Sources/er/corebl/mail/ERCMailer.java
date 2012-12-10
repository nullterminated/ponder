package er.corebl.mail;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOKeyGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSForwardException;

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
	
	private String messageID;
	
	private long sendRate;
	
	public String messageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public long sendRate() {
		return sendRate;
	}

	public void setSendRate(long sendRate) {
		this.sendRate = sendRate;
	}

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
			sendMailMessage(mailMessage);
		}
	}

	public synchronized void sendMailMessage(ERCMailMessage mailMessage) {
		if (log.isDebugEnabled()) {
			log.debug("Sending mail message: " + mailMessage);
		}

		EOEditingContext ec = mailMessage.editingContext();
		
//		if(mailMessage.hasUnverifiedRecipients()) {
//			try {
//				mailMessage.setState(ERCMailState.WAIT);
//				ec.saveChanges();
//			} catch (EOGeneralAdaptorException ge) {
//				ec.revert();
//				return;
//			}
//		}

		if(mailMessage.hasOptOutRecipients()) {
			try {
				mailMessage.setState(ERCMailState.OPT_OUT);
				ec.saveChanges();
				return;
			} catch (EOGeneralAdaptorException ge) {
				ec.revert();
				return;
			}
		}
		
		if(mailMessage.hasSuppressedRecipients()) {
			try {
				mailMessage.setState(ERCMailState.SUPPRESSED);
				ec.saveChanges();
				return;
			} catch (EOGeneralAdaptorException ge) {
				ec.revert();
				return;
			}
		}
		
		try {
			mailMessage.setState(ERCMailState.PROCESSING);
			ec.saveChanges();
		} catch (EOGeneralAdaptorException ge) {
			ec.revert();
			return;
		}
		
		ERMailDelivery delivery;
		try {
			delivery = createMailDeliveryForMailMessage(mailMessage);
		} catch (MessagingException e) {
			mailMessage.setState(ERCMailState.EXCEPTION);
			mailMessage.setException(e);
			ec.saveChanges();
			return;
		}

		try {
			//add a delegate to set sent/exception state
			EOKeyGlobalID gid = mailMessage.permanentGlobalID();
			ERCMessageDelegate delegate = new ERCMessageDelegate(gid);
			delivery.setDelegate(delegate);
			delivery.sendMail(true);
			
			if(ERCMailState.SENT.equals(mailMessage.state())) {
				if(messageID != null) {
					mailMessage.setMessageID(messageID);
					messageID = null;
				}
			}
		} catch (Exception e) {
			mailMessage.setState(ERCMailState.EXCEPTION);
			mailMessage.setException(e);
		}
		
		ec.saveChanges();
		
		if(ERCMailState.SENT.equals(mailMessage.state()) && sendRate > 0) {
			int recipients = mailMessage.mailRecipients().count();
			try {
				Thread.sleep(recipients * sendRate);
			} catch (InterruptedException e) {
				throw NSForwardException._runtimeExceptionForThrowable(e);
			}
		}

	}

	public ERMailDelivery createMailDeliveryForMailMessage(ERCMailMessage message) 
			throws MessagingException {

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

		mail.setSubject(message.subject());
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

		NSArray<ERCMailAddress> toAddresses = message.toAddresses();
		if (!toAddresses.isEmpty()) {
			mail.setToAddresses(ERCMailAddress.EMAIL_ADDRESS.arrayValueInObject(toAddresses));
		}

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
