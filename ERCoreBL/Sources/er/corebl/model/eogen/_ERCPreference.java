// DO NOT EDIT.  Make changes to er.corebl.model.ERCPreference.java instead.
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
public abstract class _ERCPreference extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERCPreference";

  // Attributes
  public static final ERXKey<String> PREF_KEY = new ERXKey<String>("prefKey");
  public static final String PREF_KEY_KEY = PREF_KEY.key();
  public static final ERXKey<String> PREF_VALUE = new ERXKey<String>("prefValue");
  public static final String PREF_VALUE_KEY = PREF_VALUE.key();
  public static final ERXKey<Integer> USER_ID = new ERXKey<Integer>("userID");
  public static final String USER_ID_KEY = USER_ID.key();

  // Relationships

  public static class _ERCPreferenceClazz<T extends er.corebl.model.ERCPreference> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERCPreference.class);

  public er.corebl.model.ERCPreference.ERCPreferenceClazz clazz() {
    return er.corebl.model.ERCPreference.clazz;
  }

  public String prefKey() {
    return (String) storedValueForKey(_ERCPreference.PREF_KEY_KEY);
  }

  public void setPrefKey(String value) {
    if (_ERCPreference.LOG.isDebugEnabled()) {
    	_ERCPreference.LOG.debug( "updating prefKey from " + prefKey() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCPreference.PREF_KEY_KEY);
  }

  public String prefValue() {
    return (String) storedValueForKey(_ERCPreference.PREF_VALUE_KEY);
  }

  public void setPrefValue(String value) {
    if (_ERCPreference.LOG.isDebugEnabled()) {
    	_ERCPreference.LOG.debug( "updating prefValue from " + prefValue() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCPreference.PREF_VALUE_KEY);
  }

  public Integer userID() {
    return (Integer) storedValueForKey(_ERCPreference.USER_ID_KEY);
  }

  public void setUserID(Integer value) {
    if (_ERCPreference.LOG.isDebugEnabled()) {
    	_ERCPreference.LOG.debug( "updating userID from " + userID() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCPreference.USER_ID_KEY);
  }


}
