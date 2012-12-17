package er.awsplugin.actions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

import er.awsplugin.enums.SESNotificationType;
import er.corebl.mail.ERCMailState;
import er.corebl.mail.ERCMailer;
import er.corebl.model.ERCMailAddress;
import er.corebl.model.ERCMailMessage;
import er.extensions.appserver.ERXApplication;
import er.extensions.appserver.ERXDirectAction;
import er.extensions.eof.ERXEC;
import er.extensions.foundation.ERXProperties;
import er.extensions.foundation.ERXPropertyListSerialization;

public class SESAction extends ERXDirectAction {
	
	private static final int poolSize = 8;
	
	private static final ExecutorService service = Executors.newFixedThreadPool(poolSize);

	public SESAction(WORequest r) {
		super(r);
	}
	
	private static class NotificationProcessor {
		public void run(String json) {
			NSDictionary<String, Object> dict = ERXPropertyListSerialization.dictionaryForJSONString(json);
			String notificationType = (String) dict.objectForKey("notificationType");
			SESNotificationType type;
			try {
				type = SESNotificationType.valueOf(notificationType.toUpperCase());
			} catch (RuntimeException e) {
				reportException(e,json);
				return;
			}
			
			EOEditingContext ec = ERXEC.newEditingContext();
			ec.lock();
			try {
				type.createNotificationRecords(ec, dict);
				
				ec.saveChanges();
			} catch (Exception e) {
				reportException(e, json);
			} finally {
				ec.unlock();
				ec.dispose();
			}
		}
		
		public void reportException(Throwable th, String json) {
			ERXApplication app = ERXApplication.erxApplication();
			app.reportException(th, null, new NSDictionary<String, String>(json, "json string"));
		}
	}
	
	private static Runnable curry(
			final NotificationProcessor processor, 
			final String json) {
		return new Runnable() {
			@Override
			public void run() {
				processor.run(json);
			}
		};
	}
	
	public WOActionResults processNotificationAction() {
		WOResponse response = new WOResponse();
		try {
			String json = request().contentString();
			Runnable r = curry(new NotificationProcessor(), json);
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
	
	public WOActionResults testMessagesAction() {
		WOResponse response = new WOResponse();
		EOEditingContext ec = ERXEC.newEditingContext();
		try {
			String from = fromAddress();
			simpleMessage(ec, "success@simulator.amazonses.com", from, "Test Success", "Testing Success");
			simpleMessage(ec, "bounce@simulator.amazonses.com", from, "Test Bounce", "Testing Bounce");
			simpleMessage(ec, "ooto@simulator.amazonses.com", from, "Test OOTO", "Testing OOTO");
			simpleMessage(ec, "complaint@simulator.amazonses.com", from, "Test Complaint", "Testing Complaint");
			simpleMessage(ec, "blacklist@simulator.amazonses.com", from, "Test Blacklist", "Testing Blacklist");
			ec.saveChanges();
			ERCMailer.INSTANCE.processOutgoingMail();
			response.setStatus(200);
			response.setContent("OK");
		} catch (Exception e) {
			response.setStatus(500);
			response.setContent("Server Error");
			log.error("Error sending test messages", e);
		}
		return response;
	}
	
	private String fromAddress() {
		return ERXProperties.stringForKey("er.awsplugin.testMessages.senderAddress");
	}
	
	private ERCMailMessage simpleMessage(EOEditingContext ec, String to, String from, String subject, String message) {
		NSArray<ERCMailAddress> toAddresses = new NSArray<ERCMailAddress>(ERCMailAddress.clazz.addressForEmailString(ec, to));
		ERCMailAddress fromAddress = ERCMailAddress.clazz.addressForEmailString(ec, from);
		return ERCMailMessage.clazz.composeMailMessage(ec, ERCMailState.READY_TO_BE_SENT, fromAddress, fromAddress, toAddresses, null, null, subject, null, message, null, null);
	}
}
