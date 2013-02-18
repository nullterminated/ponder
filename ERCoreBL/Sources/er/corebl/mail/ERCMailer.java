package er.corebl.mail;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOKeyGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSForwardException;

import er.corebl.model.ERCMailMessage;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXFetchSpecificationBatchIterator;
import er.javamail.ERMailDelivery;

/**
 * The ERCMailer is a singleton to send mail messages at a specified
 * send rate.
 *
 */
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

	public void sendMailMessage(ERCMailMessage mailMessage) {
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
			delivery = mailMessage.createMailDeliveryForMailMessage();
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
			sendMailMessage(delivery, mailMessage);
		} catch (Exception e) {
			mailMessage.setState(ERCMailState.EXCEPTION);
			mailMessage.setException(e);
		}
		
		ec.saveChanges();
		
	}
	
	/**
	 * This method provides a bottleneck to regulate the send rate of the mailer.
	 * It simply sends the ERMailDelivery while blocking.
	 * 
	 * @param delivery the mail deliver to send
	 * @param mailMessage the mailMessage EO
	 */
	public synchronized void sendMailMessage(ERMailDelivery delivery, ERCMailMessage mailMessage) {
		delivery.sendMail(true);
		if(ERCMailState.SENT.equals(mailMessage.state())) {
			if(messageID != null) {
				mailMessage.setMessageID(messageID);
				messageID = null;
			}
			if(sendRate > 0) {
				try {
					Thread.sleep(mailMessage.mailRecipients().count() * sendRate);
				} catch (InterruptedException e) {
					throw NSForwardException._runtimeExceptionForThrowable(e);
				}
			}
		}
	}

}
