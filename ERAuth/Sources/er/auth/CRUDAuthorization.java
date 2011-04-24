package er.auth;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOQualifier;

import er.extensions.eof.EOEnterpriseObjectClazz;

public class CRUDAuthorization {
	public static final ERASelector<Boolean, CRUDAuthorization> CAN_CREATE =
		new ERASelector<Boolean, CRUDAuthorization>("canCreate", CRUDAuthorization.class, new Class<?>[]{EOEnterpriseObjectClazz.class});
	public static final ERASelector<Boolean, CRUDAuthorization> CAN_READ =
		new ERASelector<Boolean, CRUDAuthorization>("canRead", CRUDAuthorization.class, new Class<?>[]{EOEnterpriseObject.class});
	public static final ERASelector<Boolean, CRUDAuthorization> CAN_UPDATE =
		new ERASelector<Boolean, CRUDAuthorization>("canUpdate", CRUDAuthorization.class, new Class<?>[]{EOEnterpriseObject.class});
	public static final ERASelector<Boolean, CRUDAuthorization> CAN_DELETE =
		new ERASelector<Boolean, CRUDAuthorization>("canDelete", CRUDAuthorization.class, new Class<?>[]{EOEnterpriseObject.class});
	public static final ERASelector<Boolean, CRUDAuthorization> CAN_READ_PROPERTY =
		new ERASelector<Boolean, CRUDAuthorization>("canReadProperty", CRUDAuthorization.class, new Class<?>[]{EOEnterpriseObject.class, String.class});
	public static final ERASelector<Boolean, CRUDAuthorization> CAN_UPDATE_PROPERTY =
		new ERASelector<Boolean, CRUDAuthorization>("canUpdateProperty", CRUDAuthorization.class, new Class<?>[]{EOEnterpriseObject.class, String.class});
	public static final ERASelector<Boolean, CRUDAuthorization> CAN_QUERY =
		new ERASelector<Boolean, CRUDAuthorization>("canQuery", CRUDAuthorization.class, new Class<?>[]{EOEnterpriseObjectClazz.class});
	public static final ERASelector<EOQualifier, CRUDAuthorization> RESTRICTING_QUERY_QUALIFIER =
		new ERASelector<EOQualifier, CRUDAuthorization>("restrictingQueryQualifier", CRUDAuthorization.class, new Class<?>[]{EOEnterpriseObject.class});

	public <T extends EOEnterpriseObject> Boolean canCreate(EOEnterpriseObjectClazz<T> clazz) {
		return Boolean.FALSE;
	}
	
	public Boolean canDelete(EOEnterpriseObject eo) {
		return Boolean.FALSE;
	}
	
	public <T extends EOEnterpriseObject> Boolean canQuery(EOEnterpriseObjectClazz<T> clazz) {
		return Boolean.FALSE;
	}
	
	public Boolean canRead(EOEnterpriseObject eo) {
		return Boolean.FALSE;
	}
	
	public Boolean canReadProperty(EOEnterpriseObject eo, String keyPath) {
		return Boolean.FALSE;
	}
	
	public Boolean canUpdate(EOEnterpriseObject eo) {
		return Boolean.FALSE;
	}
	
	public Boolean canUpdateProperty(EOEnterpriseObject eo, String keyPath) {
		return Boolean.FALSE;
	}
	
	public <T extends EOEnterpriseObject> EOQualifier restrictingQueryQualifier(EOEnterpriseObjectClazz<T> clazz) {
		return null;
	}
}
