package er.awsplugin.handlers;

import org.apache.log4j.Logger;

import com.amazonaws.Request;
import com.amazonaws.handlers.RequestHandler;
import com.amazonaws.services.simpleemail.model.SendRawEmailResult;
import com.amazonaws.util.TimingInfo;

import er.corebl.mail.ERCMailer;

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
		// Does nothing
		if(_delegate != null) {
			_delegate.afterError(request, exception);
		} else {
			log.info("Request: " + request);
			log.info("Error: " + exception.toString(), exception);
		}
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
			log.info("Before request");
		}
	}

}
