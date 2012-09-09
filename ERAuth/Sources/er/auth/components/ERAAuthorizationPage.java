package er.auth.components;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.auth.model.EREntityPermission;
import er.auth.model.ERPropertyPermission;
import er.auth.model.ERRole;
import er.directtoweb.pages.ERD2WPage;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXFetchSpecification;
import er.extensions.foundation.ERXArrayUtilities;

public class ERAAuthorizationPage extends ERD2WPage {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;
	
	private NSArray<ERRole> _roles;

	private EREntityPermission _selectedPermission;

	private NSArray<EREntityPermission> _entityPermissions;

	private NSArray<ERPropertyPermission> _propertyPermissions;

	public ERAAuthorizationPage(WOContext context) {
        super(context);
    }
	
	@Override
	public EOEditingContext editingContext() {
		if(_context == null) {
			_context = ERXEC.newEditingContext();
			_context.lock();
		}
		return _context;
	}
	
	public NSArray<ERRole> roles() {
		if(_roles == null) {
			NSArray<String> roleNames = (NSArray<String>) d2wContext().valueForKey("authorizeRoleNames");
			_roles = ERRole.clazz.objectsMatchingQualifier(editingContext(), ERRole.ROLE_NAME.in(roleNames));
		}
		return _roles;
	}

	/**
	 * @return the selectedPermission
	 */
	public EREntityPermission selectedPermission() {
		return _selectedPermission;
	}

	/**
	 * @param selectedPermission the selectedPermission to set
	 */
	public void setSelectedPermission(EREntityPermission selectedPermission) {
		this._selectedPermission = selectedPermission;
	}

	/**
	 * @return the entityPermissions
	 */
	public NSArray<EREntityPermission> entityPermissions() {
		if(_entityPermissions == null) {
			String entityName = d2wContext().entity().name();
			NSArray<String> roleNames = (NSArray<String>) d2wContext().valueForKey("authorizeRoleNames");
			EOQualifier q = EREntityPermission.ROLE.dot(ERRole.ROLE_NAME).in(roleNames)
					.and(EREntityPermission.NAME_FOR_ENTITY.eq(entityName));
			ERXFetchSpecification<EREntityPermission> fs = 
					new ERXFetchSpecification<EREntityPermission>(EREntityPermission.ENTITY_NAME, q, null);
			NSArray<String> prefetch = new NSArray<String>(EREntityPermission.ROLE_KEY);
			fs.setPrefetchingRelationshipKeyPaths(prefetch);
			NSArray<EREntityPermission> permissions = fs.fetchObjects(editingContext());
			int diff = roles().count() - permissions.count();
			if(diff > 0) {
				NSMutableArray<EREntityPermission> newPermissions = 
						new NSMutableArray<EREntityPermission>(diff);
				NSArray<ERRole> permRoles = EREntityPermission.ROLE.arrayValueInObject(permissions);
				NSArray<ERRole> roles = ERXArrayUtilities.arrayMinusArray(roles(), permRoles);
				for(ERRole role: roles) {
					EREntityPermission permission = role.createEntityPermissionsRelationship();
					permission.setNameForEntity(entityName);
					newPermissions.addObject(permission);
				}
				permissions = permissions.arrayByAddingObjectsFromArray(newPermissions);
			}
			_entityPermissions = permissions;
		}
		return _entityPermissions;
	}

	public WOActionResults selectedPermissionAction() {
		_propertyPermissions = null;
		return null;
	}

	/**
	 * @return the propertyPermissions
	 */
	public NSArray<ERPropertyPermission> propertyPermissions() {
		if(_propertyPermissions == null && selectedPermission() != null) {
			NSArray<String> propertyKeys = (NSArray<String>) d2wContext().valueForKey("authorizePropertyKeys");
			EOQualifier q = ERPropertyPermission.ENTITY_PERMISSION.eq(selectedPermission())
					.and(ERPropertyPermission.NAME_FOR_PROPERTY.in(propertyKeys));
			ERXFetchSpecification<ERPropertyPermission> fs = 
					new ERXFetchSpecification<ERPropertyPermission>(ERPropertyPermission.ENTITY_NAME, q, null);
			fs.setIncludeEditingContextChanges(true);
			NSArray<ERPropertyPermission> permissions = fs.fetchObjects(editingContext());
			int diff = propertyKeys.count() - permissions.count();
			if(diff > 0) {
				NSMutableArray<ERPropertyPermission> newPermissions =
						new NSMutableArray<ERPropertyPermission>(diff);
				NSArray<String> permProps = 
						ERPropertyPermission.NAME_FOR_PROPERTY.arrayValueInObject(permissions);
				NSArray<String> newProps = ERXArrayUtilities.arrayMinusArray(propertyKeys, permProps);
				for(String propertyKey : newProps) {
					ERPropertyPermission permission = 
							selectedPermission().createPropertyPermissionsRelationship();
					permission.setNameForProperty(propertyKey);
					newPermissions.addObject(permission);
				}
				permissions = permissions.arrayByAddingObjectsFromArray(newPermissions);
			}
			_propertyPermissions = permissions;
		}
		return _propertyPermissions;
	}
}