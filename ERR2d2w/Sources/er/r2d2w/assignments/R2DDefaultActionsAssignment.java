package er.r2d2w.assignments;

import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.D2WModel;
import com.webobjects.eocontrol.EOKeyValueArchiver;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.directtoweb.assignments.ERDAssignment;
import er.extensions.foundation.ERXValueUtilities;

public class R2DDefaultActionsAssignment extends ERDAssignment {

	public R2DDefaultActionsAssignment(EOKeyValueUnarchiver u) {
		super(u);
	}

	public R2DDefaultActionsAssignment(String key, Object value) {
		super(key, value);
	}

	public void encodeWithKeyValueArchiver(EOKeyValueArchiver archiver) {
		super.encodeWithKeyValueArchiver(archiver);
	}

	public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver unarchiver) {
		return new R2DDefaultActionsAssignment(unarchiver);
	}

	public NSArray<Object> dependentKeys(String keyPath) {
		return new NSArray<Object>(new Object[] { D2WModel.EntityKey, D2WModel.TaskKey, "isEntityDeletable", "isEntityEditable", "isEntityInspectable", "readOnly" });
	}
	
	public Object tableActions(D2WContext c) {
		NSMutableArray<String> result = new NSMutableArray<String>();
		
		if(c.task().equals("list")) {
			Boolean canInspect = ERXValueUtilities.booleanValue(c.valueForKey("isEntityInspectable"));
			if(canInspect) { result.add("inspectAction"); }
			Boolean canEdit = ERXValueUtilities.booleanValue(c.valueForKey("isEntityEditable"));
			if(canEdit) { result.add("editAction"); }
			Boolean canDelete = ERXValueUtilities.booleanValue(c.valueForKey("isEntityDeletable"));
			if(canDelete) { result.add("deleteAction"); }
			
		} else if (c.task().equals("select")) {
			Boolean canInspect = ERXValueUtilities.booleanValue(c.valueForKey("isEntityInspectable"));
			if(canInspect) { result.add("inspectAction"); }
			result.add("selectAction");
		}
		return result;
	}

}
