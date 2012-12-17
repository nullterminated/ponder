// DO NOT EDIT.  Make changes to er.awsplugin.model.SESNotification.java instead.
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
public abstract class _SESNotification extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "SESNotification";

  // Attributes
  public static final ERXKey<String> AWS_FEEDBACK_ID = new ERXKey<String>("awsFeedbackID");
  public static final String AWS_FEEDBACK_ID_KEY = AWS_FEEDBACK_ID.key();
  public static final ERXKey<String> AWS_MESSAGE_ID = new ERXKey<String>("awsMessageID");
  public static final String AWS_MESSAGE_ID_KEY = AWS_MESSAGE_ID.key();
  public static final ERXKey<NSTimestamp> MAIL_TIMESTAMP = new ERXKey<NSTimestamp>("mailTimestamp");
  public static final String MAIL_TIMESTAMP_KEY = MAIL_TIMESTAMP.key();
  public static final ERXKey<NSTimestamp> NOTIFICATION_TIMESTAMP = new ERXKey<NSTimestamp>("notificationTimestamp");
  public static final String NOTIFICATION_TIMESTAMP_KEY = NOTIFICATION_TIMESTAMP.key();
  public static final ERXKey<String> NOTIFICATION_TYPE = new ERXKey<String>("notificationType");
  public static final String NOTIFICATION_TYPE_KEY = NOTIFICATION_TYPE.key();

  // Relationships
  public static final ERXKey<er.corebl.model.ERCMailAddress> MAIL_ADDRESS = new ERXKey<er.corebl.model.ERCMailAddress>("mailAddress");
  public static final String MAIL_ADDRESS_KEY = MAIL_ADDRESS.key();
  public static final ERXKey<er.corebl.model.ERCMailMessage> MAIL_MESSAGE = new ERXKey<er.corebl.model.ERCMailMessage>("mailMessage");
  public static final String MAIL_MESSAGE_KEY = MAIL_MESSAGE.key();
  public static final ERXKey<er.corebl.model.ERCMailAddress> SOURCE_ADDRESS = new ERXKey<er.corebl.model.ERCMailAddress>("sourceAddress");
  public static final String SOURCE_ADDRESS_KEY = SOURCE_ADDRESS.key();

  public static class _SESNotificationClazz<T extends er.awsplugin.model.SESNotification> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_SESNotification.class);

  public er.awsplugin.model.SESNotification.SESNotificationClazz clazz() {
    return er.awsplugin.model.SESNotification.clazz;
  }
  
  public String awsFeedbackID() {
    return (String) storedValueForKey(_SESNotification.AWS_FEEDBACK_ID_KEY);
  }

  public void setAwsFeedbackID(String value) {
    if (_SESNotification.LOG.isDebugEnabled()) {
    	_SESNotification.LOG.debug( "updating awsFeedbackID from " + awsFeedbackID() + " to " + value);
    }
    takeStoredValueForKey(value, _SESNotification.AWS_FEEDBACK_ID_KEY);
  }

  public String awsMessageID() {
    return (String) storedValueForKey(_SESNotification.AWS_MESSAGE_ID_KEY);
  }

  public void setAwsMessageID(String value) {
    if (_SESNotification.LOG.isDebugEnabled()) {
    	_SESNotification.LOG.debug( "updating awsMessageID from " + awsMessageID() + " to " + value);
    }
    takeStoredValueForKey(value, _SESNotification.AWS_MESSAGE_ID_KEY);
  }

  public NSTimestamp mailTimestamp() {
    return (NSTimestamp) storedValueForKey(_SESNotification.MAIL_TIMESTAMP_KEY);
  }

  public void setMailTimestamp(NSTimestamp value) {
    if (_SESNotification.LOG.isDebugEnabled()) {
    	_SESNotification.LOG.debug( "updating mailTimestamp from " + mailTimestamp() + " to " + value);
    }
    takeStoredValueForKey(value, _SESNotification.MAIL_TIMESTAMP_KEY);
  }

  public NSTimestamp notificationTimestamp() {
    return (NSTimestamp) storedValueForKey(_SESNotification.NOTIFICATION_TIMESTAMP_KEY);
  }

  public void setNotificationTimestamp(NSTimestamp value) {
    if (_SESNotification.LOG.isDebugEnabled()) {
    	_SESNotification.LOG.debug( "updating notificationTimestamp from " + notificationTimestamp() + " to " + value);
    }
    takeStoredValueForKey(value, _SESNotification.NOTIFICATION_TIMESTAMP_KEY);
  }

  public String notificationType() {
    return (String) storedValueForKey(_SESNotification.NOTIFICATION_TYPE_KEY);
  }

  public void setNotificationType(String value) {
    if (_SESNotification.LOG.isDebugEnabled()) {
    	_SESNotification.LOG.debug( "updating notificationType from " + notificationType() + " to " + value);
    }
    takeStoredValueForKey(value, _SESNotification.NOTIFICATION_TYPE_KEY);
  }

  public er.corebl.model.ERCMailAddress mailAddress() {
    return (er.corebl.model.ERCMailAddress)storedValueForKey(_SESNotification.MAIL_ADDRESS_KEY);
  }
  
  public void setMailAddress(er.corebl.model.ERCMailAddress value) {
    takeStoredValueForKey(value, _SESNotification.MAIL_ADDRESS_KEY);
  }

  public void setMailAddressRelationship(er.corebl.model.ERCMailAddress value) {
    if (_SESNotification.LOG.isDebugEnabled()) {
      _SESNotification.LOG.debug("updating mailAddress from " + mailAddress() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setMailAddress(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCMailAddress oldValue = mailAddress();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _SESNotification.MAIL_ADDRESS_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _SESNotification.MAIL_ADDRESS_KEY);
    }
  }
  public er.corebl.model.ERCMailMessage mailMessage() {
    return (er.corebl.model.ERCMailMessage)storedValueForKey(_SESNotification.MAIL_MESSAGE_KEY);
  }
  
  public void setMailMessage(er.corebl.model.ERCMailMessage value) {
    takeStoredValueForKey(value, _SESNotification.MAIL_MESSAGE_KEY);
  }

  public void setMailMessageRelationship(er.corebl.model.ERCMailMessage value) {
    if (_SESNotification.LOG.isDebugEnabled()) {
      _SESNotification.LOG.debug("updating mailMessage from " + mailMessage() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setMailMessage(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCMailMessage oldValue = mailMessage();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _SESNotification.MAIL_MESSAGE_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _SESNotification.MAIL_MESSAGE_KEY);
    }
  }
  public er.corebl.model.ERCMailAddress sourceAddress() {
    return (er.corebl.model.ERCMailAddress)storedValueForKey(_SESNotification.SOURCE_ADDRESS_KEY);
  }
  
  public void setSourceAddress(er.corebl.model.ERCMailAddress value) {
    takeStoredValueForKey(value, _SESNotification.SOURCE_ADDRESS_KEY);
  }

  public void setSourceAddressRelationship(er.corebl.model.ERCMailAddress value) {
    if (_SESNotification.LOG.isDebugEnabled()) {
      _SESNotification.LOG.debug("updating sourceAddress from " + sourceAddress() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setSourceAddress(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCMailAddress oldValue = sourceAddress();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _SESNotification.SOURCE_ADDRESS_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _SESNotification.SOURCE_ADDRESS_KEY);
    }
  }

}
