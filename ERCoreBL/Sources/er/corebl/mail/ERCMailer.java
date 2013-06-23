package er.corebl.mail;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOKeyGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSTimestamp;

import er.corebl.model.ERCMailAddress;
import er.corebl.model.ERCMailAttachment;
import er.corebl.model.ERCMailMessage;
import er.corebl.model.ERCMailRecipient;
import er.extensions.appserver.ERXApplication;
import er.extensions.appserver.ERXWOContext;
import er.extensions.concurrency.ERXRunnable;
import er.extensions.eof.ERXBatchFetchUtilities;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXFetchSpecificationBatchIterator;
import er.javamail.ERMailDelivery;

/**
 * The ERCMailer is a singleton to send mail messages at a specified
 * send rate. Since the send rate is maintained in the executing
 * process, the mailer should only be running on a single instance.
 */
public enum ERCMailer {
	INSTANCE;

	private static final Logger log = Logger.getLogger(ERCMailer.class);
	
	private static final NSArray<String> prefetch = new NSArray<String>(
			ERCMailMessage.MAIL_RECIPIENTS_KEY,
			ERCMailMessage.MAIL_RECIPIENTS.dot(ERCMailRecipient.MAIL_ADDRESS).key(),
			ERCMailMessage.HTML_CLOB_KEY,
			ERCMailMessage.PLAIN_CLOB_KEY,
			ERCMailMessage.MAIL_ATTACHMENTS_KEY,
			ERCMailMessage.MAIL_ATTACHMENTS.dot(ERCMailAttachment.ATTACHMENT).key(),
			ERCMailMessage.FROM_ADDRESS_KEY,
			ERCMailMessage.REPLY_TO_ADDRESS_KEY
			);
	
	private ThreadLocal<String> _messageID = new ThreadLocal<String>();
	
	private long sendRate;
	
	private final Semaphore semaphore = new Semaphore(1, true);
	
	private final ThreadPoolExecutor releasePool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	
	private ThreadPoolExecutor executorPool = new ThreadPoolExecutor(8, 8, 0L, TimeUnit.MILLISECONDS,  new SynchronousQueue<Runnable>(true)){
		protected void afterExecute(Runnable future, Throwable t) {
			super.afterExecute(future, t);
			if(t != null) {
				log.error("Error in executor pool", t);
				ERXApplication.erxApplication().reportException(t, null, null);
			}
		};
	};
	
	private ScheduledExecutorService scheduledExecutor;
	
	private MailerDelegate delegate = new MailerDelegate() {
		private SingleSenderDelegate _delegate = new SingleSenderDelegate();
		
		@Override
		public void willCreateDelivery(ERCMailMessage message) {
			// Does nothing
		}
		
		@Override
		public boolean shouldSendMessage(ERCMailMessage message) {
			return _delegate.shouldSendMessage(message);
		}

		@Override
		public void didSendMessage(ERCMailMessage message) {
			// Does nothing
		}

		@Override
		public void failedWithException(ERCMailMessage message, Throwable t) {
			// Does nothing
		}
	};
	
	/**
	 * Set the MailerDelegate for the mailer. The default delegate
	 * just uses the logic in the {@link SingleSenderDelegate} class.
	 * If you need to modify messages before sending them for CAN
	 * SPAM Act compliance, have a look at the {@link CSAMailerDelegate}
	 * class.
	 *
	 * @param delegate the mailer delegate to set
	 */
	public void setMailerDelegate(MailerDelegate delegate) {
		if(delegate == null) { throw new IllegalArgumentException("Delegate not allowed to be null"); }
		this.delegate = delegate;
	}
	
	/**
	 * A MailerCallback allows for pre/post processing.
	 */
	public interface MailerCallback {
		public void preProcess();
		public void postProcess();
	}
	
	/**
	 * A MailerDelegate allows for message suppression and
	 * modification that is beyond the scope of the mailer
	 * daemon.
	 */
	public interface MailerDelegate {
		public boolean shouldSendMessage(ERCMailMessage message);
		public void willCreateDelivery(ERCMailMessage message);
		public void didSendMessage(ERCMailMessage message);
		public void failedWithException(ERCMailMessage message, Throwable t);
	}
	
	/**
	 * The Message sender class sends messages on a background thread.
	 */
	private static class MessageSender extends ERXRunnable {
		private final EOKeyGlobalID _gid;
		private final ERMailDelivery _delivery;
		
		private static final NSArray<String> keypaths = new NSArray<String>(
				ERCMailMessage.MAIL_RECIPIENTS_KEY,
				ERCMailMessage.MAIL_RECIPIENTS.dot(ERCMailRecipient.MAIL_ADDRESS).key()
				);
		
		public MessageSender(EOKeyGlobalID gid, ERMailDelivery delivery) {
			_gid = gid;
			_delivery = delivery;
		}
		
		@Override
		public void _run() {
			EOEditingContext ec = ERXEC.newEditingContext();
			ERCMailMessage message = (ERCMailMessage) ec.faultForGlobalID(_gid, ec);
			ERXBatchFetchUtilities.batchFetch(new NSArray<ERCMailMessage>(message), keypaths);
			try {
				long wait = message.mailRecipients().count() * ERCMailer.INSTANCE.sendRate();
				ERCMailer.INSTANCE.sendMailMessage(_delivery, wait);
			} catch (Exception e) {
				message.setState(ERCMailState.EXCEPTION);
				message.setException(e);
				ERCMailer.INSTANCE.delegate.failedWithException(message, e);
			}
			message.setState(ERCMailState.SENT);
			NSTimestamp now = new NSTimestamp();
			message.setDateSent(now);
			ERCMailMessage.MAIL_RECIPIENTS.dot(ERCMailRecipient.MAIL_ADDRESS).dot(ERCMailAddress.DATE_LAST_SENT).takeValueInObject(now, message);
			if(ERCMailer.INSTANCE.messageID() != null) {
				message.setMessageID(ERCMailer.INSTANCE.messageID());
				ERCMailer.INSTANCE.setMessageID(null);
			}
			ERCMailer.INSTANCE.delegate.didSendMessage(message);
			ERXWOContext.setCurrentContext(null);
			ec.saveChanges();
		}
	}

	private void release(final long millis) {
		releasePool.submit(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(millis);
				} catch (InterruptedException e) {
					throw NSForwardException._runtimeExceptionForThrowable(e);
				}
				semaphore.release();
			}
		});
	}

	public String messageID() {
		return _messageID.get();
	}

	public void setMessageID(String messageID) {
		_messageID.set(messageID);
	}

	public long sendRate() {
		return sendRate;
	}

	public void setSendRate(long sendRate) {
		this.sendRate = sendRate;
	}
	
	public int numberOfThreads() {
		return executorPool.getCorePoolSize();
	}
	
	public void setNumberOfThreads(int poolSize) {
		executorPool.setCorePoolSize(poolSize);
		executorPool.setMaximumPoolSize(poolSize);
	}
	
	public boolean isMailerActive() {
		return scheduledExecutor != null && !scheduledExecutor.isTerminated();
	}
	
	public void startMailer(int period, final MailerCallback callback) {
		if(isMailerActive()) {
			return;
		}
		scheduledExecutor = new ScheduledThreadPoolExecutor(1) {
			@Override
			protected void afterExecute(Runnable future, Throwable t) {
				super.afterExecute(future, t);
				if(t != null) {
					log.error("Error in executor pool", t);
					ERXApplication.erxApplication().reportException(t, null, null);
				}

			}
		};
		ERXRunnable command = new ERXRunnable() {
			@Override
			public void _run() {
				if(callback != null) { callback.preProcess(); }
				ERCMailer.INSTANCE.processOutgoingMail();
				if(callback != null) { callback.postProcess(); }
			}
		};
		scheduledExecutor.scheduleAtFixedRate(command, 0, period, TimeUnit.MINUTES);
	}
	
	public void stopMailer() {
		if(scheduledExecutor != null) {
			scheduledExecutor.shutdownNow();
		}
		EOEditingContext ec = ERXEC.newEditingContext();
		ec.lock();
		try {
			NSArray<ERCMailMessage> messages = ERCMailMessage.clazz.objectsMatchingQualifier(ec, ERCMailMessage.STATE.eq(ERCMailState.PROCESSING));
			ERCMailMessage.STATE.takeValueInObject(ERCMailState.READY_TO_BE_SENT, messages);
			ec.saveChanges();
		} finally {
			ec.unlock();
		}
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
		
		ERXBatchFetchUtilities.batchFetch(mailMessages, prefetch);
		
		log.info("Sending " + mailMessages.count() + " mail message(s).");
		for (ERCMailMessage mailMessage : mailMessages) {
			_sendMailMessage(mailMessage);
		}
		log.info("Mail sent!");
	}
	
	public void sendMailMessage(ERCMailMessage mailMessage) {
		ERXBatchFetchUtilities.batchFetch(mailMessage, prefetch, false);
		_sendMailMessage(mailMessage);
	}

	private void _sendMailMessage(ERCMailMessage mailMessage) {
		if(!ERCMailState.READY_TO_BE_SENT.equals(mailMessage.state())) {
			return;
		}
		
		if(!delegate.shouldSendMessage(mailMessage)) {
			return;
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Sending mail message: " + mailMessage);
		}

		EOEditingContext ec = mailMessage.editingContext();
		try {
			mailMessage.setState(ERCMailState.PROCESSING);
			ec.saveChanges();
		} catch (EOGeneralAdaptorException ge) {
			ec.revert();
			return;
		}
		
		delegate.willCreateDelivery(mailMessage);
		
		ERMailDelivery delivery = null;
		try {
			delivery = mailMessage.createMailDeliveryForMailMessage();
		} catch (MessagingException e) {
			mailMessage.setState(ERCMailState.EXCEPTION);
			mailMessage.setException(e);
			ec.saveChanges();
			return;
		}

		EOKeyGlobalID gid = mailMessage.permanentGlobalID();
		MessageSender sender = new MessageSender(gid, delivery);
		
		boolean submitted = false;
		while(!submitted) {
			try {
				executorPool.submit(sender);
				submitted = true;
			} catch(RejectedExecutionException rre) {
				/* Wait for an available thread */
				if(sendRate > 0) {
					try {
						Thread.sleep(sendRate);
					} catch (InterruptedException ie) {
						// Thread cancelled? Give up waiting.
						break;
					}
				} else {
					Thread.yield();
				}
			}
		}
	}
	
	/**
	 * Provide a bottleneck for rate limited sending when 'wait' is greater than 0.
	 *
	 * @param delivery the mail delivery to send
	 * @param wait the wait time in milliseconds
	 */
	private void sendMailMessage(ERMailDelivery delivery, long wait) {
		if(wait > 0) {
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				throw NSForwardException._runtimeExceptionForThrowable(e);
			}
			release(wait);
		}
		delivery.sendMail(true);
	}

}
