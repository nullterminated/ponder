// DO NOT EDIT.  Make changes to er.corebl.model.ERCUserAgent.java instead.
package er.corebl.model.eogen;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;


@SuppressWarnings("all")
public abstract class _ERCUserAgent extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERCUserAgent";

  // Attributes
  public static final ERXKey<NSData> CONTENT_HASH = new ERXKey<NSData>("contentHash");
  public static final String CONTENT_HASH_KEY = CONTENT_HASH.key();
  public static final ERXKey<String> CONTENT_STRING = new ERXKey<String>("contentString");
  public static final String CONTENT_STRING_KEY = CONTENT_STRING.key();

  // Relationships

  public static class _ERCUserAgentClazz<T extends er.corebl.model.ERCUserAgent> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERCUserAgent.class);

  public er.corebl.model.ERCUserAgent.ERCUserAgentClazz clazz() {
    return er.corebl.model.ERCUserAgent.clazz;
  }
  
  public NSData contentHash() {
    return (NSData) storedValueForKey(_ERCUserAgent.CONTENT_HASH_KEY);
  }

  public void setContentHash(NSData value) {
    if (_ERCUserAgent.LOG.isDebugEnabled()) {
    	_ERCUserAgent.LOG.debug( "updating contentHash from " + contentHash() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCUserAgent.CONTENT_HASH_KEY);
  }

  public String contentString() {
    return (String) storedValueForKey(_ERCUserAgent.CONTENT_STRING_KEY);
  }

  public void setContentString(String value) {
    if (_ERCUserAgent.LOG.isDebugEnabled()) {
    	_ERCUserAgent.LOG.debug( "updating contentString from " + contentString() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCUserAgent.CONTENT_STRING_KEY);
  }


}
