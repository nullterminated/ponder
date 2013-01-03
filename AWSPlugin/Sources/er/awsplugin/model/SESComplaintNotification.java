package er.awsplugin.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

public class SESComplaintNotification extends er.awsplugin.model.eogen._SESComplaintNotification {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(SESComplaintNotification.class);
	
	public static final String NOTIFICATION_TYPE_STRING = "COMPLAINT";

    public static final SESComplaintNotificationClazz<SESComplaintNotification> clazz = new SESComplaintNotificationClazz<SESComplaintNotification>();
    public static class SESComplaintNotificationClazz<T extends SESComplaintNotification> extends er.awsplugin.model.eogen._SESComplaintNotification._SESComplaintNotificationClazz<T> {
        /* more clazz methods here */
    }

    /**
     * Initializes the EO. This is called when an EO is created, not when it is 
     * inserted into an EC.
     */
    public void init(EOEditingContext ec) {
        super.init(ec);
    }
    
    public String typeString() {
    	return NOTIFICATION_TYPE_STRING;
    }
}
