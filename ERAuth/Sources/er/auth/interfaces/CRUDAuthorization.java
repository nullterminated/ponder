package er.auth.interfaces;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOQualifier;

import er.extensions.eof.EOEnterpriseObjectClazz;

public interface CRUDAuthorization {
	public <T extends EOEnterpriseObject> boolean canCreate(EOEnterpriseObjectClazz<T> clazz);
	public boolean canDelete(EOEnterpriseObject eo);
	public <T extends EOEnterpriseObject> boolean canQuery(EOEnterpriseObjectClazz<T> clazz);
	public boolean canRead(EOEnterpriseObject eo);
	public boolean canReadProperty(EOEnterpriseObject eo, String propertyKey);
	public boolean canUpdate(EOEnterpriseObject eo);
	public boolean canUpdateProperty(EOEnterpriseObject eo, String propertyKey);
	public <T extends EOEnterpriseObject> EOQualifier restrictingQualifier(EOEnterpriseObjectClazz<T> clazz);
}
