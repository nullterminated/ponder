// DO NOT EDIT.  Make changes to er.auth.model.ERTwoFactorAuthenticationRequest.java instead.
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
public abstract class _ERTwoFactorAuthenticationRequest extends er.auth.model.ERAuthenticationRequest implements CRUDEntity {
  public static final String ENTITY_NAME = "ERTwoFactorAuthenticationRequest";

  // Attribute Keys
  public static final ERXKey<String> INET_ADDRESS = new ERXKey<String>("inetAddress");
  public static final ERXKey<org.joda.time.DateTime> REQUEST_DATE = new ERXKey<org.joda.time.DateTime>("requestDate");
  public static final ERXKey<String> SUBTYPE = new ERXKey<String>("subtype");
  public static final ERXKey<String> USERNAME = new ERXKey<String>("username");

  // Relationship Keys

  // Attributes
  public static final String INET_ADDRESS_KEY = INET_ADDRESS.key();
  public static final String REQUEST_DATE_KEY = REQUEST_DATE.key();
  public static final String SUBTYPE_KEY = SUBTYPE.key();
  public static final String USERNAME_KEY = USERNAME.key();

  // Relationships

  private static Logger LOG = Logger.getLogger(_ERTwoFactorAuthenticationRequest.class);

  public er.auth.model.ERTwoFactorAuthenticationRequest.ERTwoFactorAuthenticationRequestClazz clazz() {
    return er.auth.model.ERTwoFactorAuthenticationRequest.clazz;
  }
  
  public static class _ERTwoFactorAuthenticationRequestClazz<T extends er.auth.model.ERTwoFactorAuthenticationRequest> extends er.auth.model.ERAuthenticationRequest.ERAuthenticationRequestClazz<T> implements CRUDClazz {
    /* more clazz methods here */
    public boolean canCreate(CRUDAuthorization auth) {
      return auth.canCreate(er.auth.model.ERTwoFactorAuthenticationRequest.clazz);
    }
    public boolean canQuery(CRUDAuthorization auth) {
        return auth.canQuery(er.auth.model.ERTwoFactorAuthenticationRequest.clazz);
    }
    public EOQualifier restrictingQualifier(CRUDAuthorization auth) {
        return auth.restrictingQualifier(er.auth.model.ERTwoFactorAuthenticationRequest.clazz);
    }
  }

  public boolean canDelete(CRUDAuthorization auth) {
    return auth.canDelete((er.auth.model.ERTwoFactorAuthenticationRequest)this);
  }
  
  public boolean canRead(CRUDAuthorization auth) {
	    return auth.canRead((er.auth.model.ERTwoFactorAuthenticationRequest)this);
  }
  
  public boolean canReadProperty(CRUDAuthorization auth, String propertyKey) {
	    return auth.canReadProperty((er.auth.model.ERTwoFactorAuthenticationRequest)this, propertyKey);
  }
  
  public boolean canUpdate(CRUDAuthorization auth) {
	    return auth.canUpdate((er.auth.model.ERTwoFactorAuthenticationRequest)this);
  }
  
  public boolean canUpdateProperty(CRUDAuthorization auth, String propertyKey) {
	    return auth.canUpdateProperty((er.auth.model.ERTwoFactorAuthenticationRequest)this, propertyKey);
  }

	
  public String username() {
    return (String) storedValueForKey(_ERTwoFactorAuthenticationRequest.USERNAME_KEY);
  }

  public void setUsername(String value) {
    if (_ERTwoFactorAuthenticationRequest.LOG.isDebugEnabled()) {
    	_ERTwoFactorAuthenticationRequest.LOG.debug( "updating username from " + username() + " to " + value);
    }
    takeStoredValueForKey(value, _ERTwoFactorAuthenticationRequest.USERNAME_KEY);
  }


  public static er.auth.model.ERTwoFactorAuthenticationRequest createERTwoFactorAuthenticationRequest(EOEditingContext editingContext, org.joda.time.DateTime requestDate, String username) {
    er.auth.model.ERTwoFactorAuthenticationRequest eo = (er.auth.model.ERTwoFactorAuthenticationRequest) EOUtilities.createAndInsertInstance(editingContext, _ERTwoFactorAuthenticationRequest.ENTITY_NAME);    
	eo.setRequestDate(requestDate);
	eo.setUsername(username);
    return eo;
  }
}
