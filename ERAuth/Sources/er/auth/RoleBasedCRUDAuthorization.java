package er.auth;

import com.webobjects.appserver.WOComponent;
import com.webobjects.directtoweb.D2WComponent;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.ERD2WContext;
import com.webobjects.directtoweb.ERD2WUtilities;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import er.auth.model.EREntityPermission;
import er.auth.model.ERPropertyPermission;
import er.auth.model.ERRole;
import er.extensions.appserver.ERXSession;
import er.extensions.eof.EOEnterpriseObjectClazz;
import er.extensions.eof.ERXKey;
import er.extensions.foundation.ERXArrayUtilities;
import er.extensions.qualifiers.ERXFalseQualifier;
import er.extensions.qualifiers.ERXTrueQualifier;

public class RoleBasedCRUDAuthorization implements CRUDAuthorization {
	
	private static final ERXKey<ERRole> currentUserRoles = new ERXKey<ERRole>("currentUserRoles");
	private static final EOQualifier falseQualifier = new ERXFalseQualifier();
	private static final EOQualifier trueQualifier = new ERXTrueQualifier();
	
	public static D2WContext d2wContext() {
		ERXSession session = ERXSession.session();
		WOComponent component = session.context().component();
		D2WComponent d2wComponent = ERD2WUtilities.enclosingComponentOfClass(component, D2WComponent.class);
		D2WContext context = d2wComponent == null?ERD2WContext.newContext(session):d2wComponent.d2wContext();
		return context;
	}
	
	public static NSArray<EREntityPermission> entityPermissions(String entityName) {
		NSArray<ERRole> roles = currentUserRoles.arrayValueInObject(d2wContext());
		if(roles == null || roles.isEmpty()) {
			return NSArray.emptyArray();
		}
		NSArray<EREntityPermission> allPermissions = ERXArrayUtilities.flatten(ERRole.ENTITY_PERMISSIONS.arrayValueInObject(roles));
		NSArray<EREntityPermission> entityPermissions = EREntityPermission.NAME_FOR_ENTITY.eq(entityName).filtered(allPermissions);
		return entityPermissions;
	}
	
	public static NSArray<ERPropertyPermission> propertyPermissions(String entityName, String propertyKey) {
		NSArray<EREntityPermission> entityPermissions = entityPermissions(entityName);
		if(entityPermissions.isEmpty()) {
			return NSArray.emptyArray();
		}
		NSArray<ERPropertyPermission> allPermissions = ERXArrayUtilities.flatten(EREntityPermission.PROPERTY_PERMISSIONS.arrayValueInObject(entityPermissions));
		NSArray<ERPropertyPermission> propertyPermissions = ERPropertyPermission.NAME_FOR_PROPERTY.eq(propertyKey).filtered(allPermissions);
		return propertyPermissions;
	}

	@Override
	public <T extends EOEnterpriseObject> Boolean canCreate(EOEnterpriseObjectClazz<T> clazz) {
		NSArray<Boolean> allow = EREntityPermission.ALLOW_CREATE.arrayValueInObject(entityPermissions(clazz.entityName()));
		return allow.contains(Boolean.TRUE);
	}

	@Override
	public Boolean canDelete(EOEnterpriseObject eo) {
		NSArray<Boolean> allow = EREntityPermission.ALLOW_DELETE.arrayValueInObject(entityPermissions(eo.entityName()));
		return allow.contains(Boolean.TRUE);
	}

	@Override
	public <T extends EOEnterpriseObject> Boolean canQuery(EOEnterpriseObjectClazz<T> clazz) {
		NSArray<Boolean> allow = EREntityPermission.ALLOW_QUERY.arrayValueInObject(entityPermissions(clazz.entityName()));
		return allow.contains(Boolean.TRUE);
	}

	@Override
	public Boolean canRead(EOEnterpriseObject eo) {
		NSArray<Boolean> allow = EREntityPermission.ALLOW_READ.arrayValueInObject(entityPermissions(eo.entityName()));
		return allow.contains(Boolean.TRUE);
	}

	@Override
	public Boolean canReadProperty(EOEnterpriseObject eo, String keyPath) {
		if(keyPath.indexOf('.') > -1 || keyPath.indexOf('@') > -1) {
			//Key paths and array operators not supported
			return Boolean.FALSE;
		}
		NSArray<Boolean> allow = ERPropertyPermission.ALLOW_READ.arrayValueInObject(propertyPermissions(eo.entityName(), keyPath));
		return allow.contains(Boolean.TRUE);
	}

	@Override
	public Boolean canUpdate(EOEnterpriseObject eo) {
		NSArray<Boolean> allow = EREntityPermission.ALLOW_UPDATE.arrayValueInObject(entityPermissions(eo.entityName()));
		return allow.contains(Boolean.TRUE);
	}

	@Override
	public Boolean canUpdateProperty(EOEnterpriseObject eo, String keyPath) {
		if(keyPath.indexOf('.') > -1 || keyPath.indexOf('@') > -1) {
			//Key paths and array operators not supported
			return Boolean.FALSE;
		}
		NSArray<Boolean> allow = ERPropertyPermission.ALLOW_UPDATE.arrayValueInObject(propertyPermissions(eo.entityName(), keyPath));
		return allow.contains(Boolean.TRUE);
	}

	@Override
	public <T extends EOEnterpriseObject> EOQualifier restrictingQueryQualifier(EOEnterpriseObjectClazz<T> clazz) {
		return canQuery(clazz)?trueQualifier:falseQualifier;
	}

}
