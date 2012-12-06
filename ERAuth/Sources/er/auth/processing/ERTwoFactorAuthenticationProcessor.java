package er.auth.processing;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

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
import com.webobjects.foundation.NSMutableDictionary;

import er.auth.ERStageManager;
import er.auth.model.ERAuthenticationRequest;
import er.auth.model.ERAuthenticationResponse;
import er.auth.model.ERTwoFactorAuthenticationRequest;
import er.auth.model.ERTwoFactorAuthenticationResponse;
import er.auth.model.enums.ERTwoFactorAuthenticationFailure;
import er.extensions.eof.ERXFetchSpecification;
import er.extensions.eof.ERXKeyGlobalID;
import er.extensions.eof.ERXQ;
import er.extensions.eof.ERXSortOrdering.ERXSortOrderings;
import er.extensions.foundation.ERXProperties;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;

public class ERTwoFactorAuthenticationProcessor extends ERAuthenticationProcessor<ERTwoFactorAuthenticationRequest> {
	private static final Logger log = Logger.getLogger(ERTwoFactorAuthenticationProcessor.class);

	/**
	 * @return Number of tries allowed before locking the account. Default is 10.
	 * 
	 * @property 
	 *           er.auth.processing.ERTwoFactorAuthenticationProcessor.invalidPasswordLimit
	 */
	public static int invalidPasswordLimit() {
		return ERXProperties.intForKeyWithDefault(
				"er.auth.processing.ERTwoFactorAuthenticationProcessor.invalidPasswordLimit", 10);
	}

	/**
	 * @return the length of time to lock an account after exceeding. Default is one hour.
	 * 
	 * @property 
	 *           er.auth.processing.ERTwoFactorAuthenticationProcessor.lockDuration
	 */
	public static long lockDuration() {
		return ERXProperties.longForKeyWithDefault(
				"er.auth.processing.ERTwoFactorAuthenticationProcessor.lockDuration", 3600000);
	}

	public DateTime lockedUntil(ERTwoFactorAuthenticationRequest authRequest) {
		if (invalidPasswordLimit() <= 0 || lockDuration() <= 0) {
			log.debug("Authentication locking disabled");
			return null;
		}

		DateTime lockDate = authRequest.requestDate().minus(lockDuration());
		ERXSortOrderings so = 
				ERTwoFactorAuthenticationResponse.AUTHENTICATION_REQUEST.dot(ERTwoFactorAuthenticationRequest.REQUEST_DATE).descs();
		String usernameKeyPath = ERXQ.keyPath(ERTwoFactorAuthenticationResponse.TWO_FACTOR_AUTHENTICATION_REQUEST_KEY, ERTwoFactorAuthenticationRequest.USERNAME_KEY);
		EOQualifier q = 
				ERXQ.equals(usernameKeyPath, authRequest.username())
				.and(ERTwoFactorAuthenticationResponse.AUTHENTICATION_FAILURE_TYPE.eq(ERTwoFactorAuthenticationFailure.INVALID_PASSWORD))
				.and(ERAuthenticationResponse.AUTHENTICATION_REQUEST.dot(ERAuthenticationRequest.INET_ADDRESS).eq(authRequest.inetAddress()))
				.and(ERTwoFactorAuthenticationResponse.AUTHENTICATION_REQUEST.dot(ERAuthenticationRequest.REQUEST_DATE).greaterThanOrEqualTo(lockDate));
		ERXFetchSpecification<ERTwoFactorAuthenticationResponse> fs = 
				new ERXFetchSpecification<ERTwoFactorAuthenticationResponse>(ERTwoFactorAuthenticationResponse.ENTITY_NAME, q, so);
		NSArray<ERTwoFactorAuthenticationResponse> responses = fs.fetchObjects(authRequest.editingContext());
		if (responses.isEmpty() 
				|| responses.count() < invalidPasswordLimit()
				|| !ERTwoFactorAuthenticationResponse.AUTHENTICATION_FAILED.isFalse().filtered(responses).isEmpty()) {
			return null;
		}
		return responses.lastObject().authenticationRequest().requestDate().plus(lockDuration());
	}

	public WOActionResults authenticate(ERTwoFactorAuthenticationRequest authRequest) {
		DateTime lockDate = lockedUntil(authRequest);
		NSMutableDictionary<String, Object> exceptionContext = null;

		ERTwoFactorAuthenticationConfig config = ERTwoFactorAuthenticationRequest.clazz.authenticationConfig();

		EOEditingContext ec = authRequest.editingContext();
		ERTwoFactorAuthenticationResponse response = ERTwoFactorAuthenticationResponse.clazz.createAndInsertObject(ec);
		response.setAuthenticationRequest(authRequest);

		if (lockDate != null) {
			response.setAuthenticationFailed(Boolean.TRUE);
			response.setAuthenticationFailureType(ERTwoFactorAuthenticationFailure.ACCOUNT_LOCKED);
			// TODO format lockDate and put it into the exceptionContext
		} else {
			EOQualifier q = ERXQ.equals(config.usernameKeyPath(), authRequest.username());
			ERXFetchSpecification<EOEnterpriseObject> fs = new ERXFetchSpecification<EOEnterpriseObject>(
					config.userEntityName(), q, null);
			if (config.storedPasswordKeyPath().indexOf(EOKeyValueCodingAdditions.KeyPathSeparator) != -1) {
				String prefetch = ERXStringUtilities.keyPathWithoutLastProperty(config.storedPasswordKeyPath());
				fs.setPrefetchingRelationshipKeyPaths(new NSArray<String>(prefetch));
			}
			NSArray<EOEnterpriseObject> users = fs.fetchObjects(ec);

			switch (users.count()) {
			case 0:
				response.setAuthenticationFailed(Boolean.TRUE);
				response.setAuthenticationFailureType(ERTwoFactorAuthenticationFailure.UNKNOWN_USERNAME);
				break;
			case 1:
				EOEnterpriseObject eo = users.lastObject();
				EOGlobalID gid = ec.globalIDForObject(eo);
				ERXKeyGlobalID kgid = ERXKeyGlobalID.globalIDForGID((EOKeyGlobalID) gid);
				response.setUserID(kgid);
				if(!config.allowLogin(eo)) {
					response.setAuthenticationFailed(Boolean.TRUE);
					response.setAuthenticationFailureType(ERTwoFactorAuthenticationFailure.ACCOUNT_LOCKED);
				} else if (config.verifyPassword(eo, authRequest.password())) {
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
		}

		try {
			ec.saveChanges();
		} catch (EOGeneralAdaptorException e) {
			// TODO crap...
			throw e;
		}

		WOComponent nextPage;
		if (response.authenticationFailed()) {
			nextPage = config.loginFailurePage();
			ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
			ERXValidationException ex = factory.createCustomException(null, response.authenticationFailureType().name());
			ex.setContext(exceptionContext);
			nextPage.validationFailedWithException(ex, null, null);
		} else {
			nextPage = config.loginSuccessPage();
		}

		return nextPage;
	}

}
