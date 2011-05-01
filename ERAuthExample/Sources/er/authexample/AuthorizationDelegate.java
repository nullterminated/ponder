package er.authexample;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOQualifier;

import er.auth.CRUDAuthorization;
import er.authexample.model.User;
import er.authexample.model.User.UserClazz;
import er.corebusinesslogic.ERCoreBusinessLogic;
import er.extensions.eof.EOEnterpriseObjectClazz;

public class AuthorizationDelegate extends CRUDAuthorization {

	@Override
	public <T extends EOEnterpriseObject> Boolean canCreate(EOEnterpriseObjectClazz<T> clazz) {
		// TODO Auto-generated method stub
		return super.canCreate(clazz);
	}

	@Override
	public Boolean canDelete(EOEnterpriseObject eo) {
		// TODO Auto-generated method stub
		return super.canDelete(eo);
	}

	@Override
	public <T extends EOEnterpriseObject> Boolean canQuery(EOEnterpriseObjectClazz<T> clazz) {
		// TODO Auto-generated method stub
		return super.canQuery(clazz);
	}

	@Override
	public Boolean canRead(EOEnterpriseObject eo) {
		// TODO Auto-generated method stub
		return super.canRead(eo);
	}

	@Override
	public Boolean canReadProperty(EOEnterpriseObject eo, String keyPath) {
		// TODO Auto-generated method stub
		return super.canReadProperty(eo, keyPath);
	}

	@Override
	public Boolean canUpdate(EOEnterpriseObject eo) {
		// TODO Auto-generated method stub
		return super.canUpdate(eo);
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

	@Override
	public Boolean canUpdateProperty(EOEnterpriseObject eo, String keyPath) {
		// TODO Auto-generated method stub
		return super.canUpdateProperty(eo, keyPath);
	}

	@Override
	public <T extends EOEnterpriseObject> EOQualifier restrictingQueryQualifier(EOEnterpriseObjectClazz<T> clazz) {
		// TODO Auto-generated method stub
		return super.restrictingQueryQualifier(clazz);
	}

}
