// DO NOT EDIT.  Make changes to er.corebl.model.ERCMailOpen.java instead.
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
public abstract class _ERCMailOpen extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERCMailOpen";

  // Attributes
  public static final ERXKey<NSTimestamp> DATE_OPENED = new ERXKey<NSTimestamp>("dateOpened");
  public static final String DATE_OPENED_KEY = DATE_OPENED.key();

  // Relationships
  public static final ERXKey<er.corebl.model.ERCMailMessage> MAIL_MESSAGE = new ERXKey<er.corebl.model.ERCMailMessage>("mailMessage");
  public static final String MAIL_MESSAGE_KEY = MAIL_MESSAGE.key();
  public static final ERXKey<er.corebl.model.ERCUserAgent> USER_AGENT = new ERXKey<er.corebl.model.ERCUserAgent>("userAgent");
  public static final String USER_AGENT_KEY = USER_AGENT.key();

  public static class _ERCMailOpenClazz<T extends er.corebl.model.ERCMailOpen> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERCMailOpen.class);

  public er.corebl.model.ERCMailOpen.ERCMailOpenClazz clazz() {
    return er.corebl.model.ERCMailOpen.clazz;
  }
  
  public NSTimestamp dateOpened() {
    return (NSTimestamp) storedValueForKey(_ERCMailOpen.DATE_OPENED_KEY);
  }

  public void setDateOpened(NSTimestamp value) {
    if (_ERCMailOpen.LOG.isDebugEnabled()) {
    	_ERCMailOpen.LOG.debug( "updating dateOpened from " + dateOpened() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailOpen.DATE_OPENED_KEY);
  }

  public er.corebl.model.ERCMailMessage mailMessage() {
    return (er.corebl.model.ERCMailMessage)storedValueForKey(_ERCMailOpen.MAIL_MESSAGE_KEY);
  }
  
  public void setMailMessage(er.corebl.model.ERCMailMessage value) {
    takeStoredValueForKey(value, _ERCMailOpen.MAIL_MESSAGE_KEY);
  }

  public void setMailMessageRelationship(er.corebl.model.ERCMailMessage value) {
    if (_ERCMailOpen.LOG.isDebugEnabled()) {
      _ERCMailOpen.LOG.debug("updating mailMessage from " + mailMessage() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setMailMessage(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCMailMessage oldValue = mailMessage();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCMailOpen.MAIL_MESSAGE_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCMailOpen.MAIL_MESSAGE_KEY);
    }
  }
  public er.corebl.model.ERCUserAgent userAgent() {
    return (er.corebl.model.ERCUserAgent)storedValueForKey(_ERCMailOpen.USER_AGENT_KEY);
  }
  
  public void setUserAgent(er.corebl.model.ERCUserAgent value) {
    takeStoredValueForKey(value, _ERCMailOpen.USER_AGENT_KEY);
  }

  public void setUserAgentRelationship(er.corebl.model.ERCUserAgent value) {
    if (_ERCMailOpen.LOG.isDebugEnabled()) {
      _ERCMailOpen.LOG.debug("updating userAgent from " + userAgent() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setUserAgent(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCUserAgent oldValue = userAgent();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCMailOpen.USER_AGENT_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCMailOpen.USER_AGENT_KEY);
    }
  }

}
