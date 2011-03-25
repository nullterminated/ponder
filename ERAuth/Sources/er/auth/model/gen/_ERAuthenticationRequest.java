// DO NOT EDIT.  Make changes to er.auth.model.ERAuthenticationRequest.java instead.
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
public abstract class _ERAuthenticationRequest extends er.extensions.eof.ERXGenericRecord implements CRUDEntity {
  public static final String ENTITY_NAME = "ERAuthenticationRequest";

  // Attribute Keys
  public static final ERXKey<String> INET_ADDRESS = new ERXKey<String>("inetAddress");
  public static final ERXKey<org.joda.time.DateTime> REQUEST_DATE = new ERXKey<org.joda.time.DateTime>("requestDate");
  public static final ERXKey<String> SUBTYPE = new ERXKey<String>("subtype");

  // Relationship Keys

  // Attributes
  public static final String INET_ADDRESS_KEY = INET_ADDRESS.key();
  public static final String REQUEST_DATE_KEY = REQUEST_DATE.key();
  public static final String SUBTYPE_KEY = SUBTYPE.key();

  // Relationships

  private static Logger LOG = Logger.getLogger(_ERAuthenticationRequest.class);

  public er.auth.model.ERAuthenticationRequest.ERAuthenticationRequestClazz clazz() {
    return er.auth.model.ERAuthenticationRequest.clazz;
  }
  
  public static class _ERAuthenticationRequestClazz<T extends er.auth.model.ERAuthenticationRequest> extends ERXGenericRecord.ERXGenericRecordClazz<T> implements CRUDClazz {
    /* more clazz methods here */
    public boolean canCreate(CRUDAuthorization auth) {
      return auth.canCreate(er.auth.model.ERAuthenticationRequest.clazz);
    }
    public boolean canQuery(CRUDAuthorization auth) {
        return auth.canQuery(er.auth.model.ERAuthenticationRequest.clazz);
    }
    public EOQualifier restrictingQualifier(CRUDAuthorization auth) {
        return auth.restrictingQualifier(er.auth.model.ERAuthenticationRequest.clazz);
    }
  }

  public boolean canDelete(CRUDAuthorization auth) {
    return auth.canDelete((er.auth.model.ERAuthenticationRequest)this);
  }
  
  public boolean canRead(CRUDAuthorization auth) {
	    return auth.canRead((er.auth.model.ERAuthenticationRequest)this);
  }
  
  public boolean canReadProperty(CRUDAuthorization auth, String propertyKey) {
	    return auth.canReadProperty((er.auth.model.ERAuthenticationRequest)this, propertyKey);
  }
  
  public boolean canUpdate(CRUDAuthorization auth) {
	    return auth.canUpdate((er.auth.model.ERAuthenticationRequest)this);
  }
  
  public boolean canUpdateProperty(CRUDAuthorization auth, String propertyKey) {
	    return auth.canUpdateProperty((er.auth.model.ERAuthenticationRequest)this, propertyKey);
  }

	
  public String inetAddress() {
    return (String) storedValueForKey(_ERAuthenticationRequest.INET_ADDRESS_KEY);
  }

  public void setInetAddress(String value) {
    if (_ERAuthenticationRequest.LOG.isDebugEnabled()) {
    	_ERAuthenticationRequest.LOG.debug( "updating inetAddress from " + inetAddress() + " to " + value);
    }
    takeStoredValueForKey(value, _ERAuthenticationRequest.INET_ADDRESS_KEY);
  }

	
  public org.joda.time.DateTime requestDate() {
    return (org.joda.time.DateTime) storedValueForKey(_ERAuthenticationRequest.REQUEST_DATE_KEY);
  }

  public void setRequestDate(org.joda.time.DateTime value) {
    if (_ERAuthenticationRequest.LOG.isDebugEnabled()) {
    	_ERAuthenticationRequest.LOG.debug( "updating requestDate from " + requestDate() + " to " + value);
    }
    takeStoredValueForKey(value, _ERAuthenticationRequest.REQUEST_DATE_KEY);
  }

	
  public String subtype() {
    return (String) storedValueForKey(_ERAuthenticationRequest.SUBTYPE_KEY);
  }

  public void setSubtype(String value) {
    if (_ERAuthenticationRequest.LOG.isDebugEnabled()) {
    	_ERAuthenticationRequest.LOG.debug( "updating subtype from " + subtype() + " to " + value);
    }
    takeStoredValueForKey(value, _ERAuthenticationRequest.SUBTYPE_KEY);
  }


  public static er.auth.model.ERAuthenticationRequest createERAuthenticationRequest(EOEditingContext editingContext, org.joda.time.DateTime requestDate, String subtype) {
    er.auth.model.ERAuthenticationRequest eo = (er.auth.model.ERAuthenticationRequest) EOUtilities.createAndInsertInstance(editingContext, _ERAuthenticationRequest.ENTITY_NAME);    
	eo.setRequestDate(requestDate);
	eo.setSubtype(subtype);
    return eo;
  }
}
