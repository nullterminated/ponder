package er.users;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOQualifier;

import er.auth.CRUDAuthorization;
import er.corebusinesslogic.ERCoreBusinessLogic;
import er.extensions.eof.EOEnterpriseObjectClazz;
import er.extensions.qualifiers.ERXFalseQualifier;
import er.users.model.ERUser;
import er.users.model.ERUser.ERUserClazz;

public enum AuthorizationDelegate implements CRUDAuthorization {
	INSTANCE;
	
	public boolean isActor(EOEnterpriseObject eo) {
		return eo.equals(ERCoreBusinessLogic.actor(eo.editingContext()));
	}
	
	@Override
	public <T extends EOEnterpriseObject> Boolean canCreate(EOEnterpriseObjectClazz<T> clazz) {
		return Boolean.FALSE;
	}

	@Override
	public Boolean canDelete(EOEnterpriseObject eo) {
		return Boolean.FALSE;
	}

	@Override
	public <T extends EOEnterpriseObject> Boolean canQuery(EOEnterpriseObjectClazz<T> clazz) {
		return Boolean.FALSE;
	}

	@Override
	public Boolean canRead(EOEnterpriseObject eo) {
		return Boolean.FALSE;
	}

	@Override
	public Boolean canReadProperty(EOEnterpriseObject eo, String keyPath) {
		return Boolean.FALSE;
	}

	@Override
	public Boolean canUpdate(EOEnterpriseObject eo) {
		return Boolean.FALSE;
	}
	
	@Override
	public Boolean canUpdateProperty(EOEnterpriseObject eo, String keyPath) {
		return Boolean.FALSE;
	}

	@Override
	public <T extends EOEnterpriseObject> EOQualifier restrictingQueryQualifier(EOEnterpriseObjectClazz<T> clazz) {
		return new ERXFalseQualifier();
	}

	public Boolean canCreate(ERUserClazz<ERUser> clazz) {
		return Boolean.TRUE;
	}
	
	public Boolean canUpdate(ERUser eo) {
		return eo.equals(ERCoreBusinessLogic.actor(eo.editingContext()));
	}
	
	public Boolean canRead(ERUser eo) {
		return Boolean.TRUE;
	}
	
	public Boolean canQuery(ERUserClazz<ERUser> clazz) {
		return Boolean.TRUE;
	}
	
	public Boolean canReadProperty(ERUser eo, String keypath) {
		if(ERUser.USERNAME_KEY.equals(keypath)) { return true; }
		if(isActor(eo)) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
	public Boolean canUpdateProperty(ERUser eo, String keypath) {
		if(eo.isNewObject()) { return Boolean.TRUE; }
		if(keypath.equals(ERUser.USERNAME_KEY)) { return Boolean.FALSE; }
		return eo.equals(ERCoreBusinessLogic.actor(eo.editingContext()));
	}
}
