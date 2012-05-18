package er.auth;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSSelector;

import er.corebl.ERCoreBL;
import er.extensions.appserver.ERXSession;
import er.extensions.appserver.ERXWOContext;
import er.extensions.eof.ERXConstant;
import er.extensions.eof.ERXKey;

public enum ERStageManager {
	INSTANCE;
	
	public static final ERXKey<EOEnterpriseObject> USER = new ERXKey<EOEnterpriseObject>("user");
	
	static {
		NSNotificationCenter nc = NSNotificationCenter.defaultCenter();
		NSSelector<Void> wakeActor = new NSSelector<Void>("wakeActor", ERXConstant.NotificationClassArray);
		nc.addObserver(ERStageManager.INSTANCE, wakeActor, ERXSession.SessionWillAwakeNotification, null);
		NSSelector<Void> sleepActor = new NSSelector<Void>("sleepActor", ERXConstant.NotificationClassArray);
		nc.addObserver(ERStageManager.INSTANCE, sleepActor, ERXSession.SessionWillSleepNotification, null);
	}
	
	public void wakeActor(NSNotification n) {
		ERXSession session = (ERXSession)n.object();
		EOEnterpriseObject user = USER.valueInObject(session.objectStore());
		ERCoreBL.setActor(user);
	}
	
	public void sleepActor(NSNotification n) {
		ERCoreBL.setActor(null);
	}
	
	public void setActor(EOEnterpriseObject actor) {
		ERCoreBL.setActor(actor);
		ERXWOContext.currentContext().session();
		USER.takeValueInObject(actor, ERXSession.session().objectStore());
	}
}
