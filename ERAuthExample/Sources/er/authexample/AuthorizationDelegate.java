package er.authexample;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOQualifier;

import er.auth.CRUDAuthorization;
import er.authexample.model.User;
import er.authexample.model.User.UserClazz;
import er.corebusinesslogic.ERCoreBusinessLogic;
import er.extensions.eof.EOEnterpriseObjectClazz;
import er.extensions.qualifiers.ERXFalseQualifier;

public class AuthorizationDelegate implements CRUDAuthorization {

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

	public Boolean canCreate(UserClazz<User> clazz) {
		return Boolean.TRUE;
	}
	
	public Boolean canUpdate(User eo) {
		return eo.equals(ERCoreBusinessLogic.actor(eo.editingContext()));
	}
	
	public Boolean canRead(User eo) {
		return Boolean.TRUE;
	}
	
	public Boolean canQuery(UserClazz<User> clazz) {
		return Boolean.TRUE;
	}
	
	public Boolean canReadProperty(User eo, String keypath) {
		if(User.USERNAME_KEY.equals(keypath)) { return true; }
		if(isActor(eo)) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
	public Boolean canUpdateProperty(User eo, String keypath) {
		if(eo.isNewObject()) { return Boolean.TRUE; }
		if(keypath.equals(User.USERNAME_KEY)) { return Boolean.FALSE; }
		return eo.equals(ERCoreBusinessLogic.actor(eo.editingContext()));
	}

}
