// DO NOT EDIT.  Make changes to er.datum.model.ERDatum.java instead.
package er.datum.model.eogen;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;


@SuppressWarnings("all")
public abstract class _ERDatum extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERDatum";

  // Attributes
  public static final ERXKey<String> SUBTYPE = new ERXKey<String>("subtype");
  public static final String SUBTYPE_KEY = SUBTYPE.key();
  public static final ERXKey<String> TYPE = new ERXKey<String>("type");
  public static final String TYPE_KEY = TYPE.key();

  // Relationships
  public static final ERXKey<er.datum.model.ERDatumObject> DATUM_OBJECT = new ERXKey<er.datum.model.ERDatumObject>("datumObject");
  public static final String DATUM_OBJECT_KEY = DATUM_OBJECT.key();

  public static class _ERDatumClazz<T extends er.datum.model.ERDatum> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERDatum.class);

  public er.datum.model.ERDatum.ERDatumClazz clazz() {
    return er.datum.model.ERDatum.clazz;
  }
  
  public String subtype() {
    return (String) storedValueForKey(_ERDatum.SUBTYPE_KEY);
  }

  public void setSubtype(String value) {
    if (_ERDatum.LOG.isDebugEnabled()) {
    	_ERDatum.LOG.debug( "updating subtype from " + subtype() + " to " + value);
    }
    takeStoredValueForKey(value, _ERDatum.SUBTYPE_KEY);
  }

  public String type() {
    return (String) storedValueForKey(_ERDatum.TYPE_KEY);
  }

  public void setType(String value) {
    if (_ERDatum.LOG.isDebugEnabled()) {
    	_ERDatum.LOG.debug( "updating type from " + type() + " to " + value);
    }
    takeStoredValueForKey(value, _ERDatum.TYPE_KEY);
  }

  public er.datum.model.ERDatumObject datumObject() {
    return (er.datum.model.ERDatumObject)storedValueForKey(_ERDatum.DATUM_OBJECT_KEY);
  }
  
  public void setDatumObject(er.datum.model.ERDatumObject value) {
    takeStoredValueForKey(value, _ERDatum.DATUM_OBJECT_KEY);
  }

  public void setDatumObjectRelationship(er.datum.model.ERDatumObject value) {
    if (_ERDatum.LOG.isDebugEnabled()) {
      _ERDatum.LOG.debug("updating datumObject from " + datumObject() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setDatumObject(value);
    }
    else if (value == null) {
    	er.datum.model.ERDatumObject oldValue = datumObject();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERDatum.DATUM_OBJECT_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERDatum.DATUM_OBJECT_KEY);
    }
  }

}
