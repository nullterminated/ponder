package er.awsplugin.enums;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

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
					(NSArray<NSDictionary<String, Object>>) json.valueForKeyPath("bounce.bouncedRecipients");
			SimpleDateFormat format = new SimpleDateFormat(JSON_DATE_FORMAT_STRING);
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			
			for(NSDictionary<String, Object> recipientDict : recipients) {
				String awsFeedbackID= (String) json.valueForKeyPath("bounce.feedbackId");
				SESNotification existing = SESNotification.clazz.objectMatchingKeyAndValue(ec, SESNotification.AWS_FEEDBACK_ID_KEY, awsFeedbackID);
				if(existing != null) {
					continue;
				}
				SESBounceNotification notification = SESBounceNotification.clazz.createAndInsertObject(ec);
				String email = (String) recipientDict.valueForKey("emailAddress");
				ERCMailAddress recipientAddress = ERCMailAddress.clazz.addressForEmailString(ec, email);
				notification.setMailAddressRelationship(recipientAddress);
				email = (String) json.valueForKeyPath("mail.source");
				ERCMailAddress sourceAddress = ERCMailAddress.clazz.addressForEmailString(ec, email);
				notification.setSourceAddressRelationship(sourceAddress);

				notification.setStatus((String) recipientDict.valueForKey("status"));
				notification.setAction((String) recipientDict.valueForKey("action"));
				String diagnosticCode = (String) recipientDict.valueForKey("diagnosticCode");
				diagnosticCode = StringUtils.left(diagnosticCode, 1000);
				notification.setDiagnosticCode(diagnosticCode);
				notification.setReportingMTA((String) json.valueForKeyPath("bounce.reportingMTA"));
				String bounceTypeString = (String) json.valueForKeyPath("bounce.bounceType");
				notification.setBounceType(SESBounceType.typeForString(bounceTypeString));
				notification.setBounceSubType((String) json.valueForKeyPath("bounce.bounceSubType"));

				notification.setAwsFeedbackID(awsFeedbackID);
				String messageId = (String) json.valueForKeyPath("mail.messageId");
				notification.setAwsMessageID(messageId);
				ERCMailMessage message = ERCMailMessage.clazz.objectMatchingKeyAndValue(ec, ERCMailMessage.MESSAGE_ID_KEY, messageId);
				notification.setMailMessageRelationship(message);
				String dateString = (String) json.valueForKeyPath("bounce.timestamp");
				Date date;
				try {
					date = format.parse(dateString);
				} catch (ParseException e) {
					throw NSForwardException._runtimeExceptionForThrowable(e);
				}
				notification.setNotificationTimestamp(new NSTimestamp(date));
				dateString = (String) json.valueForKeyPath("mail.timestamp");
				try {
					date = format.parse(dateString);
				} catch (ParseException e) {
					throw NSForwardException._runtimeExceptionForThrowable(e);
				}
				notification.setMailTimestamp(new NSTimestamp(date));
				
				if(delegate() == null || !delegate().delegateHandledNotification(this, notification, recipientAddress)) {
					recipientAddress.setStopReason(reasonForType());
				}
			}
		}
	},
	COMPLAINT(ERCMailStopReason.COMPLAINT) {
		@Override
		public void createNotificationRecords(EOEditingContext ec, NSDictionary<String, Object> json) {
			NSArray<NSDictionary<String, Object>> recipients = 
					(NSArray<NSDictionary<String, Object>>) json.valueForKeyPath("complaint.complainedRecipients");
			SimpleDateFormat format = new SimpleDateFormat(JSON_DATE_FORMAT_STRING);
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			
			for(NSDictionary<String, Object> recipientDict : recipients) {
				SESComplaintNotification notification = SESComplaintNotification.clazz.createAndInsertObject(ec);
				String email = (String) recipientDict.valueForKey("emailAddress");
				ERCMailAddress recipientAddress = ERCMailAddress.clazz.addressForEmailString(ec, email);
				notification.setMailAddressRelationship(recipientAddress);
				email = (String) json.valueForKeyPath("mail.source");
				ERCMailAddress sourceAddress = ERCMailAddress.clazz.addressForEmailString(ec, email);
				notification.setSourceAddressRelationship(sourceAddress);
				
				notification.setUserAgent((String) json.valueForKeyPath("complaint.userAgent"));
				notification.setComplaintFeedbackType((String) json.valueForKeyPath("complaint.complaintFeedbackType"));
				String dateString = (String) json.valueForKeyPath("complaint.arrivalDate");
				Date date;
				if(dateString != null) {
					try {
						date = format.parse(dateString);
					} catch (ParseException e) {
						throw NSForwardException._runtimeExceptionForThrowable(e);
					}
					notification.setArrivalDate(new NSTimestamp(date));
				}
				
				notification.setAwsFeedbackID((String) json.valueForKeyPath("complaint.feedbackId"));
				String messageId = (String) json.valueForKeyPath("mail.messageId");
				notification.setAwsMessageID(messageId);
				ERCMailMessage message = ERCMailMessage.clazz.objectMatchingKeyAndValue(ec, ERCMailMessage.MESSAGE_ID_KEY, messageId);
				notification.setMailMessageRelationship(message);
				dateString = (String) json.valueForKeyPath("complaint.timestamp");
				try {
					date = format.parse(dateString);
				} catch (ParseException e) {
					throw NSForwardException._runtimeExceptionForThrowable(e);
				}
				notification.setNotificationTimestamp(new NSTimestamp(date));
				dateString = (String) json.valueForKeyPath("mail.timestamp");
				try {
					date = format.parse(dateString);
				} catch (ParseException e) {
					throw NSForwardException._runtimeExceptionForThrowable(e);
				}
				notification.setMailTimestamp(new NSTimestamp(date));

				if(delegate() == null || !delegate().delegateHandledNotification(this, notification, recipientAddress)) {
					recipientAddress.setStopReason(reasonForType());
				}
			}
		}
	};
	
	private static final String JSON_DATE_FORMAT_STRING = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";
	
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
		 * @param address the recipient email address
		 * @return true if the delegate handled the notification, false if the default behavior should be used.
		 */
		public boolean delegateHandledNotification(SESNotificationType type, SESNotification notification, ERCMailAddress address);
	}
	
}
