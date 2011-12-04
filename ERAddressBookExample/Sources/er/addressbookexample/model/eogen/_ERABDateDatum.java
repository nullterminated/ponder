// DO NOT EDIT.  Make changes to er.addressbookexample.model.ERABDateDatum.java instead.
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
public abstract class _ERABDateDatum extends er.datum.model.ERDatum {
  public static final String ENTITY_NAME = "ERABDateDatum";

  // Attributes
  public static final ERXKey<String> SUBTYPE = new ERXKey<String>("subtype");
  public static final String SUBTYPE_KEY = SUBTYPE.key();
  public static final ERXKey<String> TYPE = new ERXKey<String>("type");
  public static final String TYPE_KEY = TYPE.key();
  public static final ERXKey<org.joda.time.LocalDate> VALUE = new ERXKey<org.joda.time.LocalDate>("value");
  public static final String VALUE_KEY = VALUE.key();

  // Relationships
  public static final ERXKey<er.datum.model.ERDatumObject> DATUM_OBJECT = new ERXKey<er.datum.model.ERDatumObject>("datumObject");
  public static final String DATUM_OBJECT_KEY = DATUM_OBJECT.key();

  public static class _ERABDateDatumClazz<T extends er.addressbookexample.model.ERABDateDatum> extends er.datum.model.ERDatum.ERDatumClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERABDateDatum.class);

  public er.addressbookexample.model.ERABDateDatum.ERABDateDatumClazz clazz() {
    return er.addressbookexample.model.ERABDateDatum.clazz;
  }
  
  public org.joda.time.LocalDate value() {
    return (org.joda.time.LocalDate) storedValueForKey(_ERABDateDatum.VALUE_KEY);
  }

  public void setValue(org.joda.time.LocalDate value) {
    if (_ERABDateDatum.LOG.isDebugEnabled()) {
    	_ERABDateDatum.LOG.debug( "updating value from " + value() + " to " + value);
    }
    takeStoredValueForKey(value, _ERABDateDatum.VALUE_KEY);
  }


}
