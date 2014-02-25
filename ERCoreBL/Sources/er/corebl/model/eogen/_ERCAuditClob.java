// DO NOT EDIT.  Make changes to er.corebl.model.ERCAuditClob.java instead.
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
public abstract class _ERCAuditClob extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERCAuditClob";

  // Attributes
  public static final ERXKey<String> VALUES_STRING = new ERXKey<String>("valuesString");
  public static final String VALUES_STRING_KEY = VALUES_STRING.key();

  // Relationships

  public static class _ERCAuditClobClazz<T extends er.corebl.model.ERCAuditClob> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERCAuditClob.class);

  public er.corebl.model.ERCAuditClob.ERCAuditClobClazz clazz() {
    return er.corebl.model.ERCAuditClob.clazz;
  }

  public String valuesString() {
    return (String) storedValueForKey(_ERCAuditClob.VALUES_STRING_KEY);
  }

  public void setValuesString(String value) {
    if (_ERCAuditClob.LOG.isDebugEnabled()) {
    	_ERCAuditClob.LOG.debug( "updating valuesString from " + valuesString() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCAuditClob.VALUES_STRING_KEY);
  }


}
