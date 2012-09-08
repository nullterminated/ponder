// DO NOT EDIT.  Make changes to er.auth.model.ERAuthenticationRequest.java instead.
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
public abstract class _ERAuthenticationRequest extends er.extensions.eof.ERXGenericRecord {
  public static final String ENTITY_NAME = "ERAuthenticationRequest";

  // Attributes
  public static final ERXKey<String> INET_ADDRESS = new ERXKey<String>("inetAddress");
  public static final String INET_ADDRESS_KEY = INET_ADDRESS.key();
  public static final ERXKey<org.joda.time.DateTime> REQUEST_DATE = new ERXKey<org.joda.time.DateTime>("requestDate");
  public static final String REQUEST_DATE_KEY = REQUEST_DATE.key();
  public static final ERXKey<String> SUBTYPE = new ERXKey<String>("subtype");
  public static final String SUBTYPE_KEY = SUBTYPE.key();

  // Relationships

  public static class _ERAuthenticationRequestClazz<T extends er.auth.model.ERAuthenticationRequest> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERAuthenticationRequest.class);

  public er.auth.model.ERAuthenticationRequest.ERAuthenticationRequestClazz clazz() {
    return er.auth.model.ERAuthenticationRequest.clazz;
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


}
