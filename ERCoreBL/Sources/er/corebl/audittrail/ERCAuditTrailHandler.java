package er.corebl.audittrail;

import java.util.Enumeration;

import org.apache.log4j.Logger;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOObjectStoreCoordinator;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation._NSUtilities;

import er.corebl.model.ERCAuditTrail;
import er.corebl.model.ERCAuditTrailEntry;
import er.extensions.eof.ERXConstant;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXEOAccessUtilities;
import er.extensions.eof.ERXGenericRecord;
import er.extensions.eof.ERXKeyGlobalID;
import er.extensions.eof.ERXModelGroup;
import er.extensions.foundation.ERXPatcher;
import er.extensions.foundation.ERXProperties;
import er.extensions.foundation.ERXSelectorUtilities;
import er.extensions.foundation.ERXValueUtilities;

/**
 *
 * @property er.corebusinesslogic.ERCAuditTrailClassName allow sub-classing of the audit trail handler
 *
 * @author nullterminated
 */
public class ERCAuditTrailHandler {
	private static final Logger log = Logger.getLogger(ERCAuditTrailHandler.class);

	private static final ERCAuditTrailHandler _handler;

	/**
	 * The userInfo key for audited model objects.
	 */
	public static final String ERXAUDIT_KEYS = "ERXAuditKeys";

	public static final String AUDIT_CLASS_NAME_PROPERTY = "er.corebusinesslogic.ERCAuditTrailClassName";

	protected NSMutableDictionary<String, Configuration> configuration = new NSMutableDictionary<String, Configuration>();

	public class Configuration {
		public boolean isAudited = false;

		public NSMutableArray<String> keys = new NSMutableArray<String>();

		public NSMutableArray<String> notificationKeys = new NSMutableArray<String>();

		@Override
		public String toString() {
			return "{ isAudited =" + isAudited + "; keys = " + keys + "; notificationKeys = " + notificationKeys + ";}";
		}
	}

	static {
		String defaultClassName = ERCAuditTrailHandler.class.getName();
		String className = ERXProperties.stringForKeyWithDefault(AUDIT_CLASS_NAME_PROPERTY, defaultClassName);
		Class<?> c = ERXPatcher.classForName(className);
		Object handler = _NSUtilities.instantiateObject(c, new Class[] {}, new Object[] {}, true, false);
		_handler = (ERCAuditTrailHandler) handler;
		NSSelector<Void> sel = ERXSelectorUtilities.notificationSelector("modelGroupDidLoad");
		NSNotificationCenter nc = NSNotificationCenter.defaultCenter();
		nc.addObserver(_handler, sel, ERXModelGroup.ModelGroupAddedNotification, null);
	}
	
	public static void init() {
		//Triggers the static initialization
		log.info("Audit trail handler initialized.");
	}

	public void modelGroupDidLoad(NSNotification n) {
		configuration.removeAllObjects();
		EOModelGroup group = (EOModelGroup) n.object();
		NSArray<EOModel> models = group.models();
		for (EOModel model : models) {
			NSArray<EOEntity> entities = model.entities();
			for (EOEntity entity : entities) {
				if (entity.userInfo() != null && entity.userInfo().objectForKey(ERXAUDIT_KEYS) != null) {
					configureEntity(entity);
				}
			}
		}
		log.info("Configuration : " + configuration);
		NSNotificationCenter.defaultCenter().removeObserver(_handler, ERXModelGroup.ModelGroupAddedNotification, null);
		NSSelector<Void> sel = ERXSelectorUtilities.notificationSelector("handleSave");
		NSNotificationCenter nc = NSNotificationCenter.defaultCenter();
		nc.addObserver(_handler, sel, ERXEC.EditingContextWillSaveChangesNotification, null);
	}

	protected Configuration configureEntity(EOEntity entity) {
		Configuration config = configuration.objectForKey(entity.name());
		if (config == null) {
			config = new Configuration();
			configuration.setObjectForKey(config, entity.name());
		}

		if (entity.userInfo() != null && entity.userInfo().objectForKey(ERXAUDIT_KEYS) != null) {
			String val = entity.userInfo().objectForKey(ERXAUDIT_KEYS).toString();
			NSArray<String> keys = null;

			if (val.length() == 0) {
				keys = entity.classDescriptionForInstances().attributeKeys();
			} else {
				keys = ERXValueUtilities.arrayValue(val);
			}
			config.isAudited = true;
			config.keys.addObjectsFromArray(keys);
			for (int i = 0, count = config.keys.count(); i < count; ++i) {
				String key = config.keys.objectAtIndex(i);
				EOEntity source = entity;
				NSArray<String> parts = NSArray.componentsSeparatedByString(key, ".");
				for (int i2 = 0, count2 = parts.count(); i2 < count2; ++i2) {
					String part = parts.objectAtIndex(i2);
					EORelationship rel = source._relationshipForPath(part);
					if (rel != null) {
						if (rel.isFlattened()) {
							// AK: for now this only handles non-flattened rels
							String message = "Can't handle flattened relations, use the definition: " + rel;
							throw new IllegalStateException(message);
						}
						if (rel.isToMany()) {
							EOEntity destinationEntity = rel.destinationEntity();
							Configuration destinationConfiguration = configureEntity(destinationEntity);
							/*
							 * CHECKME should this check the entity class
							 * description for inverse key? ERXInverseKey
							 */
							String inverseName = rel.anyInverseRelationship().name();
							destinationConfiguration.notificationKeys.addObject(inverseName);
							source = destinationEntity;
						} else {
							config.keys.addObject(rel.name());
							source = rel.destinationEntity();
						}
					}
				}
			}
		}
		return config;
	}

	public void handleSave(NSNotification n) {
		// If nothing is configured to be audited, just return
		if (configuration.isEmpty()) {
			return;
		}
		EOEditingContext ec = (EOEditingContext) n.object();
		// Don't create trails for nested ECs
		if (ec.parentObjectStore() instanceof EOObjectStoreCoordinator) {
			ec.processRecentChanges();
			NSArray<EOEnterpriseObject> insertedObjects = ec.insertedObjects().immutableClone();
			// Delete any inserted audit trails as this indicates a previous
			// save failed.
			for (EOEnterpriseObject eo : insertedObjects) {
				if (ERCAuditTrailEntry.clazz.entityName().equals(eo.entityName())) {
					ec.deleteObject(eo);
				}
				if (ERCAuditTrail.clazz.entityName().equals(eo.entityName())) {
					ec.deleteObject(eo);
				}
			}
			// Process any deleted trail entries
			ec.processRecentChanges();

			NSArray<EOEnterpriseObject> updatedObjects = ec.updatedObjects();
			NSArray<EOEnterpriseObject> deletedObjects = ec.deletedObjects();

			/*
			 * CHECKME looks like this would audit the deleted audit trails :-P
			 * Should just return harmlessly as there should be no audit config
			 * for the audit trails anyway though.
			 */
			handleSave(ec, EOEditingContext.InsertedKey, insertedObjects);
			handleSave(ec, EOEditingContext.UpdatedKey, updatedObjects);
			handleSave(ec, EOEditingContext.DeletedKey, deletedObjects);
		}
	}

	protected void handleSave(EOEditingContext ec, String typeKey, NSArray<EOEnterpriseObject> objects) {
		// CHECKME is this check really necessary?
		if (objects == null) {
			return;
		}
		for (EOEnterpriseObject eo : objects) {
			Configuration config = configuration.objectForKey(eo.entityName());

			if (config != null) {
				if (config.isAudited) {
					handleSave(ec, typeKey, eo);
				} else {
					for (String key : config.notificationKeys) {
						EOEnterpriseObject target = (EOEnterpriseObject) eo.valueForKey(key);
						EOEntity entity = ERXEOAccessUtilities.entityForEo(eo);
						/*
						 * CHECKME same as above. Check class description for
						 * inverse?
						 */
						String inverse = entity.relationshipNamed(key).anyInverseRelationship().name();
						if (typeKey.equals(EOEditingContext.UpdatedKey)) {
							handleUpdate(ec, target, inverse, eo);
						} else if (typeKey.equals(EOEditingContext.InsertedKey)) {
							handleAdd(ec, target, inverse, eo);
						} else if (typeKey.equals(EOEditingContext.DeletedKey)) {
							target = (EOEnterpriseObject) ec.committedSnapshotForObject(eo).valueForKey(key);
							handleRemove(ec, target, inverse, eo);
						}
					}
				}
			}
		}
	}

	private void handleSave(EOEditingContext ec, String typeKey, EOEnterpriseObject eo) {
		if (typeKey.equals(EOEditingContext.UpdatedKey)) {
			handleUpdate(ec, eo);
		} else if (typeKey.equals(EOEditingContext.InsertedKey)) {
			handleInsert(ec, eo, serializeObject(eo));
		} else if (typeKey.equals(EOEditingContext.DeletedKey)) {
			handleDelete(ec, eo, serializeObject(eo));
		}
	}

	protected NSDictionary<String,Object> serializeObject(EOEnterpriseObject eo) {
		NSMutableDictionary<String, Object> result = new NSMutableDictionary<String, Object>();
		result.addEntriesFromDictionary(eo.snapshot());
		for (String key : eo.snapshot().allKeys()) {
			Object value = result.objectForKey(key);
			if (value instanceof ERXConstant.Constant) {
				ERXConstant.Constant constant = (ERXConstant.Constant) value;
				result.setObjectForKey(constant.value(), key);
			} else if (value == NSKeyValueCoding.NullValue) {
				result.removeObjectForKey(key);
			} else if (value instanceof ERXGenericRecord) {
				ERXGenericRecord rec = (ERXGenericRecord) value;
				result.setObjectForKey(ERXKeyGlobalID.globalIDForGID(rec.permanentGlobalID()).asString(), key);
			} else if (value instanceof NSArray) {
				/*
				 * CHECKME Doesn't look like this would work with array
				 * attributes. Check to see if key is a toMany relationship
				 * first?
				 */
				NSArray oldValue = (NSArray) value;
				NSMutableArray newValue = new NSMutableArray(oldValue.count());
				for (Enumeration e1 = newValue.objectEnumerator(); e1.hasMoreElements();) {
					ERXGenericRecord rec = (ERXGenericRecord) e1.nextElement();
					newValue.addObject(ERXKeyGlobalID.globalIDForGID(rec.permanentGlobalID()).asString());
				}
				result.setObjectForKey(newValue, key);
			}
		}
		return result;
	}

    protected void handleUpdate(EOEditingContext ec, EOEnterpriseObject eo) {
        NSArray<String> keys = configuration.objectForKey(eo.entityName()).keys;
        NSDictionary<String,Object> committedSnapshotForObject = ec.committedSnapshotForObject(eo);
        NSDictionary<String,Object> changes = eo.changesFromSnapshot(committedSnapshotForObject);
        for (String key : changes.allKeys()) {
            if (keys.containsObject(key)) {
                handleUpdate(ec, eo, key, committedSnapshotForObject.objectForKey(key), changes.objectForKey(key));
            }
        }
    }

	protected void handleInsert(EOEditingContext ec, EOEnterpriseObject eo, Object newValue) {
		handleChange(ec, eo, ERCAuditTrailType.INSERTED, null, null, newValue);
	}

	protected void handleUpdate(EOEditingContext ec, EOEnterpriseObject eo, String keyPath, Object oldValue,
			Object newValue) {
		handleChange(ec, eo, ERCAuditTrailType.UPDATED, keyPath, oldValue, newValue);
	}

	protected void handleDelete(EOEditingContext ec, EOEnterpriseObject eo, Object oldValue) {
		handleChange(ec, eo, ERCAuditTrailType.DELETED, null, oldValue, null);
	}

	protected void handleRemove(EOEditingContext ec, EOEnterpriseObject target, String keyPath, EOEnterpriseObject eo) {
		if(!(target instanceof ERXGenericRecord)) {
			throw new IllegalArgumentException("Target must be an ERXGenericRecord");
		}
		handleChange(ec, target, ERCAuditTrailType.REMOVED, keyPath, serializeObject(eo), null);
	}

	protected void handleAdd(EOEditingContext ec, EOEnterpriseObject target, String keyPath, EOEnterpriseObject eo) {
		if(!(target instanceof ERXGenericRecord)) {
			throw new IllegalArgumentException("Target must be an ERXGenericRecord");
		}
		handleChange(ec, target, ERCAuditTrailType.ADDED, keyPath, null, serializeObject(eo));
	}

	protected void handleUpdate(EOEditingContext ec, EOEnterpriseObject target, String keyPath, EOEnterpriseObject eo) {
		ERXGenericRecord rec = (ERXGenericRecord) target;
		EOEnterpriseObject oldValue = (EOEnterpriseObject) rec.valueForKeyPath(keyPath);
		handleChange(ec, target, ERCAuditTrailType.UPDATED, keyPath, oldValue, oldValue);
	}

	protected void handleChange(EOEditingContext ec, EOEnterpriseObject eo, ERCAuditTrailType type, String keyPath,
			Object oldValue, Object newValue) {
		ERXGenericRecord rec = (ERXGenericRecord) eo;
		ERCAuditTrail trail = ERCAuditTrail.clazz.auditTrailForObject(ec, eo);
		if (trail == null) {
			trail = ERCAuditTrail.clazz.createAuditTrailForObject(ec, eo);
		}
		log.info(trail + " " + type + ": " + rec.permanentGlobalID() + " " + keyPath + " from " + oldValue + " to "
				+ newValue);
		if (oldValue instanceof ERXGenericRecord) {
			ERXGenericRecord rec1 = (ERXGenericRecord) oldValue;
			oldValue = ERXKeyGlobalID.globalIDForGID(rec1.permanentGlobalID()).asString();
		}
		if (newValue instanceof ERXGenericRecord) {
			ERXGenericRecord rec1 = (ERXGenericRecord) newValue;
			newValue = ERXKeyGlobalID.globalIDForGID(rec1.permanentGlobalID()).asString();
		}
		trail.createEntry(type, keyPath, oldValue, newValue);
	}
}
