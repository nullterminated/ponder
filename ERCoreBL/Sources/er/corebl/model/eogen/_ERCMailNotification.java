// DO NOT EDIT.  Make changes to er.corebl.model.ERCMailNotification.java instead.
package er.corebl.model.eogen;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;


@SuppressWarnings("all")
public abstract class _ERCMailNotification extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERCMailNotification";

  // Attributes
  public static final ERXKey<NSTimestamp> NOTIFICATION_TIMESTAMP = new ERXKey<NSTimestamp>("notificationTimestamp");
  public static final String NOTIFICATION_TIMESTAMP_KEY = NOTIFICATION_TIMESTAMP.key();
  public static final ERXKey<er.corebl.mail.ERCMailNotificationType> NOTIFICATION_TYPE = new ERXKey<er.corebl.mail.ERCMailNotificationType>("notificationType");
  public static final String NOTIFICATION_TYPE_KEY = NOTIFICATION_TYPE.key();

  // Relationships
  public static final ERXKey<er.corebl.model.ERCMailAddress> MAIL_ADDRESS = new ERXKey<er.corebl.model.ERCMailAddress>("mailAddress");
  public static final String MAIL_ADDRESS_KEY = MAIL_ADDRESS.key();
  public static final ERXKey<er.corebl.model.ERCMailMessage> MAIL_MESSAGE = new ERXKey<er.corebl.model.ERCMailMessage>("mailMessage");
  public static final String MAIL_MESSAGE_KEY = MAIL_MESSAGE.key();

  public static class _ERCMailNotificationClazz<T extends er.corebl.model.ERCMailNotification> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERCMailNotification.class);

  public er.corebl.model.ERCMailNotification.ERCMailNotificationClazz clazz() {
    return er.corebl.model.ERCMailNotification.clazz;
  }
  
  public NSTimestamp notificationTimestamp() {
    return (NSTimestamp) storedValueForKey(_ERCMailNotification.NOTIFICATION_TIMESTAMP_KEY);
  }

  public void setNotificationTimestamp(NSTimestamp value) {
    if (_ERCMailNotification.LOG.isDebugEnabled()) {
    	_ERCMailNotification.LOG.debug( "updating notificationTimestamp from " + notificationTimestamp() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailNotification.NOTIFICATION_TIMESTAMP_KEY);
  }

  public er.corebl.mail.ERCMailNotificationType notificationType() {
    return (er.corebl.mail.ERCMailNotificationType) storedValueForKey(_ERCMailNotification.NOTIFICATION_TYPE_KEY);
  }

  public void setNotificationType(er.corebl.mail.ERCMailNotificationType value) {
    if (_ERCMailNotification.LOG.isDebugEnabled()) {
    	_ERCMailNotification.LOG.debug( "updating notificationType from " + notificationType() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailNotification.NOTIFICATION_TYPE_KEY);
  }

  public er.corebl.model.ERCMailAddress mailAddress() {
    return (er.corebl.model.ERCMailAddress)storedValueForKey(_ERCMailNotification.MAIL_ADDRESS_KEY);
  }
  
  public void setMailAddress(er.corebl.model.ERCMailAddress value) {
    takeStoredValueForKey(value, _ERCMailNotification.MAIL_ADDRESS_KEY);
  }

  public void setMailAddressRelationship(er.corebl.model.ERCMailAddress value) {
    if (_ERCMailNotification.LOG.isDebugEnabled()) {
      _ERCMailNotification.LOG.debug("updating mailAddress from " + mailAddress() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setMailAddress(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCMailAddress oldValue = mailAddress();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCMailNotification.MAIL_ADDRESS_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCMailNotification.MAIL_ADDRESS_KEY);
    }
  }
  public er.corebl.model.ERCMailMessage mailMessage() {
    return (er.corebl.model.ERCMailMessage)storedValueForKey(_ERCMailNotification.MAIL_MESSAGE_KEY);
  }
  
  public void setMailMessage(er.corebl.model.ERCMailMessage value) {
    takeStoredValueForKey(value, _ERCMailNotification.MAIL_MESSAGE_KEY);
  }

  public void setMailMessageRelationship(er.corebl.model.ERCMailMessage value) {
    if (_ERCMailNotification.LOG.isDebugEnabled()) {
      _ERCMailNotification.LOG.debug("updating mailMessage from " + mailMessage() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setMailMessage(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCMailMessage oldValue = mailMessage();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCMailNotification.MAIL_MESSAGE_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCMailNotification.MAIL_MESSAGE_KEY);
    }
  }

}
