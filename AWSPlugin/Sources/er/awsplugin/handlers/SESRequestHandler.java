package er.awsplugin.handlers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import javax.mail.Message.RecipientType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.Request;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.handlers.RequestHandler;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClient;
import com.amazonaws.services.simpleemail.model.GetSendQuotaRequest;
import com.amazonaws.services.simpleemail.model.GetSendQuotaResult;
import com.amazonaws.services.simpleemail.model.MessageRejectedException;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailResult;
import com.amazonaws.util.TimingInfo;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

import er.corebl.mail.ERCMailStopReason;
import er.corebl.mail.ERCMailer;
import er.corebl.model.ERCMailAddress;
import er.extensions.eof.ERXEC;
import er.extensions.foundation.ERXProperties;

public class SESRequestHandler implements RequestHandler {
	private static final Logger log = Logger.getLogger(SESRequestHandler.class);
	private static final String THROTTLING = "Throttling";
	
	static {
		updateSendQuotas();
	}
	
	private static Delegate _delegate;

	public static interface Delegate {
		public void afterError(Request<?> request, Exception exception);
		public void afterResponse(Request<?> request, Object obj, TimingInfo timinginfo);
		public void beforeRequest(Request<?> request);
	}
	
	public static Delegate delegate() {
		return _delegate;
	}

	public static void setDelegate(Delegate delegate) {
		_delegate = delegate;
	}

	@Override
	public void afterError(Request<?> request, Exception exception) {
		if(_delegate != null) {
			_delegate.afterError(request, exception);
		} else {
			if(exception instanceof MessageRejectedException && exception.getMessage().toLowerCase().contains("blacklist")) {
				SendRawEmailRequest raw = (SendRawEmailRequest) request.getOriginalRequest();
				RawMessage message = raw.getRawMessage();
				NSDictionary<String, String> headers = extractHeaders(message);
				NSArray<String> emails = rawAddressesFromHeaders(headers);
				if(emails.count() == 1) {
					EOEditingContext ec = ERXEC.newEditingContext();
					String email = emails.lastObject();
					ERCMailAddress address = ERCMailAddress.clazz.addressForEmailString(ec, email);
					address.setStopReason(ERCMailStopReason.BLACKLIST);
					try {
						ec.saveChanges();
						log.info("Address blacklisted: " + email);
					} catch (Exception e) {
						log.warn("Failed to save blacklisted email: " + email);
					}
				}
			} else if (exception instanceof AmazonServiceException) {
				AmazonServiceException ex = (AmazonServiceException) exception;
				if(THROTTLING.equals(ex.getErrorCode())) {
					log.error("Throttled", ex);
					SESRequestHandler.updateSendQuotas();
				}
			}
			log.debug("Request: " + request);
			log.debug("Error: " + exception.toString(), exception);
		}
	}
	
	private NSDictionary<String, String> extractHeaders(RawMessage message) {
		NSMutableDictionary<String, String> result = new NSMutableDictionary<String, String>();
		ByteBuffer buff = message.getData();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buff.array())));
		try {
			String nextLine = null;
			while((nextLine = reader.readLine()) != null) {
				if(StringUtils.isBlank(nextLine)) {
					break;
				}
				String key = StringUtils.substringBefore(nextLine, ": ");
				String value = StringUtils.substringAfter(nextLine, ": ");
				result.put(key, value);
			}
		} catch (IOException e) {
			try { reader.close(); } catch (IOException ex) { /* Do Nothing */ }
		}
		return result.immutableClone();
	}
	
	private NSArray<String> rawAddressesFromHeaders(NSDictionary<String, String> headers) {
		NSMutableArray<String> result = new NSMutableArray<String>();
		result.addObjectsFromArray(stringsForRecipientType(RecipientType.TO, headers));
		result.addObjectsFromArray(stringsForRecipientType(RecipientType.CC, headers));
		result.addObjectsFromArray(stringsForRecipientType(RecipientType.BCC, headers));
		return result.immutableClone();
	}
	
	private NSArray<String> stringsForRecipientType(RecipientType type, NSDictionary<String, String> headers) {
		NSArray<String> empty = NSArray.emptyArray();
		String tmp = headers.objectForKey(type.toString());
		return tmp == null?empty:NSArray.componentsSeparatedByString(tmp, ", ");
	}
	
	public static void updateSendQuotas() {
		String accessKey = ERXProperties.stringForKey("er.javamail.smtpUser");
		String secretKey = ERXProperties.stringForKey("er.javamail.smtpPassword");
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		AmazonSimpleEmailServiceAsync client = new AmazonSimpleEmailServiceAsyncClient(credentials);
		GetSendQuotaRequest request = new GetSendQuotaRequest();
		AsyncHandler<GetSendQuotaRequest, GetSendQuotaResult> handler = new AsyncHandler<GetSendQuotaRequest, GetSendQuotaResult>() {

			@Override
			public void onError(Exception exception) {
				/* Nothing to really do here */
				log.error("Error updating send rate", exception);
			}

			@Override
			public void onSuccess(GetSendQuotaRequest request, GetSendQuotaResult result) {
				int maxSendRate = result.getMaxSendRate().intValue();
				//Add one for rounding error. Close enough
				int sendRate = maxSendRate > 0?(1000/maxSendRate) + 1:Integer.MAX_VALUE;
				ERCMailer.INSTANCE.setSendRate(sendRate);
				if(result.getSentLast24Hours() >= result.getMax24HourSend()) {
					log.warn("24 hour quota exceeded. Stopping mailer.");
					ERCMailer.INSTANCE.stopMailer();
				}
			}
		};
		
		client.getSendQuotaAsync(request, handler);
	}

	/**
	 * Capture the message id result after successfully sending a message.
	 */
	@Override
	public void afterResponse(Request<?> request, Object obj, TimingInfo timinginfo) {
		if(_delegate != null) {
			_delegate.afterResponse(request, obj, timinginfo);
		} else {
			SendRawEmailResult result = (SendRawEmailResult) obj;
			String messageID = result.getMessageId();
			ERCMailer.INSTANCE.setMessageID(messageID);
		}
	}

	@Override
	public void beforeRequest(Request<?> request) {
		// Does nothing
		if(_delegate != null) {
			_delegate.beforeRequest(request);
		} else {
			log.debug("Before request");
		}
	}

}
