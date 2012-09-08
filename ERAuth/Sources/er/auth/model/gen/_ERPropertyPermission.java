// DO NOT EDIT.  Make changes to er.auth.model.ERPropertyPermission.java instead.
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
public abstract class _ERPropertyPermission extends er.extensions.eof.ERXGenericRecord {
  public static final String ENTITY_NAME = "ERPropertyPermission";

  // Attributes
  public static final ERXKey<Boolean> ALLOW_READ = new ERXKey<Boolean>("allowRead");
  public static final String ALLOW_READ_KEY = ALLOW_READ.key();
  public static final ERXKey<Boolean> ALLOW_UPDATE = new ERXKey<Boolean>("allowUpdate");
  public static final String ALLOW_UPDATE_KEY = ALLOW_UPDATE.key();
  public static final ERXKey<String> NAME_FOR_PROPERTY = new ERXKey<String>("nameForProperty");
  public static final String NAME_FOR_PROPERTY_KEY = NAME_FOR_PROPERTY.key();

  // Relationships
  public static final ERXKey<er.auth.model.EREntityPermission> ENTITY_PERMISSION = new ERXKey<er.auth.model.EREntityPermission>("entityPermission");
  public static final String ENTITY_PERMISSION_KEY = ENTITY_PERMISSION.key();

  public static class _ERPropertyPermissionClazz<T extends er.auth.model.ERPropertyPermission> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERPropertyPermission.class);

  public er.auth.model.ERPropertyPermission.ERPropertyPermissionClazz clazz() {
    return er.auth.model.ERPropertyPermission.clazz;
  }
  
  public Boolean allowRead() {
    return (Boolean) storedValueForKey(_ERPropertyPermission.ALLOW_READ_KEY);
  }

  public void setAllowRead(Boolean value) {
    if (_ERPropertyPermission.LOG.isDebugEnabled()) {
    	_ERPropertyPermission.LOG.debug( "updating allowRead from " + allowRead() + " to " + value);
    }
    takeStoredValueForKey(value, _ERPropertyPermission.ALLOW_READ_KEY);
  }

  public Boolean allowUpdate() {
    return (Boolean) storedValueForKey(_ERPropertyPermission.ALLOW_UPDATE_KEY);
  }

  public void setAllowUpdate(Boolean value) {
    if (_ERPropertyPermission.LOG.isDebugEnabled()) {
    	_ERPropertyPermission.LOG.debug( "updating allowUpdate from " + allowUpdate() + " to " + value);
    }
    takeStoredValueForKey(value, _ERPropertyPermission.ALLOW_UPDATE_KEY);
  }

  public String nameForProperty() {
    return (String) storedValueForKey(_ERPropertyPermission.NAME_FOR_PROPERTY_KEY);
  }

  public void setNameForProperty(String value) {
    if (_ERPropertyPermission.LOG.isDebugEnabled()) {
    	_ERPropertyPermission.LOG.debug( "updating nameForProperty from " + nameForProperty() + " to " + value);
    }
    takeStoredValueForKey(value, _ERPropertyPermission.NAME_FOR_PROPERTY_KEY);
  }

  public er.auth.model.EREntityPermission entityPermission() {
    return (er.auth.model.EREntityPermission)storedValueForKey(_ERPropertyPermission.ENTITY_PERMISSION_KEY);
  }
  
  public void setEntityPermission(er.auth.model.EREntityPermission value) {
    takeStoredValueForKey(value, _ERPropertyPermission.ENTITY_PERMISSION_KEY);
  }

  public void setEntityPermissionRelationship(er.auth.model.EREntityPermission value) {
    if (_ERPropertyPermission.LOG.isDebugEnabled()) {
      _ERPropertyPermission.LOG.debug("updating entityPermission from " + entityPermission() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setEntityPermission(value);
    }
    else if (value == null) {
    	er.auth.model.EREntityPermission oldValue = entityPermission();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERPropertyPermission.ENTITY_PERMISSION_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERPropertyPermission.ENTITY_PERMISSION_KEY);
    }
  }

}
