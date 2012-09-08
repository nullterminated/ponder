// DO NOT EDIT.  Make changes to er.auth.model.EREntityPermission.java instead.
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
public abstract class _EREntityPermission extends er.extensions.eof.ERXGenericRecord {
  public static final String ENTITY_NAME = "EREntityPermission";

  // Attributes
  public static final ERXKey<Boolean> ALLOW_CREATE = new ERXKey<Boolean>("allowCreate");
  public static final String ALLOW_CREATE_KEY = ALLOW_CREATE.key();
  public static final ERXKey<Boolean> ALLOW_DELETE = new ERXKey<Boolean>("allowDelete");
  public static final String ALLOW_DELETE_KEY = ALLOW_DELETE.key();
  public static final ERXKey<Boolean> ALLOW_QUERY = new ERXKey<Boolean>("allowQuery");
  public static final String ALLOW_QUERY_KEY = ALLOW_QUERY.key();
  public static final ERXKey<Boolean> ALLOW_READ = new ERXKey<Boolean>("allowRead");
  public static final String ALLOW_READ_KEY = ALLOW_READ.key();
  public static final ERXKey<Boolean> ALLOW_UPDATE = new ERXKey<Boolean>("allowUpdate");
  public static final String ALLOW_UPDATE_KEY = ALLOW_UPDATE.key();
  public static final ERXKey<String> NAME_FOR_ENTITY = new ERXKey<String>("nameForEntity");
  public static final String NAME_FOR_ENTITY_KEY = NAME_FOR_ENTITY.key();

  // Relationships
  public static final ERXKey<er.auth.model.ERPropertyPermission> PROPERTY_PERMISSIONS = new ERXKey<er.auth.model.ERPropertyPermission>("propertyPermissions");
  public static final String PROPERTY_PERMISSIONS_KEY = PROPERTY_PERMISSIONS.key();
  public static final ERXKey<er.auth.model.ERRole> ROLE = new ERXKey<er.auth.model.ERRole>("role");
  public static final String ROLE_KEY = ROLE.key();

  public static class _EREntityPermissionClazz<T extends er.auth.model.EREntityPermission> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_EREntityPermission.class);

  public er.auth.model.EREntityPermission.EREntityPermissionClazz clazz() {
    return er.auth.model.EREntityPermission.clazz;
  }
  
  public Boolean allowCreate() {
    return (Boolean) storedValueForKey(_EREntityPermission.ALLOW_CREATE_KEY);
  }

  public void setAllowCreate(Boolean value) {
    if (_EREntityPermission.LOG.isDebugEnabled()) {
    	_EREntityPermission.LOG.debug( "updating allowCreate from " + allowCreate() + " to " + value);
    }
    takeStoredValueForKey(value, _EREntityPermission.ALLOW_CREATE_KEY);
  }

  public Boolean allowDelete() {
    return (Boolean) storedValueForKey(_EREntityPermission.ALLOW_DELETE_KEY);
  }

  public void setAllowDelete(Boolean value) {
    if (_EREntityPermission.LOG.isDebugEnabled()) {
    	_EREntityPermission.LOG.debug( "updating allowDelete from " + allowDelete() + " to " + value);
    }
    takeStoredValueForKey(value, _EREntityPermission.ALLOW_DELETE_KEY);
  }

  public Boolean allowQuery() {
    return (Boolean) storedValueForKey(_EREntityPermission.ALLOW_QUERY_KEY);
  }

  public void setAllowQuery(Boolean value) {
    if (_EREntityPermission.LOG.isDebugEnabled()) {
    	_EREntityPermission.LOG.debug( "updating allowQuery from " + allowQuery() + " to " + value);
    }
    takeStoredValueForKey(value, _EREntityPermission.ALLOW_QUERY_KEY);
  }

  public Boolean allowRead() {
    return (Boolean) storedValueForKey(_EREntityPermission.ALLOW_READ_KEY);
  }

  public void setAllowRead(Boolean value) {
    if (_EREntityPermission.LOG.isDebugEnabled()) {
    	_EREntityPermission.LOG.debug( "updating allowRead from " + allowRead() + " to " + value);
    }
    takeStoredValueForKey(value, _EREntityPermission.ALLOW_READ_KEY);
  }

  public Boolean allowUpdate() {
    return (Boolean) storedValueForKey(_EREntityPermission.ALLOW_UPDATE_KEY);
  }

  public void setAllowUpdate(Boolean value) {
    if (_EREntityPermission.LOG.isDebugEnabled()) {
    	_EREntityPermission.LOG.debug( "updating allowUpdate from " + allowUpdate() + " to " + value);
    }
    takeStoredValueForKey(value, _EREntityPermission.ALLOW_UPDATE_KEY);
  }

  public String nameForEntity() {
    return (String) storedValueForKey(_EREntityPermission.NAME_FOR_ENTITY_KEY);
  }

  public void setNameForEntity(String value) {
    if (_EREntityPermission.LOG.isDebugEnabled()) {
    	_EREntityPermission.LOG.debug( "updating nameForEntity from " + nameForEntity() + " to " + value);
    }
    takeStoredValueForKey(value, _EREntityPermission.NAME_FOR_ENTITY_KEY);
  }

  public er.auth.model.ERRole role() {
    return (er.auth.model.ERRole)storedValueForKey(_EREntityPermission.ROLE_KEY);
  }
  
  public void setRole(er.auth.model.ERRole value) {
    takeStoredValueForKey(value, _EREntityPermission.ROLE_KEY);
  }

  public void setRoleRelationship(er.auth.model.ERRole value) {
    if (_EREntityPermission.LOG.isDebugEnabled()) {
      _EREntityPermission.LOG.debug("updating role from " + role() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setRole(value);
    }
    else if (value == null) {
    	er.auth.model.ERRole oldValue = role();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _EREntityPermission.ROLE_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _EREntityPermission.ROLE_KEY);
    }
  }
  public NSArray<er.auth.model.ERPropertyPermission> propertyPermissions() {
    return (NSArray<er.auth.model.ERPropertyPermission>)storedValueForKey(_EREntityPermission.PROPERTY_PERMISSIONS_KEY);
  }

  public NSArray<er.auth.model.ERPropertyPermission> propertyPermissions(EOQualifier qualifier) {
    return propertyPermissions(qualifier, null, false);
  }

  public NSArray<er.auth.model.ERPropertyPermission> propertyPermissions(EOQualifier qualifier, boolean fetch) {
    return propertyPermissions(qualifier, null, fetch);
  }

  public NSArray<er.auth.model.ERPropertyPermission> propertyPermissions(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings, boolean fetch) {
    NSArray<er.auth.model.ERPropertyPermission> results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(er.auth.model.ERPropertyPermission.ENTITY_PERMISSION_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray<EOQualifier> qualifiers = new NSMutableArray<EOQualifier>();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = er.auth.model.ERPropertyPermission.clazz.objectsMatchingQualifier(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = propertyPermissions();
      if (qualifier != null) {
        results = (NSArray<er.auth.model.ERPropertyPermission>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<er.auth.model.ERPropertyPermission>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToPropertyPermissions(er.auth.model.ERPropertyPermission object) {
    includeObjectIntoPropertyWithKey(object, _EREntityPermission.PROPERTY_PERMISSIONS_KEY);
  }

  public void removeFromPropertyPermissions(er.auth.model.ERPropertyPermission object) {
    excludeObjectFromPropertyWithKey(object, _EREntityPermission.PROPERTY_PERMISSIONS_KEY);
  }

  public void addToPropertyPermissionsRelationship(er.auth.model.ERPropertyPermission object) {
    if (_EREntityPermission.LOG.isDebugEnabled()) {
      _EREntityPermission.LOG.debug("adding " + object + " to propertyPermissions relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToPropertyPermissions(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _EREntityPermission.PROPERTY_PERMISSIONS_KEY);
    }
  }

  public void removeFromPropertyPermissionsRelationship(er.auth.model.ERPropertyPermission object) {
    if (_EREntityPermission.LOG.isDebugEnabled()) {
      _EREntityPermission.LOG.debug("removing " + object + " from propertyPermissions relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromPropertyPermissions(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _EREntityPermission.PROPERTY_PERMISSIONS_KEY);
    }
  }

  public er.auth.model.ERPropertyPermission createPropertyPermissionsRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( er.auth.model.ERPropertyPermission.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _EREntityPermission.PROPERTY_PERMISSIONS_KEY);
    return (er.auth.model.ERPropertyPermission) eo;
  }

  public void deletePropertyPermissionsRelationship(er.auth.model.ERPropertyPermission object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _EREntityPermission.PROPERTY_PERMISSIONS_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllPropertyPermissionsRelationships() {
    Enumeration<er.auth.model.ERPropertyPermission> objects = propertyPermissions().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deletePropertyPermissionsRelationship(objects.nextElement());
    }
  }


}
