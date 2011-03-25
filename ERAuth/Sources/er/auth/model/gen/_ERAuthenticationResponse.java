// DO NOT EDIT.  Make changes to er.auth.model.ERAuthenticationResponse.java instead.
package er.auth.model.gen;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.auth.interfaces.*;
import er.extensions.eof.*;
import er.extensions.foundation.*;


@SuppressWarnings("all")
public abstract class _ERAuthenticationResponse extends er.extensions.eof.ERXGenericRecord implements CRUDEntity {
  public static final String ENTITY_NAME = "ERAuthenticationResponse";

  // Attribute Keys
  public static final ERXKey<Boolean> AUTHENTICATION_FAILED = new ERXKey<Boolean>("authenticationFailed");
  public static final ERXKey<Enum> AUTHENTICATION_FAILURE_TYPE = new ERXKey<Enum>("authenticationFailureType");
  public static final ERXKey<String> SUBTYPE = new ERXKey<String>("subtype");
  public static final ERXKey<er.extensions.eof.ERXKeyGlobalID> USER_ID = new ERXKey<er.extensions.eof.ERXKeyGlobalID>("userID");

  // Relationship Keys
  public static final ERXKey<er.auth.model.ERAuthenticationRequest> AUTHENTICATION_REQUEST = new ERXKey<er.auth.model.ERAuthenticationRequest>("authenticationRequest");

  // Attributes
  public static final String AUTHENTICATION_FAILED_KEY = AUTHENTICATION_FAILED.key();
  public static final String AUTHENTICATION_FAILURE_TYPE_KEY = AUTHENTICATION_FAILURE_TYPE.key();
  public static final String SUBTYPE_KEY = SUBTYPE.key();
  public static final String USER_ID_KEY = USER_ID.key();

  // Relationships
  public static final String AUTHENTICATION_REQUEST_KEY = AUTHENTICATION_REQUEST.key();

  private static Logger LOG = Logger.getLogger(_ERAuthenticationResponse.class);

  public er.auth.model.ERAuthenticationResponse.ERAuthenticationResponseClazz clazz() {
    return er.auth.model.ERAuthenticationResponse.clazz;
  }
  
  public static class _ERAuthenticationResponseClazz<T extends er.auth.model.ERAuthenticationResponse> extends ERXGenericRecord.ERXGenericRecordClazz<T> implements CRUDClazz {
    /* more clazz methods here */
    public boolean canCreate(CRUDAuthorization auth) {
      return auth.canCreate(er.auth.model.ERAuthenticationResponse.clazz);
    }
    public boolean canQuery(CRUDAuthorization auth) {
        return auth.canQuery(er.auth.model.ERAuthenticationResponse.clazz);
    }
    public EOQualifier restrictingQualifier(CRUDAuthorization auth) {
        return auth.restrictingQualifier(er.auth.model.ERAuthenticationResponse.clazz);
    }
  }

  public boolean canDelete(CRUDAuthorization auth) {
    return auth.canDelete((er.auth.model.ERAuthenticationResponse)this);
  }
  
  public boolean canRead(CRUDAuthorization auth) {
	    return auth.canRead((er.auth.model.ERAuthenticationResponse)this);
  }
  
  public boolean canReadProperty(CRUDAuthorization auth, String propertyKey) {
	    return auth.canReadProperty((er.auth.model.ERAuthenticationResponse)this, propertyKey);
  }
  
  public boolean canUpdate(CRUDAuthorization auth) {
	    return auth.canUpdate((er.auth.model.ERAuthenticationResponse)this);
  }
  
  public boolean canUpdateProperty(CRUDAuthorization auth, String propertyKey) {
	    return auth.canUpdateProperty((er.auth.model.ERAuthenticationResponse)this, propertyKey);
  }

	
  public Boolean authenticationFailed() {
    return (Boolean) storedValueForKey(_ERAuthenticationResponse.AUTHENTICATION_FAILED_KEY);
  }

  public void setAuthenticationFailed(Boolean value) {
    if (_ERAuthenticationResponse.LOG.isDebugEnabled()) {
    	_ERAuthenticationResponse.LOG.debug( "updating authenticationFailed from " + authenticationFailed() + " to " + value);
    }
    takeStoredValueForKey(value, _ERAuthenticationResponse.AUTHENTICATION_FAILED_KEY);
  }

	
  public Enum authenticationFailureType() {
    return (Enum) storedValueForKey(_ERAuthenticationResponse.AUTHENTICATION_FAILURE_TYPE_KEY);
  }

  public void setAuthenticationFailureType(Enum value) {
    if (_ERAuthenticationResponse.LOG.isDebugEnabled()) {
    	_ERAuthenticationResponse.LOG.debug( "updating authenticationFailureType from " + authenticationFailureType() + " to " + value);
    }
    takeStoredValueForKey(value, _ERAuthenticationResponse.AUTHENTICATION_FAILURE_TYPE_KEY);
  }

	
  public String subtype() {
    return (String) storedValueForKey(_ERAuthenticationResponse.SUBTYPE_KEY);
  }

  public void setSubtype(String value) {
    if (_ERAuthenticationResponse.LOG.isDebugEnabled()) {
    	_ERAuthenticationResponse.LOG.debug( "updating subtype from " + subtype() + " to " + value);
    }
    takeStoredValueForKey(value, _ERAuthenticationResponse.SUBTYPE_KEY);
  }

	
  public er.extensions.eof.ERXKeyGlobalID userID() {
    return (er.extensions.eof.ERXKeyGlobalID) storedValueForKey(_ERAuthenticationResponse.USER_ID_KEY);
  }

  public void setUserID(er.extensions.eof.ERXKeyGlobalID value) {
    if (_ERAuthenticationResponse.LOG.isDebugEnabled()) {
    	_ERAuthenticationResponse.LOG.debug( "updating userID from " + userID() + " to " + value);
    }
    takeStoredValueForKey(value, _ERAuthenticationResponse.USER_ID_KEY);
  }

  public er.auth.model.ERAuthenticationRequest authenticationRequest() {
    return (er.auth.model.ERAuthenticationRequest)storedValueForKey(_ERAuthenticationResponse.AUTHENTICATION_REQUEST_KEY);
  }
  
  public void setAuthenticationRequest(er.auth.model.ERAuthenticationRequest value) {
    takeStoredValueForKey(value, _ERAuthenticationResponse.AUTHENTICATION_REQUEST_KEY);
  }

  public void setAuthenticationRequestRelationship(er.auth.model.ERAuthenticationRequest value) {
    if (_ERAuthenticationResponse.LOG.isDebugEnabled()) {
      _ERAuthenticationResponse.LOG.debug("updating authenticationRequest from " + authenticationRequest() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setAuthenticationRequest(value);
    }
    else if (value == null) {
    	er.auth.model.ERAuthenticationRequest oldValue = authenticationRequest();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERAuthenticationResponse.AUTHENTICATION_REQUEST_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERAuthenticationResponse.AUTHENTICATION_REQUEST_KEY);
    }
  }

  public static er.auth.model.ERAuthenticationResponse createERAuthenticationResponse(EOEditingContext editingContext, Boolean authenticationFailed, String subtype, er.auth.model.ERAuthenticationRequest authenticationRequest) {
    er.auth.model.ERAuthenticationResponse eo = (er.auth.model.ERAuthenticationResponse) EOUtilities.createAndInsertInstance(editingContext, _ERAuthenticationResponse.ENTITY_NAME);    
	eo.setAuthenticationFailed(authenticationFailed);
	eo.setSubtype(subtype);
    eo.setAuthenticationRequestRelationship(authenticationRequest);
    return eo;
  }
}
