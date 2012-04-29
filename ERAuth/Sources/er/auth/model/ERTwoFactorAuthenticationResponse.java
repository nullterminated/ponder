package er.auth.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;

public class ERTwoFactorAuthenticationResponse extends er.auth.model.gen._ERTwoFactorAuthenticationResponse {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Key for non class property twoFactorAuthenticationRequest
	 */
	public static final String TWO_FACTOR_AUTHENTICATION_REQUEST_KEY = "twoFactorAuthenticationRequest";
	
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(ERTwoFactorAuthenticationResponse.class);

	public static final ERTwoFactorAuthenticationResponseClazz<ERTwoFactorAuthenticationResponse> clazz = new ERTwoFactorAuthenticationResponseClazz<ERTwoFactorAuthenticationResponse>();
	public static class ERTwoFactorAuthenticationResponseClazz<T extends ERTwoFactorAuthenticationResponse> extends er.auth.model.gen._ERTwoFactorAuthenticationResponse._ERTwoFactorAuthenticationResponseClazz<T> {
		/* more clazz methods here */
	}

	/**
	 * Initializes the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
	}

	protected String _subtype() {
		return ENTITY_NAME;
	}
	
	/**
	 * Enforces the requirement that only two factor requests may be related to two factor responses.
	 * @param request the two factor request
	 * @return the two factor request
	 */
	public ERAuthenticationRequest validateAuthenticationRequest(ERAuthenticationRequest request) {
		if(request instanceof ERTwoFactorAuthenticationRequest) {
			return request;
		}
		ERXValidationException ex = ERXValidationFactory.defaultFactory().createException(this, AUTHENTICATION_REQUEST_KEY, request, ERXValidationException.InvalidValueException);
		throw ex;
	}
}
