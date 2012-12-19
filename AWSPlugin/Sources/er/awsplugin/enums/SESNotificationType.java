package er.awsplugin.enums;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSTimestamp;

import er.awsplugin.model.SESBounceNotification;
import er.awsplugin.model.SESComplaintNotification;
import er.awsplugin.model.SESNotification;
import er.corebl.mail.ERCMailStopReason;
import er.corebl.model.ERCMailAddress;
import er.corebl.model.ERCMailMessage;

public enum SESNotificationType {
	BOUNCE(ERCMailStopReason.BOUNCED) {
		@Override
		public void createNotificationRecords(EOEditingContext ec, NSDictionary<String, Object> json) {
			NSArray<NSDictionary<String, Object>> recipients = 
					(NSArray<NSDictionary<String, Object>>) json.valueForKeyPath("Message.bounce.bouncedRecipients");
			SimpleDateFormat format = new SimpleDateFormat(JSON_DATE_FORMAT_STRING);
			
			for(NSDictionary<String, Object> recipientDict : recipients) {
				SESBounceNotification notification = SESBounceNotification.clazz.createAndInsertObject(ec);
				String email = (String) recipientDict.valueForKey("Message.emailAddress");
				ERCMailAddress address = ERCMailAddress.clazz.addressForEmailString(ec, email);
				notification.setMailAddressRelationship(address);
				email = (String) json.valueForKeyPath("Message.mail.source");
				address = ERCMailAddress.clazz.addressForEmailString(ec, email);
				notification.setSourceAddressRelationship(address);

				notification.setStatus((String) recipientDict.valueForKey("status"));
				notification.setAction((String) recipientDict.valueForKey("action"));
				notification.setDiagnosticCode((String) recipientDict.valueForKey("diagnosticCode"));
				notification.setReportingMTA((String) json.valueForKeyPath("Message.bounce.reportingMTA"));
				String bounceTypeString = (String) json.valueForKeyPath("Message.bounce.bounceType");
				notification.setBounceType(SESBounceType.typeForString(bounceTypeString));
				notification.setBounceSubType((String) json.valueForKeyPath("Message.bounce.bounceSubType"));

				notification.setAwsFeedbackID((String) json.valueForKeyPath("Message.Message.bounce.feedbackId"));
				String messageId = (String) json.valueForKeyPath("Message.mail.messageId");
				notification.setAwsMessageID(messageId);
				ERCMailMessage message = ERCMailMessage.clazz.objectMatchingKeyAndValue(ec, ERCMailMessage.MESSAGE_ID_KEY, messageId);
				notification.setMailMessageRelationship(message);
				String dateString = (String) json.valueForKeyPath("Message.bounce.timestamp");
				Date date;
				try {
					date = format.parse(dateString);
				} catch (ParseException e) {
					throw NSForwardException._runtimeExceptionForThrowable(e);
				}
				notification.setNotificationTimestamp(new NSTimestamp(date));
				dateString = (String) json.valueForKeyPath("Message.mail.timestamp");
				try {
					date = format.parse(dateString);
				} catch (ParseException e) {
					throw NSForwardException._runtimeExceptionForThrowable(e);
				}
				notification.setMailTimestamp(new NSTimestamp(date));
				
				if(delegate() == null || !delegate().delegateHandledNotification(this, notification, address)) {
					address.setStopReason(reasonForType());
				}
			}
		}
	},
	COMPLAINT(ERCMailStopReason.COMPLAINT) {
		@Override
		public void createNotificationRecords(EOEditingContext ec, NSDictionary<String, Object> json) {
			NSArray<NSDictionary<String, Object>> recipients = 
					(NSArray<NSDictionary<String, Object>>) json.valueForKeyPath("Message.complaint.complainedRecipients");
			SimpleDateFormat format = new SimpleDateFormat(JSON_DATE_FORMAT_STRING);
			
			for(NSDictionary<String, Object> recipientDict : recipients) {
				SESComplaintNotification notification = SESComplaintNotification.clazz.createAndInsertObject(ec);
				String email = (String) recipientDict.valueForKey("emailAddress");
				ERCMailAddress address = ERCMailAddress.clazz.addressForEmailString(ec, email);
				notification.setMailAddressRelationship(address);
				email = (String) json.valueForKeyPath("Message.mail.source");
				address = ERCMailAddress.clazz.addressForEmailString(ec, email);
				notification.setSourceAddressRelationship(address);
				
				notification.setUserAgent((String) json.valueForKeyPath("Message.complaint.userAgent"));
				notification.setComplaintFeedbackType((String) json.valueForKeyPath("Message.complaint.complaintFeedbackType"));
				String dateString = (String) json.valueForKeyPath("Message.complaint.arrivalDate");
				Date date;
				try {
					date = format.parse(dateString);
				} catch (ParseException e) {
					throw NSForwardException._runtimeExceptionForThrowable(e);
				}
				notification.setArrivalDate(new NSTimestamp(date));
				
				notification.setAwsFeedbackID((String) json.valueForKeyPath("Message.complaint.feedbackId"));
				String messageId = (String) json.valueForKeyPath("Message.mail.messageId");
				notification.setAwsMessageID(messageId);
				ERCMailMessage message = ERCMailMessage.clazz.objectMatchingKeyAndValue(ec, ERCMailMessage.MESSAGE_ID_KEY, messageId);
				notification.setMailMessageRelationship(message);
				dateString = (String) json.valueForKeyPath("Message.complaint.timestamp");
				try {
					date = format.parse(dateString);
				} catch (ParseException e) {
					throw NSForwardException._runtimeExceptionForThrowable(e);
				}
				notification.setNotificationTimestamp(new NSTimestamp(date));
				dateString = (String) json.valueForKeyPath("Message.mail.timestamp");
				try {
					date = format.parse(dateString);
				} catch (ParseException e) {
					throw NSForwardException._runtimeExceptionForThrowable(e);
				}
				notification.setMailTimestamp(new NSTimestamp(date));

				if(delegate() == null || !delegate().delegateHandledNotification(this, notification, address)) {
					address.setStopReason(reasonForType());
				}
			}
		}
	};
	
	private static final String JSON_DATE_FORMAT_STRING = "yyyy-MM-ddThh:mm:ss.SSSZ";
	
	private final ERCMailStopReason _reason;
	private static Delegate _delegate;
	
	private SESNotificationType(ERCMailStopReason reason) {
		_reason = reason;
	}
	
	public ERCMailStopReason reasonForType() {
		return _reason;
	}

	public abstract void createNotificationRecords(EOEditingContext ec, NSDictionary<String, Object> json);

	public static Delegate delegate() {
		return _delegate;
	}

	public static void setDelegate(Delegate delegate) {
		_delegate = delegate;
	}

	public static interface Delegate {
		/**
		 * Provides a place to override default behavior.
		 * 
		 * @param type the notification type
		 * @param notification the notification
		 * @param address the email address
		 * @return true if the delegate handled the notification, false if the default behavior should be used.
		 */
		public boolean delegateHandledNotification(SESNotificationType type, SESNotification notification, ERCMailAddress address);
	}
	
}
