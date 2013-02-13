package er.awsplugin.handlers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import javax.mail.Message.RecipientType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.amazonaws.Request;
import com.amazonaws.handlers.RequestHandler;
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

public class SESRequestHandler implements RequestHandler {
	private static final Logger log = Logger.getLogger(SESRequestHandler.class);
	
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
