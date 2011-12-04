// DO NOT EDIT.  Make changes to er.datum.model.ERDatumObject.java instead.
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
public abstract class _ERDatumObject extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERDatumObject";

  // Attributes
  public static final ERXKey<String> SUBTYPE = new ERXKey<String>("subtype");
  public static final String SUBTYPE_KEY = SUBTYPE.key();

  // Relationships
  public static final ERXKey<er.datum.model.ERDatum> OBJECT_DATA = new ERXKey<er.datum.model.ERDatum>("objectData");
  public static final String OBJECT_DATA_KEY = OBJECT_DATA.key();

  public static class _ERDatumObjectClazz<T extends er.datum.model.ERDatumObject> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERDatumObject.class);

  public er.datum.model.ERDatumObject.ERDatumObjectClazz clazz() {
    return er.datum.model.ERDatumObject.clazz;
  }
  
  public String subtype() {
    return (String) storedValueForKey(_ERDatumObject.SUBTYPE_KEY);
  }

  public void setSubtype(String value) {
    if (_ERDatumObject.LOG.isDebugEnabled()) {
    	_ERDatumObject.LOG.debug( "updating subtype from " + subtype() + " to " + value);
    }
    takeStoredValueForKey(value, _ERDatumObject.SUBTYPE_KEY);
  }

  public NSArray<er.datum.model.ERDatum> objectData() {
    return (NSArray<er.datum.model.ERDatum>)storedValueForKey(_ERDatumObject.OBJECT_DATA_KEY);
  }

  public NSArray<er.datum.model.ERDatum> objectData(EOQualifier qualifier) {
    return objectData(qualifier, null, false);
  }

  public NSArray<er.datum.model.ERDatum> objectData(EOQualifier qualifier, boolean fetch) {
    return objectData(qualifier, null, fetch);
  }

  public NSArray<er.datum.model.ERDatum> objectData(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings, boolean fetch) {
    NSArray<er.datum.model.ERDatum> results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(er.datum.model.ERDatum.DATUM_OBJECT_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray<EOQualifier> qualifiers = new NSMutableArray<EOQualifier>();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = er.datum.model.ERDatum.clazz.objectsMatchingQualifier(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = objectData();
      if (qualifier != null) {
        results = (NSArray<er.datum.model.ERDatum>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<er.datum.model.ERDatum>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToObjectData(er.datum.model.ERDatum object) {
    includeObjectIntoPropertyWithKey(object, _ERDatumObject.OBJECT_DATA_KEY);
  }

  public void removeFromObjectData(er.datum.model.ERDatum object) {
    excludeObjectFromPropertyWithKey(object, _ERDatumObject.OBJECT_DATA_KEY);
  }

  public void addToObjectDataRelationship(er.datum.model.ERDatum object) {
    if (_ERDatumObject.LOG.isDebugEnabled()) {
      _ERDatumObject.LOG.debug("adding " + object + " to objectData relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToObjectData(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _ERDatumObject.OBJECT_DATA_KEY);
    }
  }

  public void removeFromObjectDataRelationship(er.datum.model.ERDatum object) {
    if (_ERDatumObject.LOG.isDebugEnabled()) {
      _ERDatumObject.LOG.debug("removing " + object + " from objectData relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromObjectData(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _ERDatumObject.OBJECT_DATA_KEY);
    }
  }

  public er.datum.model.ERDatum createObjectDataRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( er.datum.model.ERDatum.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _ERDatumObject.OBJECT_DATA_KEY);
    return (er.datum.model.ERDatum) eo;
  }

  public void deleteObjectDataRelationship(er.datum.model.ERDatum object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _ERDatumObject.OBJECT_DATA_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllObjectDataRelationships() {
    Enumeration<er.datum.model.ERDatum> objects = objectData().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteObjectDataRelationship(objects.nextElement());
    }
  }


}
