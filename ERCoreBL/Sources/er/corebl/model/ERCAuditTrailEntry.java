package er.corebl.model;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.webobjects.eoaccess.EOObjectNotAvailableException;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOKeyGlobalID;
import com.webobjects.foundation.NSPropertyListSerialization;
import com.webobjects.foundation.NSTimestamp;

import er.corebl.ERCoreBL;
import er.extensions.eof.ERXGenericRecord;
import er.extensions.eof.ERXKeyGlobalID;

public class ERCAuditTrailEntry extends er.corebl.model.eogen._ERCAuditTrailEntry {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERCAuditTrailEntry.class);

    public static final ERCAuditTrailEntryClazz<ERCAuditTrailEntry> clazz = new ERCAuditTrailEntryClazz<ERCAuditTrailEntry>();
    public static class ERCAuditTrailEntryClazz<T extends ERCAuditTrailEntry> extends er.corebl.model.eogen._ERCAuditTrailEntry._ERCAuditTrailEntryClazz<T> {
        /* more clazz methods here */
    }

    /**
     * Initialize the EO. This is called when an EO is created, not when it is
     * inserted into an EC.
     */
    public void init(EOEditingContext ec) {
        super.init(ec);
        EOEnterpriseObject user = ERCoreBL.actor(ec);
        if (user != null && user instanceof ERXGenericRecord) {
            ERXKeyGlobalID gid = ERXKeyGlobalID.globalIDForGID(((ERXGenericRecord) user).permanentGlobalID());
            setUserGlobalID(gid);
        }
        setCreated(new NSTimestamp());
    }

    public EOEnterpriseObject user() {
        if (userGlobalID() == null) {
            return null;
        }
        EOKeyGlobalID gid = userGlobalID().globalID();
        EOEnterpriseObject eo = editingContext().faultForGlobalID(gid, editingContext());
        try {
            eo.willRead();
            return eo;
        } catch (EOObjectNotAvailableException e) {
            return null;
        }
    }

    public void setOldValue(Object value) {
    	String val = NSPropertyListSerialization.stringFromPropertyList(value);
    	if(StringUtils.isBlank(val)) { return; }
    	ERCAuditClob values = ERCAuditClob.clazz.createAndInsertObject(editingContext());
    	values.setValuesString(val);
    	addObjectToBothSidesOfRelationshipWithKey(values, OLD_CLOB_KEY);
    }

    public void setNewValue(Object value) {
    	String val = NSPropertyListSerialization.stringFromPropertyList(value);
    	if(StringUtils.isBlank(val)) { return; }
    	ERCAuditClob values = ERCAuditClob.clazz.createAndInsertObject(editingContext());
    	values.setValuesString(val);
    	addObjectToBothSidesOfRelationshipWithKey(values, NEW_CLOB_KEY);
    }
}
