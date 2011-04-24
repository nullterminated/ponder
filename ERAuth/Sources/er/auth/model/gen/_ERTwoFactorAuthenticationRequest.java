// DO NOT EDIT.  Make changes to er.auth.model.ERTwoFactorAuthenticationRequest.java instead.
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
public abstract class _ERTwoFactorAuthenticationRequest extends er.auth.model.ERAuthenticationRequest {
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

  public static class _ERTwoFactorAuthenticationRequestClazz<T extends er.auth.model.ERTwoFactorAuthenticationRequest> extends er.auth.model.ERAuthenticationRequest.ERAuthenticationRequestClazz<T> {
    /* more clazz methods here */
  }

  private static Logger LOG = Logger.getLogger(_ERTwoFactorAuthenticationRequest.class);

  public er.auth.model.ERTwoFactorAuthenticationRequest.ERTwoFactorAuthenticationRequestClazz clazz() {
    return er.auth.model.ERTwoFactorAuthenticationRequest.clazz;
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
