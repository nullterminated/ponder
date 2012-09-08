package er.auth.assignments;

import com.webobjects.appserver.WOSession;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eocontrol.EOClassDescription;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSArray;

import er.auth.model.ERRole;
import er.directtoweb.assignments.ERDAssignment;

public class ERAModelAuthorizationAssignment extends ERDAssignment {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;
	
	private static final NSArray<String> dependentKeys = new NSArray<String>("entity");

	public ERAModelAuthorizationAssignment(EOKeyValueUnarchiver u) {
		super(u);
	}

	public ERAModelAuthorizationAssignment(String key, Object value) {
		super(key, value);
	}

	public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver u) {
		return new ERAModelAuthorizationAssignment(u);
	}
	
	@Override
	public NSArray<String> dependentKeys(String keyPath) {
		if("authorizePropertyKeys".equals(keyPath)) {
			return dependentKeys;
		}
		return null;
	}
	
	public NSArray<String> authorizeRoleNames(D2WContext c) {
		WOSession session = (WOSession) c.valueForKey("session");
		EOEditingContext ec = session.defaultEditingContext();
		NSArray<ERRole> roles = ERRole.clazz.allObjects(ec);
		return ERRole.ROLE_NAME.arrayValueInObject(roles);
	}

	public NSArray<String> authorizePropertyKeys(D2WContext c) {
		EOEntity entity = c.entity();
		if(entity == null) { return NSArray.emptyArray(); }
		EOClassDescription cd = entity.classDescriptionForInstances();
		NSArray<String> propertyKeys = cd.attributeKeys();
		propertyKeys = propertyKeys.arrayByAddingObjectsFromArray(cd.toOneRelationshipKeys());
		propertyKeys = propertyKeys.arrayByAddingObjectsFromArray(cd.toManyRelationshipKeys());
		return propertyKeys;
	}
}
