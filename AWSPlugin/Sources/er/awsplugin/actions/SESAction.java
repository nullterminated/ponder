package er.awsplugin.actions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

import er.corebl.mail.ERCMailNotificationType;
import er.corebl.model.ERCMailAddress;
import er.corebl.model.ERCMailMessage;
import er.corebl.model.ERCMailNotification;
import er.extensions.appserver.ERXApplication;
import er.extensions.appserver.ERXDirectAction;
import er.extensions.eof.ERXEC;
import er.extensions.foundation.ERXPropertyListSerialization;

public class SESAction extends ERXDirectAction {
	
	private static final String format = "yyyy-MM-ddThh:mm:ss.SSSZ";
	
	private static final int poolSize = 8;
	
	private static final ExecutorService service = Executors.newFixedThreadPool(poolSize);

	public SESAction(WORequest r) {
		super(r);
	}
	
	private static class NotificationProcessor {
		public void run(String messageID, NSArray<String> emails, String timestamp, ERCMailNotificationType type) {
			EOEditingContext ec = ERXEC.newEditingContext();
			Date date;
			try {
				date = new SimpleDateFormat(format).parse(timestamp);
			} catch (ParseException e) {
				reportException(e, "Failed to parse timestamp", messageID, emails, timestamp, type);
				return;
			}
			NSTimestamp ts = new NSTimestamp(date);
			
			ERCMailMessage message = ERCMailMessage.clazz.objectMatchingKeyAndValue(ec, ERCMailMessage.MESSAGE_ID_KEY, messageID);
			
			for(String email: emails) {
				ERCMailAddress address = ERCMailAddress.clazz.addressForEmailString(ec, email);
				address.setStopReason(type.stopReason());
				if(message != null) {
					ERCMailNotification notification = ERCMailNotification.clazz.createAndInsertObject(ec);
					notification.setMailAddress(address);
					notification.setMailMessage(message);
					notification.setNotificationTimestamp(ts);
					notification.setNotificationType(type);
				}
				
				try {
					ec.saveChanges();
				} catch (EOGeneralAdaptorException e) {
					reportException(e, "Failed to save notification for address: " + email, messageID, emails, timestamp, type);
				}
			}
			ec.dispose();
		}
		
		public void reportException(Throwable t, String exceptionMessage, String messageID, NSArray<String> emails, String timestamp, ERCMailNotificationType type) {
			NSMutableDictionary<String, Object> extraInfo = new NSMutableDictionary<String, Object>();
			extraInfo.setObjectForKey(exceptionMessage, "Notification exception");
			extraInfo.setObjectForKey(type, "Notification type");
			extraInfo.setObjectForKey(messageID, "message id");
			extraInfo.setObjectForKey(emails, "email addresses");
			extraInfo.setObjectForKey(timestamp, "timestamp");
			ERXApplication.erxApplication().reportException(t, null, extraInfo);
		}
	}
	
	private static Runnable curry(
			final NotificationProcessor processor, 
			final String messageID, 
			final NSArray<String> emails, 
			final String timestamp,
			final ERCMailNotificationType type) {
		return new Runnable() {
			@Override
			public void run() {
				processor.run(messageID, emails, timestamp, type);
			}
		};
	}
	
	public WOActionResults bounceNotificationAction() {
		WOResponse response = new WOResponse();
		try {
			String json = request().contentString();
			NSDictionary dict = (NSDictionary) ERXPropertyListSerialization.propertyListFromJSONString(json);
			String messageID = (String) dict.valueForKeyPath("mail.messageId");
			NSArray<String> emails = (NSArray<String>) dict.valueForKeyPath("bounce.bouncedRecipients.emailAddress");
			String timestamp = (String) dict.valueForKeyPath("bounce.timestamp");
			Runnable r = curry(new NotificationProcessor(), messageID, emails, timestamp, ERCMailNotificationType.BOUNCE);
			service.execute(r);
		} catch (Exception e) {
			log.warn("Exception processing bounce notification: " + request(), e);
			response.setStatus(500);
			response.setContent("Server Error");
			return response;
		}
		response.setStatus(200);
		response.setContent("OK");
		return response;
		
	}

	public WOActionResults complaintNotificationAction() {
		WOResponse response = new WOResponse();
		try {
			String json = request().contentString();
			NSDictionary dict = (NSDictionary) ERXPropertyListSerialization.propertyListFromJSONString(json);
			String messageID = (String) dict.valueForKeyPath("mail.messageId");
			NSArray<String> emails = (NSArray<String>) dict.valueForKeyPath("complaint.complainedRecipients.emailAddress");
			String timestamp = (String) dict.valueForKeyPath("complaint.timestamp");
			Runnable r = curry(new NotificationProcessor(), messageID, emails, timestamp, ERCMailNotificationType.COMPLAINT);
			service.execute(r);
		} catch (Exception e) {
			log.warn("Exception processing complaint notification: " + request(), e);
			response.setStatus(500);
			response.setContent("Server Error");
			return response;
		}
		response.setStatus(200);
		response.setContent("OK");
		return response;
	}
}
