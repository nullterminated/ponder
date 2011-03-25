package er.auth.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

public class ERTwoFactorAuthenticationResponse extends er.auth.model.gen._ERTwoFactorAuthenticationResponse {
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
}
