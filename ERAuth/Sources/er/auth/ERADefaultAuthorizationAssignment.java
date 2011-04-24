package er.auth;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOSession;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.D2WModel;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSMutableArray;

import er.directtoweb.assignments.ERDAssignment;
import er.directtoweb.assignments.delayed.ERDDelayedAssignment;
import er.extensions.eof.ERXGuardedObjectInterface;
import er.extensions.foundation.ERXValueUtilities;

/**
 * A delayed assignment for object related actions.
 */
public class ERADefaultAuthorizationAssignment extends ERDDelayedAssignment {
	private static final Logger log = Logger.getLogger(ERADefaultAuthorizationAssignment.class);

	public ERADefaultAuthorizationAssignment(EOKeyValueUnarchiver u) {
		super(u);
	}

	public ERADefaultAuthorizationAssignment(String key, Object value) {
		super(key, value);
	}

	public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver unarchiver) {
		return new ERADefaultAuthorizationAssignment(unarchiver);
	}

	public Object fireNow(D2WContext c) {
		Object result = null;
		try {
			Method m = getClass().getMethod(keyPath(), ERDAssignment.D2WContextClassArray);
			result = m.invoke(this, new Object[] { c });
		} catch (Exception e) {
			throw NSForwardException._runtimeExceptionForThrowable(e);
		}
		return result;
	}

	public EOEnterpriseObject object(D2WContext c) {
		return (EOEnterpriseObject) c.valueForKey("object");
	}

	public CRUDAuthorization auth(D2WContext c) {
		return (CRUDAuthorization)c.valueForKey("crudAuthorization");
	}

	private Class<?> classForEntity(EOEntity entity) {
		try {
			return Class.forName(entity.className());
		} catch (ClassNotFoundException e) {
			throw NSForwardException._runtimeExceptionForThrowable(e);
		}
	}

	public Object leftControllerChoices(D2WContext c) {
		NSMutableArray<String> choices = new NSMutableArray<String>();
		if ("select".equals(c.task())) {
			choices.add("_select");
		}
		if (CRUDAuthorization.CAN_READ.invoke(auth(c), object(c))) {
			String choice = "editRelationship".equals(c.task()) ? "_inspectRelated" : "_inspect";
			choices.add(choice);
		}
		return choices;
	}

	public Object rightControllerChoices(D2WContext c) {
		NSMutableArray<String> choices = new NSMutableArray<String>();
		EOEnterpriseObject eo = object(c);
		EOEntity e = c.entity();
		boolean unguarded = !ERXGuardedObjectInterface.class.isAssignableFrom(classForEntity(e));
		boolean canUpdate = eo instanceof ERXGuardedObjectInterface ? ((ERXGuardedObjectInterface) eo).canUpdate()
				: unguarded;
		boolean canDelete = eo instanceof ERXGuardedObjectInterface ? ((ERXGuardedObjectInterface) eo).canDelete()
				: unguarded;
		boolean isEntityWritable = !e.isReadOnly() && !ERXValueUtilities.booleanValue(c.valueForKey("readOnly"));
		String task = c.task();
		CRUDAuthorization auth = auth(c);

		if (isEntityWritable && CRUDAuthorization.CAN_UPDATE.invoke(auth, eo) && canUpdate) {
			if ("editRelationship".equals(task)) {
				choices.add("_editRelated");
			} else if (!"edit".equals(task)) {
				choices.add("_edit");
			}
		}
		if ("editRelationship".equals(task)) {
			EORelationship rel = (EORelationship) c.valueForKey("parentRelationship");
			if(rel == null) {
				choices.add("_removeRelated");
			} else {
				boolean owns = rel.ownsDestination();
				EORelationship inverse = rel.inverseRelationship();
				if(owns || (inverse != null && inverse.isMandatory() && !inverse.isToMany())) {
					choices.add("_removeRelated");
				}
			}

		}
		if (isEntityWritable && CRUDAuthorization.CAN_DELETE.invoke(auth, eo) && canDelete) {
			choices.add("_delete");
		}
		return choices;
	}

	public Object toManyControllerChoices(D2WContext c) {
		NSMutableArray<String> choices = new NSMutableArray<String>();
		EORelationship rel = (EORelationship) c.valueForKey("parentRelationship");
		EOEntity e = c.entity();
		boolean isEntityWritable = !e.isReadOnly() && !ERXValueUtilities.booleanValue(c.valueForKey("readOnly"));
		boolean isConcrete = !e.isAbstractEntity();

		if (!c.frame()) {
			choices.add("_returnRelated");
		}
		CRUDAuthorization auth = auth(c);
		if(rel != null) {
			boolean canQuery = CRUDAuthorization.CAN_QUERY.invoke(auth, rel.destinationEntity().name());
			if (!rel.ownsDestination() && canQuery) {
				choices.add("_queryRelated");
			}
		}
		if (isEntityWritable && CRUDAuthorization.CAN_CREATE.invoke(auth, e.name()) && isConcrete && e.subEntities().isEmpty()) {
			choices.add("_createRelated");
		}
		return choices;
	}

	public Object toOneControllerChoices(D2WContext c) {
		NSMutableArray<String> choices = new NSMutableArray<String>();
		EOEnterpriseObject eo = object(c);
		EORelationship rel = (EORelationship) c.valueForKey("parentRelationship");
		EOEntity e = c.entity();
		boolean unguarded = !ERXGuardedObjectInterface.class.isAssignableFrom(classForEntity(e));
		boolean canUpdate = eo instanceof ERXGuardedObjectInterface ? ((ERXGuardedObjectInterface) eo).canUpdate()
				: unguarded;
		boolean canDelete = eo instanceof ERXGuardedObjectInterface ? ((ERXGuardedObjectInterface) eo).canDelete()
				: unguarded;
		boolean isEntityWritable = !e.isReadOnly() && !ERXValueUtilities.booleanValue(c.valueForKey("readOnly"));
		boolean isConcrete = !e.isAbstractEntity();
		CRUDAuthorization auth = auth(c);

		if (!c.frame()) {
			choices.add("_returnRelated");
		}
		if (rel.inverseRelationship() == null || isEntityWritable) {
			choices.add("_queryRelated");
		}
		if (eo != null && CRUDAuthorization.CAN_READ.invoke(auth, eo)) {
			choices.add("_inspectRelated");
		}
		if (isEntityWritable && CRUDAuthorization.CAN_CREATE.invoke(auth, e.name()) && isConcrete && e.subEntities().isEmpty()) {
			choices.add("_createRelated");
		}
		if (isEntityWritable && CRUDAuthorization.CAN_UPDATE.invoke(auth, eo) && canUpdate) {
			choices.add("_editRelated");
			if (!rel.ownsDestination()) {
				choices.add("_removeRelated");
			}
		}
		if (isEntityWritable && CRUDAuthorization.CAN_DELETE.invoke(auth, eo) && canDelete) {
			choices.add("_delete");
		}
		return choices;
	}

	public Object inspectControllerChoices(D2WContext c) {
		NSMutableArray<String> choices = new NSMutableArray<String>();
		EOEnterpriseObject eo = object(c);
		EOEntity e = c.entity();
		boolean unguarded = !ERXGuardedObjectInterface.class.isAssignableFrom(classForEntity(e));
		boolean canUpdate = eo instanceof ERXGuardedObjectInterface ? ((ERXGuardedObjectInterface) eo).canUpdate()
				: unguarded;
		boolean canDelete = eo instanceof ERXGuardedObjectInterface ? ((ERXGuardedObjectInterface) eo).canDelete()
				: unguarded;
		boolean isEntityWritable = !ERXValueUtilities.booleanValue(c.valueForKey("readOnly"));
		CRUDAuthorization auth = auth(c);
		
		if (!c.frame()) {
			choices.add("_return");
		}
		if (isEntityWritable && CRUDAuthorization.CAN_UPDATE.invoke(auth, eo) && canUpdate) {
			choices.add("_edit");
		}
		if (isEntityWritable && CRUDAuthorization.CAN_DELETE.invoke(auth, eo) && canDelete) {
			choices.add("_deleteReturn");
		}
		return choices;
	}

	public Boolean canReadProperty(D2WContext c) {
		EOEnterpriseObject eo = object(c);
		String propertyKey = c.propertyKey();
		if(eo == null || propertyKey == null) { 
			throw new IllegalStateException("object and/or propertyKey is null");
		}
		boolean b = CRUDAuthorization.CAN_READ_PROPERTY.invoke(auth(c), eo, propertyKey);
		return Boolean.valueOf(b);
	}
	
	public Boolean canUpdateProperty(D2WContext c) {
		EOEnterpriseObject eo = object(c);
		String propertyKey = c.propertyKey();
		if(eo == null || propertyKey == null) { 
			throw new IllegalStateException("object and/or propertyKey is null");
		}
		boolean b = CRUDAuthorization.CAN_UPDATE_PROPERTY.invoke(auth(c), eo, propertyKey);
		return Boolean.valueOf(b);
	}
	
	public Boolean disabled(D2WContext c) {
		Boolean b = canUpdateProperty(c);
		return Boolean.FALSE.equals(b);
	}
	
	public Boolean displayProperty(D2WContext c) {
		Boolean b = canReadProperty(c);
		return !Boolean.FALSE.equals(b);
	}
	
	public NSArray<EOEntity> createSubEntities(D2WContext c) {
		WOSession session = (WOSession)c.valueForKey(D2WModel.SessionKey);
		D2WContext subContext = new D2WContext(session);
		NSArray<EOEntity> e = new NSArray<EOEntity>(c.entity());
		return _createSubEntities(e, subContext);
	}
	
	private NSArray<EOEntity> _createSubEntities(NSArray<EOEntity> entities, D2WContext c) {
		NSMutableArray<EOEntity> result = new NSMutableArray<EOEntity>();
		for(EOEntity e : entities) {
			if(!e.isAbstractEntity()){
				c.setEntity(e);
				boolean canCreate = CRUDAuthorization.CAN_CREATE.invoke(auth(c), e.name());
				boolean readOnly = e.isReadOnly() || ERXValueUtilities.booleanValue(c.valueForKey("readOnly"));
				if(!readOnly && canCreate) {
					result.add(e);
				}
			}
			result.addObjectsFromArray(_createSubEntities(e.subEntities(), c));
		}
		return result;
	}

}
