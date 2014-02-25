// DO NOT EDIT.  Make changes to er.corebl.model.ERCAuditTrail.java instead.
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
public abstract class _ERCAuditTrail extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERCAuditTrail";

  // Attributes
  public static final ERXKey<er.extensions.eof.ERXKeyGlobalID> GID = new ERXKey<er.extensions.eof.ERXKeyGlobalID>("gid");
  public static final String GID_KEY = GID.key();
  public static final ERXKey<Boolean> IS_DELETED = new ERXKey<Boolean>("isDeleted");
  public static final String IS_DELETED_KEY = IS_DELETED.key();

  // Relationships
  public static final ERXKey<er.corebl.model.ERCAuditTrailEntry> ENTRIES = new ERXKey<er.corebl.model.ERCAuditTrailEntry>("entries");
  public static final String ENTRIES_KEY = ENTRIES.key();

  public static class _ERCAuditTrailClazz<T extends er.corebl.model.ERCAuditTrail> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERCAuditTrail.class);

  public er.corebl.model.ERCAuditTrail.ERCAuditTrailClazz clazz() {
    return er.corebl.model.ERCAuditTrail.clazz;
  }

  public er.extensions.eof.ERXKeyGlobalID gid() {
    return (er.extensions.eof.ERXKeyGlobalID) storedValueForKey(_ERCAuditTrail.GID_KEY);
  }

  public void setGid(er.extensions.eof.ERXKeyGlobalID value) {
    if (_ERCAuditTrail.LOG.isDebugEnabled()) {
    	_ERCAuditTrail.LOG.debug( "updating gid from " + gid() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCAuditTrail.GID_KEY);
  }

  public Boolean isDeleted() {
    return (Boolean) storedValueForKey(_ERCAuditTrail.IS_DELETED_KEY);
  }

  public void setIsDeleted(Boolean value) {
    if (_ERCAuditTrail.LOG.isDebugEnabled()) {
    	_ERCAuditTrail.LOG.debug( "updating isDeleted from " + isDeleted() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCAuditTrail.IS_DELETED_KEY);
  }

  public NSArray<er.corebl.model.ERCAuditTrailEntry> entries() {
    return (NSArray<er.corebl.model.ERCAuditTrailEntry>)storedValueForKey(_ERCAuditTrail.ENTRIES_KEY);
  }

  public NSArray<er.corebl.model.ERCAuditTrailEntry> entries(EOQualifier qualifier) {
    return entries(qualifier, null, false);
  }

  public NSArray<er.corebl.model.ERCAuditTrailEntry> entries(EOQualifier qualifier, boolean fetch) {
    return entries(qualifier, null, fetch);
  }

  public NSArray<er.corebl.model.ERCAuditTrailEntry> entries(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings, boolean fetch) {
    NSArray<er.corebl.model.ERCAuditTrailEntry> results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(er.corebl.model.ERCAuditTrailEntry.TRAIL_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray<EOQualifier> qualifiers = new NSMutableArray<EOQualifier>();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = er.corebl.model.ERCAuditTrailEntry.clazz.objectsMatchingQualifier(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = entries();
      if (qualifier != null) {
        results = (NSArray<er.corebl.model.ERCAuditTrailEntry>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<er.corebl.model.ERCAuditTrailEntry>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }

  public void addToEntries(er.corebl.model.ERCAuditTrailEntry object) {
    includeObjectIntoPropertyWithKey(object, _ERCAuditTrail.ENTRIES_KEY);
  }

  public void removeFromEntries(er.corebl.model.ERCAuditTrailEntry object) {
    excludeObjectFromPropertyWithKey(object, _ERCAuditTrail.ENTRIES_KEY);
  }

  public void addToEntriesRelationship(er.corebl.model.ERCAuditTrailEntry object) {
    if (_ERCAuditTrail.LOG.isDebugEnabled()) {
      _ERCAuditTrail.LOG.debug("adding " + object + " to entries relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToEntries(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _ERCAuditTrail.ENTRIES_KEY);
    }
  }

  public void removeFromEntriesRelationship(er.corebl.model.ERCAuditTrailEntry object) {
    if (_ERCAuditTrail.LOG.isDebugEnabled()) {
      _ERCAuditTrail.LOG.debug("removing " + object + " from entries relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromEntries(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _ERCAuditTrail.ENTRIES_KEY);
    }
  }

  public er.corebl.model.ERCAuditTrailEntry createEntriesRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( er.corebl.model.ERCAuditTrailEntry.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _ERCAuditTrail.ENTRIES_KEY);
    return (er.corebl.model.ERCAuditTrailEntry) eo;
  }

  public void deleteEntriesRelationship(er.corebl.model.ERCAuditTrailEntry object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _ERCAuditTrail.ENTRIES_KEY);
  }

  public void deleteAllEntriesRelationships() {
    Enumeration<er.corebl.model.ERCAuditTrailEntry> objects = entries().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteEntriesRelationship(objects.nextElement());
    }
  }


}
