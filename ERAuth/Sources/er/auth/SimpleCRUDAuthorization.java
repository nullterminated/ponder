package er.auth;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOQualifier;

import er.extensions.eof.EOEnterpriseObjectClazz;
import er.extensions.qualifiers.ERXFalseQualifier;
import er.extensions.qualifiers.ERXTrueQualifier;

/**
 * Simple CRUD authorization delegates. ALLOW_ALL gives all access, DENY_ALL rejects all access.
 */
public enum SimpleCRUDAuthorization implements CRUDAuthorization {
	ALLOW_ALL(Boolean.TRUE, new ERXTrueQualifier()), 
	DENY_ALL(Boolean.FALSE, new ERXFalseQualifier());
	
	private final EOQualifier _qualifier;
	private final Boolean _allow;

	SimpleCRUDAuthorization(Boolean allow, EOQualifier qualifier) {
		_allow = allow;
		_qualifier = qualifier;
	}
	
	@Override
	public <T extends EOEnterpriseObject> Boolean canCreate(EOEnterpriseObjectClazz<T> clazz) {
		return _allow;
	}

	@Override
	public Boolean canDelete(EOEnterpriseObject eo) {
		return _allow;
	}

	@Override
	public <T extends EOEnterpriseObject> Boolean canQuery(EOEnterpriseObjectClazz<T> clazz) {
		return _allow;
	}

	@Override
	public Boolean canRead(EOEnterpriseObject eo) {
		return _allow;
	}

	@Override
	public Boolean canReadProperty(EOEnterpriseObject eo, String keyPath) {
		return _allow;
	}

	@Override
	public Boolean canUpdate(EOEnterpriseObject eo) {
		return _allow;
	}

	@Override
	public Boolean canUpdateProperty(EOEnterpriseObject eo, String keyPath) {
		return _allow;
	}

	@Override
	public <T extends EOEnterpriseObject> EOQualifier restrictingQueryQualifier(EOEnterpriseObjectClazz<T> clazz) {
		return _qualifier;
	}

}
