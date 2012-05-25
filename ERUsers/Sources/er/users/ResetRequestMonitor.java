package er.users;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSSelector;

import er.auth.ERStageManager;
import er.extensions.appserver.ERXSession;
import er.extensions.eof.ERXEC;
import er.extensions.foundation.ERXSelectorUtilities;
import er.users.model.ERUser;

public enum ResetRequestMonitor {
	INSTANCE;

	private static final Logger log = Logger.getLogger(ResetRequestMonitor.class);

	static {
		NSNotificationCenter nc = NSNotificationCenter.defaultCenter();
		NSSelector<Void> sleepActor = ERXSelectorUtilities.notificationSelector("sleepActor");
		nc.addObserver(ResetRequestMonitor.INSTANCE, sleepActor, ERXSession.SessionWillSleepNotification, null);
	}

	/**
	 * This method checks to see if an active user has a resetRequestDate and
	 * resetToken. If so, it clears the values from the database.
	 * 
	 * @param n
	 *            the notification
	 */
	public void sleepActor(NSNotification n) {
		ERXSession session = (ERXSession) n.object();
		ERUser actor = (ERUser) ERStageManager.USER.valueInObject(session.objectStore());
		if (actor != null && actor.resetRequestDate() != null) {
			EOEditingContext ec = ERXEC.newEditingContext();
			ERUser user = (ERUser) actor.localInstanceIn(ec);
			user.setResetRequestDate(null);
			user.setResetToken(null);
			try {
				ec.saveChanges();
			} catch (Exception e) {
				log.error("Error clearing reset token and request date for user with id: " + user.primaryKey(), e);
			}
		}
	}
}
