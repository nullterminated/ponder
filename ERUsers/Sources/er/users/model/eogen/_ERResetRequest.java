// DO NOT EDIT.  Make changes to er.users.model.ERResetRequest.java instead.
package er.users.model.eogen;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;


@SuppressWarnings("all")
public abstract class _ERResetRequest extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERResetRequest";

  // Attributes
  public static final ERXKey<String> INET_ADDRESS = new ERXKey<String>("inetAddress");
  public static final String INET_ADDRESS_KEY = INET_ADDRESS.key();
  public static final ERXKey<org.joda.time.DateTime> REQUEST_DATE = new ERXKey<org.joda.time.DateTime>("requestDate");
  public static final String REQUEST_DATE_KEY = REQUEST_DATE.key();
  public static final ERXKey<String> UUID = new ERXKey<String>("uuid");
  public static final String UUID_KEY = UUID.key();

  // Relationships
  public static final ERXKey<er.users.model.ERUser> USER = new ERXKey<er.users.model.ERUser>("user");
  public static final String USER_KEY = USER.key();

  public static class _ERResetRequestClazz<T extends er.users.model.ERResetRequest> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERResetRequest.class);

  public er.users.model.ERResetRequest.ERResetRequestClazz clazz() {
    return er.users.model.ERResetRequest.clazz;
  }
  
  public String inetAddress() {
    return (String) storedValueForKey(_ERResetRequest.INET_ADDRESS_KEY);
  }

  public void setInetAddress(String value) {
    if (_ERResetRequest.LOG.isDebugEnabled()) {
    	_ERResetRequest.LOG.debug( "updating inetAddress from " + inetAddress() + " to " + value);
    }
    takeStoredValueForKey(value, _ERResetRequest.INET_ADDRESS_KEY);
  }

  public org.joda.time.DateTime requestDate() {
    return (org.joda.time.DateTime) storedValueForKey(_ERResetRequest.REQUEST_DATE_KEY);
  }

  public void setRequestDate(org.joda.time.DateTime value) {
    if (_ERResetRequest.LOG.isDebugEnabled()) {
    	_ERResetRequest.LOG.debug( "updating requestDate from " + requestDate() + " to " + value);
    }
    takeStoredValueForKey(value, _ERResetRequest.REQUEST_DATE_KEY);
  }

  public String uuid() {
    return (String) storedValueForKey(_ERResetRequest.UUID_KEY);
  }

  public void setUuid(String value) {
    if (_ERResetRequest.LOG.isDebugEnabled()) {
    	_ERResetRequest.LOG.debug( "updating uuid from " + uuid() + " to " + value);
    }
    takeStoredValueForKey(value, _ERResetRequest.UUID_KEY);
  }

  public er.users.model.ERUser user() {
    return (er.users.model.ERUser)storedValueForKey(_ERResetRequest.USER_KEY);
  }
  
  public void setUser(er.users.model.ERUser value) {
    takeStoredValueForKey(value, _ERResetRequest.USER_KEY);
  }

  public void setUserRelationship(er.users.model.ERUser value) {
    if (_ERResetRequest.LOG.isDebugEnabled()) {
      _ERResetRequest.LOG.debug("updating user from " + user() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setUser(value);
    }
    else if (value == null) {
    	er.users.model.ERUser oldValue = user();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERResetRequest.USER_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERResetRequest.USER_KEY);
    }
  }

}
