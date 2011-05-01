// DO NOT EDIT.  Make changes to er.authexample.model.Credential.java instead.
package er.authexample.model.base;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;


@SuppressWarnings("all")
public abstract class _Credential extends ERXGenericRecord {
  public static final String ENTITY_NAME = "Credential";

  // Attribute Keys
  public static final ERXKey<org.joda.time.DateTime> DATE_CREATED = new ERXKey<org.joda.time.DateTime>("dateCreated");
  public static final ERXKey<String> PASSWORD = new ERXKey<String>("password");

  // Relationship Keys
  public static final ERXKey<er.authexample.model.User> USER = new ERXKey<er.authexample.model.User>("user");

  // Attributes
  public static final String DATE_CREATED_KEY = DATE_CREATED.key();
  public static final String PASSWORD_KEY = PASSWORD.key();

  // Relationships
  public static final String USER_KEY = USER.key();

  public static class _CredentialClazz<T extends er.authexample.model.Credential> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static Logger LOG = Logger.getLogger(_Credential.class);

  public er.authexample.model.Credential.CredentialClazz clazz() {
    return er.authexample.model.Credential.clazz;
  }
  
	
  public org.joda.time.DateTime dateCreated() {
    return (org.joda.time.DateTime) storedValueForKey(_Credential.DATE_CREATED_KEY);
  }

  public void setDateCreated(org.joda.time.DateTime value) {
    if (_Credential.LOG.isDebugEnabled()) {
    	_Credential.LOG.debug( "updating dateCreated from " + dateCreated() + " to " + value);
    }
    takeStoredValueForKey(value, _Credential.DATE_CREATED_KEY);
  }

	
  public String password() {
    return (String) storedValueForKey(_Credential.PASSWORD_KEY);
  }

  public void setPassword(String value) {
    if (_Credential.LOG.isDebugEnabled()) {
    	_Credential.LOG.debug( "updating password from " + password() + " to " + value);
    }
    takeStoredValueForKey(value, _Credential.PASSWORD_KEY);
  }

  public er.authexample.model.User user() {
    return (er.authexample.model.User)storedValueForKey(_Credential.USER_KEY);
  }
  
  public void setUser(er.authexample.model.User value) {
    takeStoredValueForKey(value, _Credential.USER_KEY);
  }

  public void setUserRelationship(er.authexample.model.User value) {
    if (_Credential.LOG.isDebugEnabled()) {
      _Credential.LOG.debug("updating user from " + user() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setUser(value);
    }
    else if (value == null) {
    	er.authexample.model.User oldValue = user();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _Credential.USER_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _Credential.USER_KEY);
    }
  }

  public static er.authexample.model.Credential createCredential(EOEditingContext editingContext, org.joda.time.DateTime dateCreated, String password, er.authexample.model.User user) {
    er.authexample.model.Credential eo = (er.authexample.model.Credential) EOUtilities.createAndInsertInstance(editingContext, _Credential.ENTITY_NAME);    
	eo.setDateCreated(dateCreated);
	eo.setPassword(password);
    eo.setUserRelationship(user);
    return eo;
  }
}
