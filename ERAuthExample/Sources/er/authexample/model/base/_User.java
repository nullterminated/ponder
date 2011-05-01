// DO NOT EDIT.  Make changes to er.authexample.model.User.java instead.
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
public abstract class _User extends ERXGenericRecord {
  public static final String ENTITY_NAME = "User";

  // Attribute Keys
  public static final ERXKey<org.joda.time.DateTime> DATE_CREATED = new ERXKey<org.joda.time.DateTime>("dateCreated");
  public static final ERXKey<String> EMAIL_ADDRESS = new ERXKey<String>("emailAddress");
  public static final ERXKey<String> PASSWORD = new ERXKey<String>("password");
  public static final ERXKey<String> USERNAME = new ERXKey<String>("username");

  // Relationship Keys
  public static final ERXKey<er.authexample.model.Credential> CREDENTIALS = new ERXKey<er.authexample.model.Credential>("credentials");

  // Attributes
  public static final String DATE_CREATED_KEY = DATE_CREATED.key();
  public static final String EMAIL_ADDRESS_KEY = EMAIL_ADDRESS.key();
  public static final String PASSWORD_KEY = PASSWORD.key();
  public static final String USERNAME_KEY = USERNAME.key();

  // Relationships
  public static final String CREDENTIALS_KEY = CREDENTIALS.key();

  public static class _UserClazz<T extends er.authexample.model.User> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static Logger LOG = Logger.getLogger(_User.class);

  public er.authexample.model.User.UserClazz clazz() {
    return er.authexample.model.User.clazz;
  }
  
	
  public org.joda.time.DateTime dateCreated() {
    return (org.joda.time.DateTime) storedValueForKey(_User.DATE_CREATED_KEY);
  }

  public void setDateCreated(org.joda.time.DateTime value) {
    if (_User.LOG.isDebugEnabled()) {
    	_User.LOG.debug( "updating dateCreated from " + dateCreated() + " to " + value);
    }
    takeStoredValueForKey(value, _User.DATE_CREATED_KEY);
  }

	
  public String emailAddress() {
    return (String) storedValueForKey(_User.EMAIL_ADDRESS_KEY);
  }

  public void setEmailAddress(String value) {
    if (_User.LOG.isDebugEnabled()) {
    	_User.LOG.debug( "updating emailAddress from " + emailAddress() + " to " + value);
    }
    takeStoredValueForKey(value, _User.EMAIL_ADDRESS_KEY);
  }

	
  public String password() {
    return (String) storedValueForKey(_User.PASSWORD_KEY);
  }

  public void setPassword(String value) {
    if (_User.LOG.isDebugEnabled()) {
    	_User.LOG.debug( "updating password from " + password() + " to " + value);
    }
    takeStoredValueForKey(value, _User.PASSWORD_KEY);
  }

	
  public String username() {
    return (String) storedValueForKey(_User.USERNAME_KEY);
  }

  public void setUsername(String value) {
    if (_User.LOG.isDebugEnabled()) {
    	_User.LOG.debug( "updating username from " + username() + " to " + value);
    }
    takeStoredValueForKey(value, _User.USERNAME_KEY);
  }

  public NSArray<er.authexample.model.Credential> credentials() {
    return (NSArray<er.authexample.model.Credential>)storedValueForKey(_User.CREDENTIALS_KEY);
  }

  public NSArray<er.authexample.model.Credential> credentials(EOQualifier qualifier) {
    return credentials(qualifier, null, false);
  }

  public NSArray<er.authexample.model.Credential> credentials(EOQualifier qualifier, boolean fetch) {
    return credentials(qualifier, null, fetch);
  }

  public NSArray<er.authexample.model.Credential> credentials(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings, boolean fetch) {
    NSArray<er.authexample.model.Credential> results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(er.authexample.model.Credential.USER_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray<EOQualifier> qualifiers = new NSMutableArray<EOQualifier>();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = er.authexample.model.Credential.clazz.objectsMatchingQualifier(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = credentials();
      if (qualifier != null) {
        results = (NSArray<er.authexample.model.Credential>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<er.authexample.model.Credential>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToCredentials(er.authexample.model.Credential object) {
    includeObjectIntoPropertyWithKey(object, _User.CREDENTIALS_KEY);
  }

  public void removeFromCredentials(er.authexample.model.Credential object) {
    excludeObjectFromPropertyWithKey(object, _User.CREDENTIALS_KEY);
  }

  public void addToCredentialsRelationship(er.authexample.model.Credential object) {
    if (_User.LOG.isDebugEnabled()) {
      _User.LOG.debug("adding " + object + " to credentials relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToCredentials(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _User.CREDENTIALS_KEY);
    }
  }

  public void removeFromCredentialsRelationship(er.authexample.model.Credential object) {
    if (_User.LOG.isDebugEnabled()) {
      _User.LOG.debug("removing " + object + " from credentials relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromCredentials(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _User.CREDENTIALS_KEY);
    }
  }

  public er.authexample.model.Credential createCredentialsRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( er.authexample.model.Credential.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _User.CREDENTIALS_KEY);
    return (er.authexample.model.Credential) eo;
  }

  public void deleteCredentialsRelationship(er.authexample.model.Credential object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _User.CREDENTIALS_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllCredentialsRelationships() {
    Enumeration<er.authexample.model.Credential> objects = credentials().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteCredentialsRelationship(objects.nextElement());
    }
  }


  public static er.authexample.model.User createUser(EOEditingContext editingContext, org.joda.time.DateTime dateCreated, String emailAddress, String password, String username) {
    er.authexample.model.User eo = (er.authexample.model.User) EOUtilities.createAndInsertInstance(editingContext, _User.ENTITY_NAME);    
	eo.setDateCreated(dateCreated);
	eo.setEmailAddress(emailAddress);
	eo.setPassword(password);
	eo.setUsername(username);
    return eo;
  }
}
