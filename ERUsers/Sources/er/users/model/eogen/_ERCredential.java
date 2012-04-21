// DO NOT EDIT.  Make changes to er.users.model.ERCredential.java instead.
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
public abstract class _ERCredential extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERCredential";

  // Attributes
  public static final ERXKey<org.joda.time.DateTime> DATE_CREATED = new ERXKey<org.joda.time.DateTime>("dateCreated");
  public static final String DATE_CREATED_KEY = DATE_CREATED.key();
  public static final ERXKey<String> PASSWORD = new ERXKey<String>("password");
  public static final String PASSWORD_KEY = PASSWORD.key();
  public static final ERXKey<String> SALT = new ERXKey<String>("salt");
  public static final String SALT_KEY = SALT.key();

  // Relationships
  public static final ERXKey<er.users.model.ERUser> USER = new ERXKey<er.users.model.ERUser>("user");
  public static final String USER_KEY = USER.key();

  public static class _ERCredentialClazz<T extends er.users.model.ERCredential> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERCredential.class);

  public er.users.model.ERCredential.ERCredentialClazz clazz() {
    return er.users.model.ERCredential.clazz;
  }
  
  public org.joda.time.DateTime dateCreated() {
    return (org.joda.time.DateTime) storedValueForKey(_ERCredential.DATE_CREATED_KEY);
  }

  public void setDateCreated(org.joda.time.DateTime value) {
    if (_ERCredential.LOG.isDebugEnabled()) {
    	_ERCredential.LOG.debug( "updating dateCreated from " + dateCreated() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCredential.DATE_CREATED_KEY);
  }

  public String password() {
    return (String) storedValueForKey(_ERCredential.PASSWORD_KEY);
  }

  public void setPassword(String value) {
    if (_ERCredential.LOG.isDebugEnabled()) {
    	_ERCredential.LOG.debug( "updating password from " + password() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCredential.PASSWORD_KEY);
  }

  public String salt() {
    return (String) storedValueForKey(_ERCredential.SALT_KEY);
  }

  public void setSalt(String value) {
    if (_ERCredential.LOG.isDebugEnabled()) {
    	_ERCredential.LOG.debug( "updating salt from " + salt() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCredential.SALT_KEY);
  }

  public er.users.model.ERUser user() {
    return (er.users.model.ERUser)storedValueForKey(_ERCredential.USER_KEY);
  }
  
  public void setUser(er.users.model.ERUser value) {
    takeStoredValueForKey(value, _ERCredential.USER_KEY);
  }

  public void setUserRelationship(er.users.model.ERUser value) {
    if (_ERCredential.LOG.isDebugEnabled()) {
      _ERCredential.LOG.debug("updating user from " + user() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setUser(value);
    }
    else if (value == null) {
    	er.users.model.ERUser oldValue = user();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCredential.USER_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCredential.USER_KEY);
    }
  }

}
