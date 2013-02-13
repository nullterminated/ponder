package er.corebl.mail;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOKeyGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import er.corebl.model.ERCMailAddress;
import er.corebl.model.ERCMailMessage;
import er.corebl.model.ERCMailRecipient;
import er.extensions.eof.ERXEC;
import er.javamail.ERMessage;
import er.javamail.ERMessage.Delegate;

public class ERCMessageDelegate implements Delegate {
	private static final Logger log = Logger.getLogger(ERCMessageDelegate.class);
	
	private final EOKeyGlobalID _gid;
	
	public ERCMessageDelegate(EOKeyGlobalID gid) {
		_gid = gid;
	}

	@Override
	public void deliverySucceeded(ERMessage message) {
		EOEditingContext ec = ERXEC.newEditingContext();
		ERCMailMessage mailMessage = (ERCMailMessage) ec.faultForGlobalID(_gid, ec);
		mailMessage.setState(ERCMailState.SENT);
		NSTimestamp now = new NSTimestamp();
		mailMessage.setDateSent(now);
		ERCMailMessage.MAIL_RECIPIENTS.dot(ERCMailRecipient.MAIL_ADDRESS).dot(ERCMailAddress.DATE_LAST_SENT).takeValueInObject(now, mailMessage);
		ec.saveChanges();
	}

	@Override
	public void invalidRecipients(ERMessage message, NSArray<String> invalidRecipientAddresses) {
		// TODO Flag or remove invalid addresses?
		log.debug("Invalid recipients: " + invalidRecipientAddresses);
	}

	@Override
	public void deliveryFailed(ERMessage message, Throwable failure) {
		EOEditingContext ec = ERXEC.newEditingContext();
		ERCMailMessage mailMessage = (ERCMailMessage) ec.faultForGlobalID(_gid, ec);
		mailMessage.setException(failure);
		ec.saveChanges();
	}

}
