package er.auth.processing;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.eocontrol.EOKeyGlobalID;
import com.webobjects.eocontrol.EOKeyValueCodingAdditions;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import er.auth.ERStageManager;
import er.auth.model.ERTwoFactorAuthenticationRequest;
import er.auth.model.ERTwoFactorAuthenticationResponse;
import er.auth.model.enums.ERTwoFactorAuthenticationFailure;
import er.extensions.eof.ERXFetchSpecification;
import er.extensions.eof.ERXKeyGlobalID;
import er.extensions.eof.ERXQ;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;

public class ERTwoFactorAuthenticationProcessor extends ERAuthenticationProcessor<ERTwoFactorAuthenticationRequest> {
	
	public WOActionResults authenticate(ERTwoFactorAuthenticationRequest authRequest) {
		EOEditingContext ec = authRequest.editingContext();
		ERTwoFactorAuthenticationResponse response = ERTwoFactorAuthenticationResponse.clazz.createAndInsertObject(ec);
		response.setAuthenticationRequest(authRequest);

		ERTwoFactorAuthenticationConfig config = ERTwoFactorAuthenticationRequest.clazz.authenticationConfig();
		EOQualifier q = ERXQ.equals(config.usernameKeyPath(), authRequest.username());
		ERXFetchSpecification<EOEnterpriseObject> fs = 
			new ERXFetchSpecification<EOEnterpriseObject>(config.userEntityName(), q, null);
		if(config.passwordKeyPath().indexOf(EOKeyValueCodingAdditions.KeyPathSeparator) != -1) {
			String prefetch = ERXStringUtilities.keyPathWithoutLastProperty(config.passwordKeyPath());
			fs.setPrefetchingRelationshipKeyPaths(new NSArray<String>(prefetch));
		}
		NSArray<EOEnterpriseObject> users = fs.fetchObjects(ec);
		
		switch(users.count()) {
		case 0:
			response.setAuthenticationFailed(Boolean.TRUE);
			response.setAuthenticationFailureType(ERTwoFactorAuthenticationFailure.UNKNOWN_USERNAME);
			break;
		case 1:
			EOEnterpriseObject eo = users.lastObject();
			Object password = eo.valueForKeyPath(config.passwordKeyPath());
			EOGlobalID gid = ec.globalIDForObject(eo);
			ERXKeyGlobalID kgid = ERXKeyGlobalID.globalIDForGID((EOKeyGlobalID)gid);
			response.setUserID(kgid);
			if(config.verifyPassword(authRequest.password(), password)) {
				response.setAuthenticationFailed(Boolean.FALSE);
				ERStageManager.INSTANCE.setActor(eo);
			} else {
				response.setAuthenticationFailed(Boolean.TRUE);
				response.setAuthenticationFailureType(ERTwoFactorAuthenticationFailure.INVALID_PASSWORD);
			}
			break;
		default:
			response.setAuthenticationFailed(Boolean.TRUE);
			response.setAuthenticationFailureType(ERTwoFactorAuthenticationFailure.DUPLICATE_USERNAME);
			break;
		}
		
		try {
			ec.saveChanges();
		} catch(EOGeneralAdaptorException e) {
			//TODO crap...
			throw e;
		}
		
		WOComponent nextPage;
		if(response.authenticationFailed()) {
			nextPage = config.loginFailurePage();
			ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
			ERXValidationException ex = factory.createCustomException(null, response.authenticationFailureType().name());
			nextPage.validationFailedWithException(ex, null, null);
		} else {
			nextPage = config.loginSuccessPage();
		}
		
		return nextPage;
	}
}
