// DO NOT EDIT.  Make changes to er.awsplugin.model.SESBounceNotification.java instead.
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
public abstract class _SESBounceNotification extends er.awsplugin.model.SESNotification {
  public static final String ENTITY_NAME = "SESBounceNotification";

  // Attributes
  public static final ERXKey<String> ACTION = new ERXKey<String>("action");
  public static final String ACTION_KEY = ACTION.key();
  public static final ERXKey<String> AWS_FEEDBACK_ID = new ERXKey<String>("awsFeedbackID");
  public static final String AWS_FEEDBACK_ID_KEY = AWS_FEEDBACK_ID.key();
  public static final ERXKey<String> AWS_MESSAGE_ID = new ERXKey<String>("awsMessageID");
  public static final String AWS_MESSAGE_ID_KEY = AWS_MESSAGE_ID.key();
  public static final ERXKey<String> BOUNCE_SUB_TYPE = new ERXKey<String>("bounceSubType");
  public static final String BOUNCE_SUB_TYPE_KEY = BOUNCE_SUB_TYPE.key();
  public static final ERXKey<String> BOUNCE_TYPE = new ERXKey<String>("bounceType");
  public static final String BOUNCE_TYPE_KEY = BOUNCE_TYPE.key();
  public static final ERXKey<String> DIAGNOSTIC_CODE = new ERXKey<String>("diagnosticCode");
  public static final String DIAGNOSTIC_CODE_KEY = DIAGNOSTIC_CODE.key();
  public static final ERXKey<NSTimestamp> MAIL_TIMESTAMP = new ERXKey<NSTimestamp>("mailTimestamp");
  public static final String MAIL_TIMESTAMP_KEY = MAIL_TIMESTAMP.key();
  public static final ERXKey<NSTimestamp> NOTIFICATION_TIMESTAMP = new ERXKey<NSTimestamp>("notificationTimestamp");
  public static final String NOTIFICATION_TIMESTAMP_KEY = NOTIFICATION_TIMESTAMP.key();
  public static final ERXKey<String> NOTIFICATION_TYPE = new ERXKey<String>("notificationType");
  public static final String NOTIFICATION_TYPE_KEY = NOTIFICATION_TYPE.key();
  public static final ERXKey<String> REPORTING_MTA = new ERXKey<String>("reportingMTA");
  public static final String REPORTING_MTA_KEY = REPORTING_MTA.key();
  public static final ERXKey<String> STATUS = new ERXKey<String>("status");
  public static final String STATUS_KEY = STATUS.key();

  // Relationships
  public static final ERXKey<er.corebl.model.ERCMailAddress> MAIL_ADDRESS = new ERXKey<er.corebl.model.ERCMailAddress>("mailAddress");
  public static final String MAIL_ADDRESS_KEY = MAIL_ADDRESS.key();
  public static final ERXKey<er.corebl.model.ERCMailMessage> MAIL_MESSAGE = new ERXKey<er.corebl.model.ERCMailMessage>("mailMessage");
  public static final String MAIL_MESSAGE_KEY = MAIL_MESSAGE.key();
  public static final ERXKey<er.corebl.model.ERCMailAddress> SOURCE_ADDRESS = new ERXKey<er.corebl.model.ERCMailAddress>("sourceAddress");
  public static final String SOURCE_ADDRESS_KEY = SOURCE_ADDRESS.key();

  public static class _SESBounceNotificationClazz<T extends er.awsplugin.model.SESBounceNotification> extends er.awsplugin.model.SESNotification.SESNotificationClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_SESBounceNotification.class);

  public er.awsplugin.model.SESBounceNotification.SESBounceNotificationClazz clazz() {
    return er.awsplugin.model.SESBounceNotification.clazz;
  }
  
  public String action() {
    return (String) storedValueForKey(_SESBounceNotification.ACTION_KEY);
  }

  public void setAction(String value) {
    if (_SESBounceNotification.LOG.isDebugEnabled()) {
    	_SESBounceNotification.LOG.debug( "updating action from " + action() + " to " + value);
    }
    takeStoredValueForKey(value, _SESBounceNotification.ACTION_KEY);
  }

  public String bounceSubType() {
    return (String) storedValueForKey(_SESBounceNotification.BOUNCE_SUB_TYPE_KEY);
  }

  public void setBounceSubType(String value) {
    if (_SESBounceNotification.LOG.isDebugEnabled()) {
    	_SESBounceNotification.LOG.debug( "updating bounceSubType from " + bounceSubType() + " to " + value);
    }
    takeStoredValueForKey(value, _SESBounceNotification.BOUNCE_SUB_TYPE_KEY);
  }

  public String bounceType() {
    return (String) storedValueForKey(_SESBounceNotification.BOUNCE_TYPE_KEY);
  }

  public void setBounceType(String value) {
    if (_SESBounceNotification.LOG.isDebugEnabled()) {
    	_SESBounceNotification.LOG.debug( "updating bounceType from " + bounceType() + " to " + value);
    }
    takeStoredValueForKey(value, _SESBounceNotification.BOUNCE_TYPE_KEY);
  }

  public String diagnosticCode() {
    return (String) storedValueForKey(_SESBounceNotification.DIAGNOSTIC_CODE_KEY);
  }

  public void setDiagnosticCode(String value) {
    if (_SESBounceNotification.LOG.isDebugEnabled()) {
    	_SESBounceNotification.LOG.debug( "updating diagnosticCode from " + diagnosticCode() + " to " + value);
    }
    takeStoredValueForKey(value, _SESBounceNotification.DIAGNOSTIC_CODE_KEY);
  }

  public String reportingMTA() {
    return (String) storedValueForKey(_SESBounceNotification.REPORTING_MTA_KEY);
  }

  public void setReportingMTA(String value) {
    if (_SESBounceNotification.LOG.isDebugEnabled()) {
    	_SESBounceNotification.LOG.debug( "updating reportingMTA from " + reportingMTA() + " to " + value);
    }
    takeStoredValueForKey(value, _SESBounceNotification.REPORTING_MTA_KEY);
  }

  public String status() {
    return (String) storedValueForKey(_SESBounceNotification.STATUS_KEY);
  }

  public void setStatus(String value) {
    if (_SESBounceNotification.LOG.isDebugEnabled()) {
    	_SESBounceNotification.LOG.debug( "updating status from " + status() + " to " + value);
    }
    takeStoredValueForKey(value, _SESBounceNotification.STATUS_KEY);
  }


}
