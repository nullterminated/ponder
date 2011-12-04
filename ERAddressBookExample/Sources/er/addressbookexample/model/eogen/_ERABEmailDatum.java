// DO NOT EDIT.  Make changes to er.addressbookexample.model.ERABEmailDatum.java instead.
package er.addressbookexample.model.eogen;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;


@SuppressWarnings("all")
public abstract class _ERABEmailDatum extends er.datum.model.ERDatum {
  public static final String ENTITY_NAME = "ERABEmailDatum";

  // Attributes
  public static final ERXKey<String> SUBTYPE = new ERXKey<String>("subtype");
  public static final String SUBTYPE_KEY = SUBTYPE.key();
  public static final ERXKey<String> TYPE = new ERXKey<String>("type");
  public static final String TYPE_KEY = TYPE.key();
  public static final ERXKey<String> VALUE = new ERXKey<String>("value");
  public static final String VALUE_KEY = VALUE.key();

  // Relationships
  public static final ERXKey<er.datum.model.ERDatumObject> DATUM_OBJECT = new ERXKey<er.datum.model.ERDatumObject>("datumObject");
  public static final String DATUM_OBJECT_KEY = DATUM_OBJECT.key();

  public static class _ERABEmailDatumClazz<T extends er.addressbookexample.model.ERABEmailDatum> extends er.datum.model.ERDatum.ERDatumClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERABEmailDatum.class);

  public er.addressbookexample.model.ERABEmailDatum.ERABEmailDatumClazz clazz() {
    return er.addressbookexample.model.ERABEmailDatum.clazz;
  }
  
  public String value() {
    return (String) storedValueForKey(_ERABEmailDatum.VALUE_KEY);
  }

  public void setValue(String value) {
    if (_ERABEmailDatum.LOG.isDebugEnabled()) {
    	_ERABEmailDatum.LOG.debug( "updating value from " + value() + " to " + value);
    }
    takeStoredValueForKey(value, _ERABEmailDatum.VALUE_KEY);
  }


}
