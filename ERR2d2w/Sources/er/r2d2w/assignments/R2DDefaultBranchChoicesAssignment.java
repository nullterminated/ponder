package er.r2d2w.assignments;

import com.webobjects.directtoweb.D2WContext;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

import er.directtoweb.assignments.ERDAssignment;
import er.extensions.eof.ERXGuardedObjectInterface;
import er.extensions.foundation.ERXValueUtilities;

/**
 * A delayed assignment for object related actions.
 */
public class R2DDefaultBranchChoicesAssignment extends ERDAssignment {

	public static final NSArray<String> leftControllerDependentKeys = 
		new NSArray<String>("task","isEntityInspectable");
	public static final NSArray<String> rightControllerDependentKeys = 
		new NSArray<String>("task","isEntityDeletable","isEntityEditable","object.canDelete","object.canUpdate","readOnly");
	public static final NSArray<String> toManyRelationshipDependentKeys =
		new NSArray<String>("task","parentRelationship","frame","isEntityEditable","readOnly");
	public static final NSArray<String> toOneRelationshipDependentKeys =
		new NSArray<String>("task","parentRelationship","frame","isEntityDeletable","isEntityEditable","isEntityInspectable","readOnly","object.canDelete","object.canUpdate");
	public static final NSArray<String> inspectControllerDependentKeys = 
		new NSArray<String>("task","frame","isEntityDeletable","isEntityEditable","readOnly","object.canDelete","object.canUpdate");
	public static final NSArray<String> editControllerDependentKeys = 
		new NSArray<String>("task");
	public static final NSArray<String> listControllerDependentKeys = 
		new NSArray<String>("task","frame");
	public static final NSArray<String> queryControllerDependentKeys = 
		new NSArray<String>("task","frame");
	public static final NSArray<String> queryAllControllerDependentKeys = 
		new NSArray<String>("task","frame");
	
	public static final NSDictionary<String, NSArray<String>> dependentKeys;
	
	static {
		NSMutableDictionary<String, NSArray<String>> keys = new NSMutableDictionary<String, NSArray<String>>();
		keys.setObjectForKey(leftControllerDependentKeys, "leftControllerChoices");
		keys.setObjectForKey(rightControllerDependentKeys, "rightControllerChoices");
		keys.setObjectForKey(toManyRelationshipDependentKeys, "toManyControllerChoices");
		keys.setObjectForKey(toOneRelationshipDependentKeys, "toOneControllerChoices");
		keys.setObjectForKey(inspectControllerDependentKeys, "inspectControllerChoices");
		keys.setObjectForKey(editControllerDependentKeys, "editControllerChoices");
		keys.setObjectForKey(listControllerDependentKeys, "listControllerChoices");
		keys.setObjectForKey(queryControllerDependentKeys, "queryControllerChoices");
		keys.setObjectForKey(queryAllControllerDependentKeys, "queryAllControllerChoices");
		dependentKeys = keys.immutableClone();
	}
	
	public R2DDefaultBranchChoicesAssignment(EOKeyValueUnarchiver u) {super(u);}
	public R2DDefaultBranchChoicesAssignment(String key, Object value) {super(key, value);}
	public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver unarchiver) {
		return new R2DDefaultBranchChoicesAssignment(unarchiver);
	}

	@Override
	public NSArray<String> dependentKeys(String keyPath) {
		return dependentKeys.objectForKey(keyPath);
	}
	
	public Object leftControllerChoices(D2WContext c) {
		NSMutableArray<String> choices = new NSMutableArray<String>();
		boolean isEntityInspectable = ERXValueUtilities.booleanValue(c.valueForKey("isEntityInspectable"));
		String task = c.task();
		
		if("select".equals(task)) {
			choices.add("_select");
		}
		if(isEntityInspectable){
			String choice = "editRelationship".equals(task)?"_inspectRelated":"_inspect";
			choices.add(choice);
		}
		return choices;
	}
	
	public Object rightControllerChoices(D2WContext c) {
		NSMutableArray<String> choices = new NSMutableArray<String>();
		EOEnterpriseObject eo = (EOEnterpriseObject)c.valueForKey("object");
		EORelationship rel = (EORelationship)c.valueForKey("parentRelationship");
		boolean isEntityEditable = ERXValueUtilities.booleanValue(c.valueForKey("isEntityEditable"));
		boolean isEntityDeletable = ERXValueUtilities.booleanValue(c.valueForKey("isEntityDeletable"));
		boolean readOnly = ERXValueUtilities.booleanValue(c.valueForKey("readOnly"));
		String task = c.task();
		
		if(!readOnly && isEntityEditable && eo instanceof ERXGuardedObjectInterface?((ERXGuardedObjectInterface)eo).canUpdate():true) {
			if("editRelationship".equals(task)) {
				choices.add("_editRelated");
			} else if(!"edit".equals(task)) {
				choices.add("_edit");
			}
		}
		if("editRelationship".equals(task) && !rel.ownsDestination()) {
			choices.add("_removeRelated");
		}
		if(!readOnly && isEntityDeletable && eo instanceof ERXGuardedObjectInterface?((ERXGuardedObjectInterface)eo).canDelete():true) {
			if("edit".equals(task) || "inspect".equals(task)) {
				choices.add("_deleteReturn");
			} else {
				choices.add("_delete");
			}
		}
		return choices;
	}
	
	public Object toManyControllerChoices(D2WContext c) {
		NSMutableArray<String> choices = new NSMutableArray<String>();
		EORelationship rel = (EORelationship)c.valueForKey("parentRelationship");
		boolean isEntityEditable = ERXValueUtilities.booleanValue(c.valueForKey("isEntityEditable"));
		boolean readOnly = ERXValueUtilities.booleanValue(c.valueForKey("readOnly"));
		if(!c.frame()) {
			choices.add("_returnRelated");
		}
		if(rel.inverseRelationship() == null || !readOnly ) {
			choices.add("_queryRelated");
		}
		if(!readOnly && isEntityEditable) {
			choices.add("_createRelated");
		}
		return choices;
	}
	
	public Object toOneControllerChoices(D2WContext c) {
		NSMutableArray<String> choices = new NSMutableArray<String>();
		EOEnterpriseObject eo = (EOEnterpriseObject)c.valueForKey("object");
		EORelationship rel = (EORelationship)c.valueForKey("parentRelationship");
		boolean isEntityEditable = ERXValueUtilities.booleanValue(c.valueForKey("isEntityEditable"));
		boolean isEntityDeletable = ERXValueUtilities.booleanValue(c.valueForKey("isEntityDeletable"));
		boolean isEntityInspectable = ERXValueUtilities.booleanValue(c.valueForKey("isEntityInspectable"));
		boolean readOnly = ERXValueUtilities.booleanValue(c.valueForKey("readOnly"));

		if(!c.frame()) {
			choices.add("_returnRelated");
		}
		if(rel.inverseRelationship() == null || !readOnly ) {
			choices.add("_queryRelated");
		}
		if(isEntityInspectable) {
			choices.add("_inspectRelated");
		}
		if(!readOnly && isEntityEditable && eo instanceof ERXGuardedObjectInterface?((ERXGuardedObjectInterface)eo).canUpdate():true) {
			choices.add("_createRelated");
			choices.add("_editRelated");
			if(!rel.ownsDestination()) {
				choices.add("_removeRelated");
			}
		}
		if(!readOnly && isEntityDeletable && eo instanceof ERXGuardedObjectInterface?((ERXGuardedObjectInterface)eo).canDelete():true) {
			choices.add("_delete");
		}
		return choices;
	}
	
	public Object inspectControllerChoices(D2WContext c) {
		NSMutableArray<String> choices = new NSMutableArray<String>();
		EOEnterpriseObject eo = (EOEnterpriseObject)c.valueForKey("object");
		boolean isEntityEditable = ERXValueUtilities.booleanValue(c.valueForKey("isEntityEditable"));
		boolean isEntityDeletable = ERXValueUtilities.booleanValue(c.valueForKey("isEntityDeletable"));
		boolean readOnly = ERXValueUtilities.booleanValue(c.valueForKey("readOnly"));

		if(!c.frame()) {
			choices.add("_return");
		}
		if(!readOnly && isEntityEditable && eo instanceof ERXGuardedObjectInterface?((ERXGuardedObjectInterface)eo).canUpdate():true) {
			choices.add("_edit");
		}
		if(!readOnly && isEntityDeletable && eo instanceof ERXGuardedObjectInterface?((ERXGuardedObjectInterface)eo).canDelete():true) {
			choices.add("_deleteReturn");
		}
		return choices;
	}

	public Object editControllerChoices(D2WContext c) {
		return new NSArray<String>("_cancelEdit","_save");
	}

	public Object listControllerChoices(D2WContext c) {
		if(c.frame()) { return NSArray.emptyArray(); }
		return new NSArray<String>("_return");
	}

	public Object queryControllerChoices(D2WContext c) {
		if(c.frame()) { return new NSArray<String>("_query"); }
		return new NSArray<String>("_return","_query");
	}

	public Object queryAllControllerChoices(D2WContext c) {
		if(c.frame()) { return NSArray.emptyArray(); }
		return new NSArray<String>("_return");
	}
}
