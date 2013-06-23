// DO NOT EDIT.  Make changes to er.corebl.model.ERCMailRecipient.java instead.
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
public abstract class _ERCMailRecipient extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERCMailRecipient";

  // Attributes
  public static final ERXKey<er.corebl.mail.ERCMailRecipientType> RECIPIENT_TYPE = new ERXKey<er.corebl.mail.ERCMailRecipientType>("recipientType");
  public static final String RECIPIENT_TYPE_KEY = RECIPIENT_TYPE.key();

  // Relationships
  public static final ERXKey<er.corebl.model.ERCMailAddress> MAIL_ADDRESS = new ERXKey<er.corebl.model.ERCMailAddress>("mailAddress");
  public static final String MAIL_ADDRESS_KEY = MAIL_ADDRESS.key();
  public static final ERXKey<er.corebl.model.ERCMailMessage> MAIL_MESSAGE = new ERXKey<er.corebl.model.ERCMailMessage>("mailMessage");
  public static final String MAIL_MESSAGE_KEY = MAIL_MESSAGE.key();

  public static class _ERCMailRecipientClazz<T extends er.corebl.model.ERCMailRecipient> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERCMailRecipient.class);

  public er.corebl.model.ERCMailRecipient.ERCMailRecipientClazz clazz() {
    return er.corebl.model.ERCMailRecipient.clazz;
  }

  public er.corebl.mail.ERCMailRecipientType recipientType() {
    return (er.corebl.mail.ERCMailRecipientType) storedValueForKey(_ERCMailRecipient.RECIPIENT_TYPE_KEY);
  }

  public void setRecipientType(er.corebl.mail.ERCMailRecipientType value) {
    if (_ERCMailRecipient.LOG.isDebugEnabled()) {
    	_ERCMailRecipient.LOG.debug( "updating recipientType from " + recipientType() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailRecipient.RECIPIENT_TYPE_KEY);
  }

  public er.corebl.model.ERCMailAddress mailAddress() {
    return (er.corebl.model.ERCMailAddress)storedValueForKey(_ERCMailRecipient.MAIL_ADDRESS_KEY);
  }

  public void setMailAddress(er.corebl.model.ERCMailAddress value) {
    takeStoredValueForKey(value, _ERCMailRecipient.MAIL_ADDRESS_KEY);
  }

  public void setMailAddressRelationship(er.corebl.model.ERCMailAddress value) {
    if (_ERCMailRecipient.LOG.isDebugEnabled()) {
      _ERCMailRecipient.LOG.debug("updating mailAddress from " + mailAddress() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setMailAddress(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCMailAddress oldValue = mailAddress();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCMailRecipient.MAIL_ADDRESS_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCMailRecipient.MAIL_ADDRESS_KEY);
    }
  }
  public er.corebl.model.ERCMailMessage mailMessage() {
    return (er.corebl.model.ERCMailMessage)storedValueForKey(_ERCMailRecipient.MAIL_MESSAGE_KEY);
  }

  public void setMailMessage(er.corebl.model.ERCMailMessage value) {
    takeStoredValueForKey(value, _ERCMailRecipient.MAIL_MESSAGE_KEY);
  }

  public void setMailMessageRelationship(er.corebl.model.ERCMailMessage value) {
    if (_ERCMailRecipient.LOG.isDebugEnabled()) {
      _ERCMailRecipient.LOG.debug("updating mailMessage from " + mailMessage() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setMailMessage(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCMailMessage oldValue = mailMessage();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCMailRecipient.MAIL_MESSAGE_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCMailRecipient.MAIL_MESSAGE_KEY);
    }
  }

}
