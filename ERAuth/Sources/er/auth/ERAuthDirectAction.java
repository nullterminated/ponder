package er.auth;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;

import er.auth.model.ERAuthenticationRequest;
import er.auth.model.ERAuthenticationRequest.ERAuthenticationRequestClazz;
import er.auth.processing.ERAuthenticationConfig;
import er.auth.processing.ERAuthenticationProcessor;
import er.auth.processing.ERCredentialRecoveryConfig;
import er.extensions.appserver.ERXDirectAction;
import er.extensions.appserver.ERXRequest;
import er.extensions.appserver.ERXWOContext;
import er.extensions.eof.EOEnterpriseObjectClazz;
import er.extensions.eof.ERXEC;
import er.extensions.validation.ERXExceptionHolder;

public class ERAuthDirectAction extends ERXDirectAction {

    /** denotes the context ID for the previous pages */
    public static final String contextIDKey = "__cid";
    public static final String lastContextIDKey = "__lcid";

    public ERAuthDirectAction(WORequest r) {
		super(r);
	}
    
    public WOActionResults credentialRecoveryAction() {
		ERXRequest request = (ERXRequest)request();
		String entityName = request.stringFormValueForKey("entityName");
		EOEnterpriseObjectClazz<?> clazz = EOEnterpriseObjectClazz.clazzForEntityNamed(entityName);
		if(clazz instanceof ERAuthenticationRequest.ERAuthenticationRequestClazz) {
			ERAuthenticationRequestClazz<ERAuthenticationRequest> authClazz = 
				(ERAuthenticationRequestClazz<ERAuthenticationRequest>)clazz;
			ERAuthenticationConfig config = authClazz.authenticationConfig();
			if(config instanceof ERCredentialRecoveryConfig) {
				ERCredentialRecoveryConfig crConfig = (ERCredentialRecoveryConfig)config;
				return crConfig.credentialRecoveryPage();
			}
			
		}
		return badRequest();
    }
	
	public WOActionResults returnAction() {
		WOComponent page = lastPreviousPageFromRequest();
		return page == null?defaultAction():page;
	}

	public WOActionResults loginAction() {
		ERXRequest request = (ERXRequest)request();
		EOEditingContext ec = ERXEC.newEditingContext();
		String entityName = request.stringFormValueForKey("entityName");
		EOEnterpriseObject eo = EOUtilities.createAndInsertInstance(ec, entityName);
		
		if(eo instanceof ERAuthenticationRequest) {
			ERAuthenticationRequest authRequest = (ERAuthenticationRequest)eo;
			authRequest.takeValuesFromRequest(request);
			try {
				eo.validateForSave();
			} catch(ValidationException e) {
				WOComponent fail = authRequest.clazz().authenticationConfig().loginFailurePage();
				if(fail instanceof ERXExceptionHolder) {
					((ERXExceptionHolder)fail).clearValidationFailed();
				}
				if(fail != null) {
					fail.validationFailedWithException(e, e.object(), e.key());
				}
				return fail;
			}
			
			ERAuthenticationProcessor<ERAuthenticationRequest> processor = ERAuth.sharedInstance().processorForType(authRequest);
			return processor.authenticate(authRequest);
		}
		return badRequest();
	}
	
	public WOActionResults badRequest() {
		WOResponse response = new WOResponse();
		response.setStatus(400);
		response.setContent("Invalid request");
		return response;
	}
	
    public static WOComponent previousPageFromRequest() {
    	WOContext context = ERXWOContext.currentContext();
        String cid = context.request().stringFormValueForKey(contextIDKey);
        if(cid == null) { return context.page(); }
        WOComponent comp = context.session().restorePageForContextID(cid);
        return comp;
    }
    
    public static WOComponent lastPreviousPageFromRequest() {
    	WOContext context = ERXWOContext.currentContext();
        String cid = context.request().stringFormValueForKey(lastContextIDKey);
        if(cid == null) { return context.page(); }
        WOComponent comp = context.session().restorePageForContextID(cid);
        return comp;
    }
}
