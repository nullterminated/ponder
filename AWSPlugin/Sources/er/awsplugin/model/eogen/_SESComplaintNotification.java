// DO NOT EDIT.  Make changes to er.awsplugin.model.SESComplaintNotification.java instead.
package er.awsplugin.model.eogen;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;


@SuppressWarnings("all")
public abstract class _SESComplaintNotification extends er.awsplugin.model.SESNotification {
  public static final String ENTITY_NAME = "SESComplaintNotification";

  // Attributes
  public static final ERXKey<NSTimestamp> ARRIVAL_DATE = new ERXKey<NSTimestamp>("arrivalDate");
  public static final String ARRIVAL_DATE_KEY = ARRIVAL_DATE.key();
  public static final ERXKey<String> AWS_FEEDBACK_ID = new ERXKey<String>("awsFeedbackID");
  public static final String AWS_FEEDBACK_ID_KEY = AWS_FEEDBACK_ID.key();
  public static final ERXKey<String> AWS_MESSAGE_ID = new ERXKey<String>("awsMessageID");
  public static final String AWS_MESSAGE_ID_KEY = AWS_MESSAGE_ID.key();
  public static final ERXKey<String> COMPLAINT_FEEDBACK_TYPE = new ERXKey<String>("complaintFeedbackType");
  public static final String COMPLAINT_FEEDBACK_TYPE_KEY = COMPLAINT_FEEDBACK_TYPE.key();
  public static final ERXKey<NSTimestamp> MAIL_TIMESTAMP = new ERXKey<NSTimestamp>("mailTimestamp");
  public static final String MAIL_TIMESTAMP_KEY = MAIL_TIMESTAMP.key();
  public static final ERXKey<NSTimestamp> NOTIFICATION_TIMESTAMP = new ERXKey<NSTimestamp>("notificationTimestamp");
  public static final String NOTIFICATION_TIMESTAMP_KEY = NOTIFICATION_TIMESTAMP.key();
  public static final ERXKey<String> NOTIFICATION_TYPE = new ERXKey<String>("notificationType");
  public static final String NOTIFICATION_TYPE_KEY = NOTIFICATION_TYPE.key();
  public static final ERXKey<String> USER_AGENT = new ERXKey<String>("userAgent");
  public static final String USER_AGENT_KEY = USER_AGENT.key();

  // Relationships
  public static final ERXKey<er.corebl.model.ERCMailAddress> MAIL_ADDRESS = new ERXKey<er.corebl.model.ERCMailAddress>("mailAddress");
  public static final String MAIL_ADDRESS_KEY = MAIL_ADDRESS.key();
  public static final ERXKey<er.corebl.model.ERCMailMessage> MAIL_MESSAGE = new ERXKey<er.corebl.model.ERCMailMessage>("mailMessage");
  public static final String MAIL_MESSAGE_KEY = MAIL_MESSAGE.key();
  public static final ERXKey<er.corebl.model.ERCMailAddress> SOURCE_ADDRESS = new ERXKey<er.corebl.model.ERCMailAddress>("sourceAddress");
  public static final String SOURCE_ADDRESS_KEY = SOURCE_ADDRESS.key();

  public static class _SESComplaintNotificationClazz<T extends er.awsplugin.model.SESComplaintNotification> extends er.awsplugin.model.SESNotification.SESNotificationClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_SESComplaintNotification.class);

  public er.awsplugin.model.SESComplaintNotification.SESComplaintNotificationClazz clazz() {
    return er.awsplugin.model.SESComplaintNotification.clazz;
  }
  
  public NSTimestamp arrivalDate() {
    return (NSTimestamp) storedValueForKey(_SESComplaintNotification.ARRIVAL_DATE_KEY);
  }

  public void setArrivalDate(NSTimestamp value) {
    if (_SESComplaintNotification.LOG.isDebugEnabled()) {
    	_SESComplaintNotification.LOG.debug( "updating arrivalDate from " + arrivalDate() + " to " + value);
    }
    takeStoredValueForKey(value, _SESComplaintNotification.ARRIVAL_DATE_KEY);
  }

  public String complaintFeedbackType() {
    return (String) storedValueForKey(_SESComplaintNotification.COMPLAINT_FEEDBACK_TYPE_KEY);
  }

  public void setComplaintFeedbackType(String value) {
    if (_SESComplaintNotification.LOG.isDebugEnabled()) {
    	_SESComplaintNotification.LOG.debug( "updating complaintFeedbackType from " + complaintFeedbackType() + " to " + value);
    }
    takeStoredValueForKey(value, _SESComplaintNotification.COMPLAINT_FEEDBACK_TYPE_KEY);
  }

  public String userAgent() {
    return (String) storedValueForKey(_SESComplaintNotification.USER_AGENT_KEY);
  }

  public void setUserAgent(String value) {
    if (_SESComplaintNotification.LOG.isDebugEnabled()) {
    	_SESComplaintNotification.LOG.debug( "updating userAgent from " + userAgent() + " to " + value);
    }
    takeStoredValueForKey(value, _SESComplaintNotification.USER_AGENT_KEY);
  }


}
