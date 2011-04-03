package er.auth.crud;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOQualifier;

import er.extensions.eof.EOEnterpriseObjectClazz;

public class CRUDUtility {
	
	public static boolean canDelete(CRUDAuthorization auth, EOEnterpriseObject eo) {
		if(eo instanceof CRUDEntity) {
			return ((CRUDEntity)eo).canDelete(auth);
		}
		return auth.canDelete(eo);
	}
	
	public static boolean canRead(CRUDAuthorization auth, EOEnterpriseObject eo) {
		if(eo instanceof CRUDEntity) {
			return ((CRUDEntity)eo).canRead(auth);
		}
		return auth.canRead(eo);
	}
	
	public static boolean canReadProperty(CRUDAuthorization auth, EOEnterpriseObject eo, String propertyKey) {
		if(eo instanceof CRUDEntity) {
			return ((CRUDEntity)eo).canReadProperty(auth, propertyKey);
		}
		return auth.canReadProperty(eo, propertyKey);
	}

	public static boolean canUpdate(CRUDAuthorization auth, EOEnterpriseObject eo) {
		if(eo instanceof CRUDEntity) {
			return ((CRUDEntity)eo).canUpdate(auth);
		}
		return auth.canUpdate(eo);
	}

	public static boolean canUpdateProperty(CRUDAuthorization auth, EOEnterpriseObject eo, String propertyKey){
		if(eo instanceof CRUDEntity) {
			return ((CRUDEntity)eo).canUpdateProperty(auth, propertyKey);
		}
		return auth.canUpdateProperty(eo, propertyKey);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean canCreate(CRUDAuthorization auth, String entityName) {
		EOEnterpriseObjectClazz clazz = EOEnterpriseObjectClazz.clazzForEntityNamed(entityName);
		if(clazz instanceof CRUDClazz) {
			return ((CRUDClazz)clazz).canCreate(auth);
		}
		return auth.canCreate(clazz);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean canQuery(CRUDAuthorization auth, String entityName) {
		EOEnterpriseObjectClazz clazz = EOEnterpriseObjectClazz.clazzForEntityNamed(entityName);
		if(clazz instanceof CRUDClazz) {
			return ((CRUDClazz)clazz).canQuery(auth);
		}
		return auth.canQuery(clazz);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EOQualifier restrictingQualifier(CRUDAuthorization auth, String entityName) {
		EOEnterpriseObjectClazz clazz = EOEnterpriseObjectClazz.clazzForEntityNamed(entityName);
		if(clazz instanceof CRUDClazz) {
			return ((CRUDClazz)clazz).restrictingQualifier(auth);
		}
		return auth.restrictingQualifier(clazz);
	}
}
