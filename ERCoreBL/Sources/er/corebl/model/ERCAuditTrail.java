package er.corebl.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.eocontrol.EOKeyGlobalID;
import com.webobjects.eocontrol.EOQualifier;

import er.corebl.audittrail.ERCAuditTrailType;
import er.extensions.eof.ERXGenericRecord;
import er.extensions.eof.ERXKeyGlobalID;
import er.extensions.eof.ERXQ;

public class ERCAuditTrail extends er.corebl.model.eogen._ERCAuditTrail {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(ERCAuditTrail.class);

	public static final ERCAuditTrailClazz<ERCAuditTrail> clazz = new ERCAuditTrailClazz<ERCAuditTrail>();

	public static class ERCAuditTrailClazz<T extends ERCAuditTrail> extends
			er.corebl.model.eogen._ERCAuditTrail._ERCAuditTrailClazz<T> {
		/* more clazz methods here */
		public ERCAuditTrail auditTrailForObject(EOEditingContext ec, EOEnterpriseObject eo) {
			EOKeyGlobalID gid = null;
			if (eo instanceof ERXGenericRecord) {
				gid = ((ERXGenericRecord) eo).permanentGlobalID();
			} else {
				throw new IllegalArgumentException("Can't handle non ERXGenericRecord");
			}

			ERCAuditTrail trail = objectMatchingKeyAndValue(ec, GID_KEY, ERXKeyGlobalID.globalIDForGID(gid));
			if (trail == null) {
				trail = (ERCAuditTrail) EOQualifier.filteredArrayWithQualifier(
						ec.insertedObjects(),
						ERXQ.equals("entityName", ENTITY_NAME).and(
								ERXQ.equals(GID_KEY + ".globalID", gid))).lastObject();
			}
			return trail;
		}

		// public ERCAuditTrail auditTrailForGlobalID(EOEditingContext ec,
		// EOGlobalID gid) {
		// throw new
		// IllegalArgumentException("Can't handle non ERXGenericRecord");
		// }

		public ERCAuditTrail createAuditTrailForObject(EOEditingContext ec, EOEnterpriseObject eo) {
			ERCAuditTrail trail = createAndInsertObject(ec);

			trail.setObject(eo);
			return trail;
		}
	}

	/**
	 * Initialize the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
		setIsDeleted(Boolean.FALSE);
	}

	public void setObject(EOEnterpriseObject eo) {
		EOGlobalID gid;
		if (gid() == null) {
			if (eo instanceof ERXGenericRecord) {
				gid = ((ERXGenericRecord) eo).permanentGlobalID();
			} else {
				throw new IllegalArgumentException("Can't handle non ERXGenericRecord");
			}
			ERXKeyGlobalID keyGID = ERXKeyGlobalID.globalIDForGID((EOKeyGlobalID) gid);
			setGid(keyGID);
		} else {
			log.warn("Attempting to set object on audit trail after it is already set.");
		}
	}

	public EOEnterpriseObject object() {
		EOKeyGlobalID gid = gid().globalID();
		return editingContext().faultForGlobalID(gid, editingContext());
	}

	public void createEntry(ERCAuditTrailType type, String keyPath, Object oldValue, Object newValue) {
		ERCAuditTrailEntry entry = ERCAuditTrailEntry.clazz.createAndInsertObject(editingContext());
		entry.setTrail(this);
		addToEntries(entry);
		entry.setKeyPath(keyPath);
		entry.setType(type);
		entry.setOldValue(oldValue);
		entry.setNewValue(newValue);
		if (type == ERCAuditTrailType.DELETED) {
			setIsDeleted(true);
		}
	}
}
