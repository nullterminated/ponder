package er.auth.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

import er.auth.processing.ERAuthenticationConfig;
import er.extensions.appserver.ERXRequest;

public abstract class ERAuthenticationRequest extends er.auth.model.gen._ERAuthenticationRequest {
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(ERAuthenticationRequest.class);

	public static final ERAuthenticationRequestClazz<ERAuthenticationRequest> clazz = new ERAuthenticationRequestClazz<ERAuthenticationRequest>();
	public static class ERAuthenticationRequestClazz<T extends ERAuthenticationRequest> extends er.auth.model.gen._ERAuthenticationRequest._ERAuthenticationRequestClazz<T> {
		/* more clazz methods here */
		public ERAuthenticationConfig authenticationConfig() { return null; }
	}

	/**
	 * Initializes the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
		setSubtype(_subtype());
	}

	public abstract void takeValuesFromRequest(ERXRequest request);

	protected abstract String _subtype();

}
