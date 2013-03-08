package er.corebl.mail;

import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import er.corebl.model.ERCMailAddress;
import er.corebl.model.ERCMailMessage;
import er.corebl.model.ERCMailRecipient;

/**
 * A basic sender delegate class that only suppresses email based on the
 * mail address stop reason. If you only mail your customers, this is
 * probably sufficient. If you email on behalf of your customers, then
 * you will likely need different logic. The default ERCMailer delegate
 * uses this logic by default.
 */
public class SingleSenderDelegate {
	public boolean shouldSendMessage(ERCMailMessage message) {
		
		if(message.mailCategory() != null) {
			EOQualifier q = ERCMailRecipient.MAIL_ADDRESS.dot(ERCMailAddress.OPT_IN_CATEGORIES).containsObject(message.mailCategory());
			NSArray<ERCMailRecipient> optOut = message.mailRecipients(q);
			if(!optOut.isEmpty()) {
				changeState(message, ERCMailState.OPT_OUT);
				return false;
			}
		}
		
		EOQualifier q = ERCMailRecipient.MAIL_ADDRESS.dot(ERCMailAddress.STOP_REASON).isNotNull();
		NSArray<ERCMailRecipient> suppressed = message.mailRecipients(q);
		if(!suppressed.isEmpty()) {
			changeState(message, ERCMailState.SUPPRESSED);
		}
		
		return true;
	}
	
	private void changeState(ERCMailMessage message, ERCMailState state) {
		EOEditingContext ec = message.editingContext();
		try {
			message.setState(state);
			ec.saveChanges();
			return;
		} catch (EOGeneralAdaptorException ge) {
			ec.revert();
			return;
		}
	}
}
