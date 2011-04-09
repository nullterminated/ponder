package er.auth.crud;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOQualifier;

import er.extensions.eof.EOEnterpriseObjectClazz;

public class NoAccessAuthorization implements CRUDAuthorization {

	@Override
	public <T extends EOEnterpriseObject> boolean canCreate(EOEnterpriseObjectClazz<T> clazz) {
		return false;
	}

	@Override
	public boolean canDelete(EOEnterpriseObject eo) {
		return false;
	}

	@Override
	public <T extends EOEnterpriseObject> boolean canQuery(EOEnterpriseObjectClazz<T> clazz) {
		return false;
	}

	@Override
	public boolean canRead(EOEnterpriseObject eo) {
		return false;
	}

	@Override
	public boolean canReadProperty(EOEnterpriseObject eo, String propertyKey) {
		return false;
	}

	@Override
	public boolean canUpdate(EOEnterpriseObject eo) {
		return false;
	}

	@Override
	public boolean canUpdateProperty(EOEnterpriseObject eo, String propertyKey) {
		return false;
	}

	@Override
	public <T extends EOEnterpriseObject> EOQualifier restrictingQualifier(EOEnterpriseObjectClazz<T> clazz) {
		return null;
	}

}
