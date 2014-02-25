// DO NOT EDIT.  Make changes to er.corebl.model.ERCAuditTrailEntry.java instead.
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
public abstract class _ERCAuditTrailEntry extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERCAuditTrailEntry";

  // Attributes
  public static final ERXKey<NSTimestamp> CREATED = new ERXKey<NSTimestamp>("created");
  public static final String CREATED_KEY = CREATED.key();
  public static final ERXKey<String> KEY_PATH = new ERXKey<String>("keyPath");
  public static final String KEY_PATH_KEY = KEY_PATH.key();
  public static final ERXKey<er.corebl.audittrail.ERCAuditTrailType> TYPE = new ERXKey<er.corebl.audittrail.ERCAuditTrailType>("type");
  public static final String TYPE_KEY = TYPE.key();
  public static final ERXKey<er.extensions.eof.ERXKeyGlobalID> USER_GLOBAL_ID = new ERXKey<er.extensions.eof.ERXKeyGlobalID>("userGlobalID");
  public static final String USER_GLOBAL_ID_KEY = USER_GLOBAL_ID.key();

  // Relationships
  public static final ERXKey<er.corebl.model.ERCAuditClob> NEW_CLOB = new ERXKey<er.corebl.model.ERCAuditClob>("newClob");
  public static final String NEW_CLOB_KEY = NEW_CLOB.key();
  public static final ERXKey<er.corebl.model.ERCAuditClob> OLD_CLOB = new ERXKey<er.corebl.model.ERCAuditClob>("oldClob");
  public static final String OLD_CLOB_KEY = OLD_CLOB.key();
  public static final ERXKey<er.corebl.model.ERCAuditTrail> TRAIL = new ERXKey<er.corebl.model.ERCAuditTrail>("trail");
  public static final String TRAIL_KEY = TRAIL.key();

  public static class _ERCAuditTrailEntryClazz<T extends er.corebl.model.ERCAuditTrailEntry> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERCAuditTrailEntry.class);

  public er.corebl.model.ERCAuditTrailEntry.ERCAuditTrailEntryClazz clazz() {
    return er.corebl.model.ERCAuditTrailEntry.clazz;
  }

  public NSTimestamp created() {
    return (NSTimestamp) storedValueForKey(_ERCAuditTrailEntry.CREATED_KEY);
  }

  public void setCreated(NSTimestamp value) {
    if (_ERCAuditTrailEntry.LOG.isDebugEnabled()) {
    	_ERCAuditTrailEntry.LOG.debug( "updating created from " + created() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCAuditTrailEntry.CREATED_KEY);
  }

  public String keyPath() {
    return (String) storedValueForKey(_ERCAuditTrailEntry.KEY_PATH_KEY);
  }

  public void setKeyPath(String value) {
    if (_ERCAuditTrailEntry.LOG.isDebugEnabled()) {
    	_ERCAuditTrailEntry.LOG.debug( "updating keyPath from " + keyPath() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCAuditTrailEntry.KEY_PATH_KEY);
  }

  public er.corebl.audittrail.ERCAuditTrailType type() {
    return (er.corebl.audittrail.ERCAuditTrailType) storedValueForKey(_ERCAuditTrailEntry.TYPE_KEY);
  }

  public void setType(er.corebl.audittrail.ERCAuditTrailType value) {
    if (_ERCAuditTrailEntry.LOG.isDebugEnabled()) {
    	_ERCAuditTrailEntry.LOG.debug( "updating type from " + type() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCAuditTrailEntry.TYPE_KEY);
  }

  public er.extensions.eof.ERXKeyGlobalID userGlobalID() {
    return (er.extensions.eof.ERXKeyGlobalID) storedValueForKey(_ERCAuditTrailEntry.USER_GLOBAL_ID_KEY);
  }

  public void setUserGlobalID(er.extensions.eof.ERXKeyGlobalID value) {
    if (_ERCAuditTrailEntry.LOG.isDebugEnabled()) {
    	_ERCAuditTrailEntry.LOG.debug( "updating userGlobalID from " + userGlobalID() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCAuditTrailEntry.USER_GLOBAL_ID_KEY);
  }

  public er.corebl.model.ERCAuditClob newClob() {
    return (er.corebl.model.ERCAuditClob)storedValueForKey(_ERCAuditTrailEntry.NEW_CLOB_KEY);
  }

  public void setNewClob(er.corebl.model.ERCAuditClob value) {
    takeStoredValueForKey(value, _ERCAuditTrailEntry.NEW_CLOB_KEY);
  }

  public void setNewClobRelationship(er.corebl.model.ERCAuditClob value) {
    if (_ERCAuditTrailEntry.LOG.isDebugEnabled()) {
      _ERCAuditTrailEntry.LOG.debug("updating newClob from " + newClob() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setNewClob(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCAuditClob oldValue = newClob();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCAuditTrailEntry.NEW_CLOB_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCAuditTrailEntry.NEW_CLOB_KEY);
    }
  }
  public er.corebl.model.ERCAuditClob oldClob() {
    return (er.corebl.model.ERCAuditClob)storedValueForKey(_ERCAuditTrailEntry.OLD_CLOB_KEY);
  }

  public void setOldClob(er.corebl.model.ERCAuditClob value) {
    takeStoredValueForKey(value, _ERCAuditTrailEntry.OLD_CLOB_KEY);
  }

  public void setOldClobRelationship(er.corebl.model.ERCAuditClob value) {
    if (_ERCAuditTrailEntry.LOG.isDebugEnabled()) {
      _ERCAuditTrailEntry.LOG.debug("updating oldClob from " + oldClob() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setOldClob(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCAuditClob oldValue = oldClob();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCAuditTrailEntry.OLD_CLOB_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCAuditTrailEntry.OLD_CLOB_KEY);
    }
  }
  public er.corebl.model.ERCAuditTrail trail() {
    return (er.corebl.model.ERCAuditTrail)storedValueForKey(_ERCAuditTrailEntry.TRAIL_KEY);
  }

  public void setTrail(er.corebl.model.ERCAuditTrail value) {
    takeStoredValueForKey(value, _ERCAuditTrailEntry.TRAIL_KEY);
  }

  public void setTrailRelationship(er.corebl.model.ERCAuditTrail value) {
    if (_ERCAuditTrailEntry.LOG.isDebugEnabled()) {
      _ERCAuditTrailEntry.LOG.debug("updating trail from " + trail() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setTrail(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCAuditTrail oldValue = trail();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCAuditTrailEntry.TRAIL_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCAuditTrailEntry.TRAIL_KEY);
    }
  }

}
