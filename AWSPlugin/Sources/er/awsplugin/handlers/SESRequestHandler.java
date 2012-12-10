package er.awsplugin.handlers;

import org.apache.log4j.Logger;

import com.amazonaws.Request;
import com.amazonaws.handlers.RequestHandler;
import com.amazonaws.services.simpleemail.model.SendRawEmailResult;
import com.amazonaws.util.TimingInfo;

import er.corebl.mail.ERCMailer;

public class SESRequestHandler implements RequestHandler {
	private static final Logger log = Logger.getLogger(SESRequestHandler.class);

	@Override
	public void afterError(Request<?> request, Exception exception) {
		// Does nothing
		log.info("Error", exception);
	}

	/**
	 * Capture the message id result after successfully sending a message.
	 */
	@Override
	public void afterResponse(Request<?> request, Object obj, TimingInfo timinginfo) {
		SendRawEmailResult result = (SendRawEmailResult) obj;
		String messageID = result.getMessageId();
		ERCMailer.INSTANCE.setMessageID(messageID);
	}

	@Override
	public void beforeRequest(Request<?> request) {
		// Does nothing
		log.info("Before request");
	}

}
