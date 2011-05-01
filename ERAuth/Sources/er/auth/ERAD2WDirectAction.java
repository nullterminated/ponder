package er.auth;

import com.webobjects.appserver.WORequest;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.D2WUtils;
import com.webobjects.directtoweb.ERD2WContext;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;

import er.directtoweb.ERD2WDirectAction;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXEOControlUtilities;
import er.extensions.foundation.ERXStringUtilities;

public class ERAD2WDirectAction extends ERD2WDirectAction {

	public ERAD2WDirectAction(WORequest r) {
		super(r);
	}
	
	public EOEnterpriseObject objectFromRequest(String entityName) {
		EOEditingContext ec = ERXEC.newEditingContext(session().defaultEditingContext().parentObjectStore());
		EOEnterpriseObject eo = ERXEOControlUtilities.objectWithPrimaryKeyValue(ec, entityName, primaryKeyFromRequest(ec, entityName), null);
		return eo;
	}
	
	/**
	 * Checks if a page configuration is allowed to render. Override this
	 * only if you want to ignore the results from the {@link CRUDAuthorization}
	 * object. Implement {@link #allowUnhandledPageConfiguration(String)}
	 * if you would like to provide authorization logic for configurations
	 * not handled by the {@link CRUDAuthorization}
	 * 
	 * @param pageConfiguration
	 */
	protected boolean allowPageConfiguration(String pageConfiguration) {
		D2WContext d2wContext = ERD2WContext.newContext(session());
		d2wContext.setDynamicPage(pageConfiguration);
		EOEntity entity = d2wContext.entity();
		String task = d2wContext.task();
		if (task == null || entity == null) {
			return allowUnhandledPageConfiguration(pageConfiguration);
		}
		CRUDAuthorization auth = (CRUDAuthorization)d2wContext.valueForKey("crudAuthorization");
		if(pageConfiguration.startsWith("Create")) {
			return CRUDAuthorization.CAN_CREATE.invoke(auth, entity).booleanValue();
		} else if("edit".equals(task)) {
			EOEnterpriseObject eo = objectFromRequest(entity.name());
			return eo == null?
					CRUDAuthorization.CAN_CREATE.invoke(auth, entity).booleanValue():
					CRUDAuthorization.CAN_UPDATE.invoke(auth, eo).booleanValue();
		} else if("inspect".equals(task)) {
			EOEnterpriseObject eo = objectFromRequest(entity.name());
			return eo == null?false:CRUDAuthorization.CAN_READ.invoke(auth, eo);
		} else if("query".equals(task) || "list".equals(task)) {
			return CRUDAuthorization.CAN_QUERY.invoke(auth, entity);
		} else if("queryAll".equals(task)) {
			return !D2WUtils.visibleEntityNames(d2wContext).isEmpty();
		} else if("editRelationship".equals(task)) {
	    	String keypath = keyPathFromRequest();
	    	String masterEntityName = ERXStringUtilities.firstPropertyKeyInKeyPath(keypath);
	    	String relationshipKey = ERXStringUtilities.keyPathWithoutFirstProperty(keypath);
			EOEnterpriseObject eo = objectFromRequest(masterEntityName);
			return CRUDAuthorization.CAN_UPDATE_PROPERTY.invoke(auth, eo, relationshipKey);
		}
		return allowUnhandledPageConfiguration(pageConfiguration);
	}
	
	/**
	 * Override this method to allow access to custom configurations that
	 * are not handled by the {@link #allowPageConfiguration(String)} method
	 * 
	 * @param pageConfiguration a page configuration name
	 * @return
	 */
	protected boolean allowUnhandledPageConfiguration(String pageConfiguration) {
		return false;
	}
}
