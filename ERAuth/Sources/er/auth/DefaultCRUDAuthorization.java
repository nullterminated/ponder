package er.auth;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOQualifier;

import er.extensions.eof.EOEnterpriseObjectClazz;
import er.extensions.qualifiers.ERXFalseQualifier;

/**
 * The default CRUD authorization delegate. It allows no access to anything.
 */
public enum DefaultCRUDAuthorization implements CRUDAuthorization {
	INSTANCE;
	
	private static final EOQualifier QUALIFIER = new ERXFalseQualifier();

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
		return QUALIFIER;
	}

}
