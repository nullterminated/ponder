// DO NOT EDIT.  Make changes to er.auth.model.ERTwoFactorAuthenticationResponse.java instead.
package er.auth.model.gen;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.auth.crud.*;
import er.extensions.eof.*;
import er.extensions.foundation.*;


@SuppressWarnings("all")
public abstract class _ERTwoFactorAuthenticationResponse extends er.auth.model.ERAuthenticationResponse implements CRUDEntity {
  public static final String ENTITY_NAME = "ERTwoFactorAuthenticationResponse";

  // Attribute Keys
  public static final ERXKey<Boolean> AUTHENTICATION_FAILED = new ERXKey<Boolean>("authenticationFailed");
  public static final ERXKey<er.auth.model.enums.ERTwoFactorAuthenticationFailure> AUTHENTICATION_FAILURE_TYPE = new ERXKey<er.auth.model.enums.ERTwoFactorAuthenticationFailure>("authenticationFailureType");
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

  private static Logger LOG = Logger.getLogger(_ERTwoFactorAuthenticationResponse.class);

  public er.auth.model.ERTwoFactorAuthenticationResponse.ERTwoFactorAuthenticationResponseClazz clazz() {
    return er.auth.model.ERTwoFactorAuthenticationResponse.clazz;
  }
  
  public static class _ERTwoFactorAuthenticationResponseClazz<T extends er.auth.model.ERTwoFactorAuthenticationResponse> extends er.auth.model.ERAuthenticationResponse.ERAuthenticationResponseClazz<T> implements CRUDClazz {
    /* more clazz methods here */
    public boolean canCreate(CRUDAuthorization auth) {
      return auth.canCreate(er.auth.model.ERTwoFactorAuthenticationResponse.clazz);
    }
    public boolean canQuery(CRUDAuthorization auth) {
        return auth.canQuery(er.auth.model.ERTwoFactorAuthenticationResponse.clazz);
    }
    public EOQualifier restrictingQualifier(CRUDAuthorization auth) {
        return auth.restrictingQualifier(er.auth.model.ERTwoFactorAuthenticationResponse.clazz);
    }
  }

  public boolean canDelete(CRUDAuthorization auth) {
    return auth.canDelete((er.auth.model.ERTwoFactorAuthenticationResponse)this);
  }
  
  public boolean canRead(CRUDAuthorization auth) {
	    return auth.canRead((er.auth.model.ERTwoFactorAuthenticationResponse)this);
  }
  
  public boolean canReadProperty(CRUDAuthorization auth, String propertyKey) {
	    return auth.canReadProperty((er.auth.model.ERTwoFactorAuthenticationResponse)this, propertyKey);
  }
  
  public boolean canUpdate(CRUDAuthorization auth) {
	    return auth.canUpdate((er.auth.model.ERTwoFactorAuthenticationResponse)this);
  }
  
  public boolean canUpdateProperty(CRUDAuthorization auth, String propertyKey) {
	    return auth.canUpdateProperty((er.auth.model.ERTwoFactorAuthenticationResponse)this, propertyKey);
  }


  public static er.auth.model.ERTwoFactorAuthenticationResponse createERTwoFactorAuthenticationResponse(EOEditingContext editingContext, Boolean authenticationFailed, er.auth.model.ERAuthenticationRequest authenticationRequest) {
    er.auth.model.ERTwoFactorAuthenticationResponse eo = (er.auth.model.ERTwoFactorAuthenticationResponse) EOUtilities.createAndInsertInstance(editingContext, _ERTwoFactorAuthenticationResponse.ENTITY_NAME);    
	eo.setAuthenticationFailed(authenticationFailed);
    eo.setAuthenticationRequestRelationship(authenticationRequest);
    return eo;
  }
}
