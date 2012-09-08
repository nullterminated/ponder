// DO NOT EDIT.  Make changes to er.auth.model.ERRole.java instead.
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
public abstract class _ERRole extends er.extensions.eof.ERXGenericRecord {
  public static final String ENTITY_NAME = "ERRole";

  // Attributes
  public static final ERXKey<String> ROLE_NAME = new ERXKey<String>("roleName");
  public static final String ROLE_NAME_KEY = ROLE_NAME.key();

  // Relationships
  public static final ERXKey<er.auth.model.EREntityPermission> ENTITY_PERMISSIONS = new ERXKey<er.auth.model.EREntityPermission>("entityPermissions");
  public static final String ENTITY_PERMISSIONS_KEY = ENTITY_PERMISSIONS.key();

  public static class _ERRoleClazz<T extends er.auth.model.ERRole> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERRole.class);

  public er.auth.model.ERRole.ERRoleClazz clazz() {
    return er.auth.model.ERRole.clazz;
  }
  
  public String roleName() {
    return (String) storedValueForKey(_ERRole.ROLE_NAME_KEY);
  }

  public void setRoleName(String value) {
    if (_ERRole.LOG.isDebugEnabled()) {
    	_ERRole.LOG.debug( "updating roleName from " + roleName() + " to " + value);
    }
    takeStoredValueForKey(value, _ERRole.ROLE_NAME_KEY);
  }

  public NSArray<er.auth.model.EREntityPermission> entityPermissions() {
    return (NSArray<er.auth.model.EREntityPermission>)storedValueForKey(_ERRole.ENTITY_PERMISSIONS_KEY);
  }

  public NSArray<er.auth.model.EREntityPermission> entityPermissions(EOQualifier qualifier) {
    return entityPermissions(qualifier, null, false);
  }

  public NSArray<er.auth.model.EREntityPermission> entityPermissions(EOQualifier qualifier, boolean fetch) {
    return entityPermissions(qualifier, null, fetch);
  }

  public NSArray<er.auth.model.EREntityPermission> entityPermissions(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings, boolean fetch) {
    NSArray<er.auth.model.EREntityPermission> results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(er.auth.model.EREntityPermission.ROLE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray<EOQualifier> qualifiers = new NSMutableArray<EOQualifier>();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = er.auth.model.EREntityPermission.clazz.objectsMatchingQualifier(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = entityPermissions();
      if (qualifier != null) {
        results = (NSArray<er.auth.model.EREntityPermission>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<er.auth.model.EREntityPermission>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToEntityPermissions(er.auth.model.EREntityPermission object) {
    includeObjectIntoPropertyWithKey(object, _ERRole.ENTITY_PERMISSIONS_KEY);
  }

  public void removeFromEntityPermissions(er.auth.model.EREntityPermission object) {
    excludeObjectFromPropertyWithKey(object, _ERRole.ENTITY_PERMISSIONS_KEY);
  }

  public void addToEntityPermissionsRelationship(er.auth.model.EREntityPermission object) {
    if (_ERRole.LOG.isDebugEnabled()) {
      _ERRole.LOG.debug("adding " + object + " to entityPermissions relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToEntityPermissions(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _ERRole.ENTITY_PERMISSIONS_KEY);
    }
  }

  public void removeFromEntityPermissionsRelationship(er.auth.model.EREntityPermission object) {
    if (_ERRole.LOG.isDebugEnabled()) {
      _ERRole.LOG.debug("removing " + object + " from entityPermissions relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromEntityPermissions(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _ERRole.ENTITY_PERMISSIONS_KEY);
    }
  }

  public er.auth.model.EREntityPermission createEntityPermissionsRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( er.auth.model.EREntityPermission.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _ERRole.ENTITY_PERMISSIONS_KEY);
    return (er.auth.model.EREntityPermission) eo;
  }

  public void deleteEntityPermissionsRelationship(er.auth.model.EREntityPermission object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _ERRole.ENTITY_PERMISSIONS_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllEntityPermissionsRelationships() {
    Enumeration<er.auth.model.EREntityPermission> objects = entityPermissions().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteEntityPermissionsRelationship(objects.nextElement());
    }
  }


}
