// DO NOT EDIT.  Make changes to er.corebl.model.ERCMailClob.java instead.
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
public abstract class _ERCMailClob extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERCMailClob";

  // Attributes
  public static final ERXKey<String> MESSAGE = new ERXKey<String>("message");
  public static final String MESSAGE_KEY = MESSAGE.key();

  // Relationships

  public static class _ERCMailClobClazz<T extends er.corebl.model.ERCMailClob> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERCMailClob.class);

  public er.corebl.model.ERCMailClob.ERCMailClobClazz clazz() {
    return er.corebl.model.ERCMailClob.clazz;
  }

  public String message() {
    return (String) storedValueForKey(_ERCMailClob.MESSAGE_KEY);
  }

  public void setMessage(String value) {
    if (_ERCMailClob.LOG.isDebugEnabled()) {
    	_ERCMailClob.LOG.debug( "updating message from " + message() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailClob.MESSAGE_KEY);
  }


}
