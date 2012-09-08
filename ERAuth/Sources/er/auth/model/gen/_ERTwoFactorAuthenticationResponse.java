// DO NOT EDIT.  Make changes to er.auth.model.ERTwoFactorAuthenticationResponse.java instead.
package er.auth.model.gen;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;


@SuppressWarnings("all")
public abstract class _ERTwoFactorAuthenticationResponse extends er.auth.model.ERAuthenticationResponse {
  public static final String ENTITY_NAME = "ERTwoFactorAuthenticationResponse";

  // Attributes
  public static final ERXKey<Boolean> AUTHENTICATION_FAILED = new ERXKey<Boolean>("authenticationFailed");
  public static final String AUTHENTICATION_FAILED_KEY = AUTHENTICATION_FAILED.key();
  public static final ERXKey<er.auth.model.enums.ERTwoFactorAuthenticationFailure> AUTHENTICATION_FAILURE_TYPE = new ERXKey<er.auth.model.enums.ERTwoFactorAuthenticationFailure>("authenticationFailureType");
  public static final String AUTHENTICATION_FAILURE_TYPE_KEY = AUTHENTICATION_FAILURE_TYPE.key();
  public static final ERXKey<String> SUBTYPE = new ERXKey<String>("subtype");
  public static final String SUBTYPE_KEY = SUBTYPE.key();
  public static final ERXKey<er.extensions.eof.ERXKeyGlobalID> USER_ID = new ERXKey<er.extensions.eof.ERXKeyGlobalID>("userID");
  public static final String USER_ID_KEY = USER_ID.key();

  // Relationships
  public static final ERXKey<er.auth.model.ERAuthenticationRequest> AUTHENTICATION_REQUEST = new ERXKey<er.auth.model.ERAuthenticationRequest>("authenticationRequest");
  public static final String AUTHENTICATION_REQUEST_KEY = AUTHENTICATION_REQUEST.key();

  public static class _ERTwoFactorAuthenticationResponseClazz<T extends er.auth.model.ERTwoFactorAuthenticationResponse> extends er.auth.model.ERAuthenticationResponse.ERAuthenticationResponseClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERTwoFactorAuthenticationResponse.class);

  public er.auth.model.ERTwoFactorAuthenticationResponse.ERTwoFactorAuthenticationResponseClazz clazz() {
    return er.auth.model.ERTwoFactorAuthenticationResponse.clazz;
  }
  

}
