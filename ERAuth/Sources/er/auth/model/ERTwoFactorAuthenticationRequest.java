package er.auth.model;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.webobjects.eocontrol.EOEditingContext;

import er.auth.processing.ERTwoFactorAuthenticationConfig;
import er.extensions.appserver.ERXRequest;

public class ERTwoFactorAuthenticationRequest extends er.auth.model.gen._ERTwoFactorAuthenticationRequest {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(ERTwoFactorAuthenticationRequest.class);
	public static final String PASSWORD_KEY = "password";

	protected transient String password;

	public static final ERTwoFactorAuthenticationRequestClazz<ERTwoFactorAuthenticationRequest> clazz = new ERTwoFactorAuthenticationRequestClazz<ERTwoFactorAuthenticationRequest>();
	public static class ERTwoFactorAuthenticationRequestClazz<T extends ERTwoFactorAuthenticationRequest> extends er.auth.model.gen._ERTwoFactorAuthenticationRequest._ERTwoFactorAuthenticationRequestClazz<T> {
		private ERTwoFactorAuthenticationConfig config;
		
		public ERTwoFactorAuthenticationConfig authenticationConfig() {
			return config;
		}
		
		public void setAuthenticationConfig(ERTwoFactorAuthenticationConfig config) {
			this.config = config;
		}
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

	public String password() {
		willRead();
		return password;
	}

	public void setPassword(String password) {
		willChange();
		this.password = password;
	}

	public void takeValuesFromRequest(ERXRequest request) {
		String username = request.stringFormValueForKey(USERNAME_KEY);
		String password = request.stringFormValueForKey(PASSWORD_KEY);
		String inetAddress = request.remoteHostAddress();
		if (ERXRequest.UNKNOWN_HOST.equals(inetAddress)) {
			inetAddress = null;
		}

		setUsername(username);
		setPassword(password);
		setInetAddress(inetAddress);
		setRequestDate(new DateTime());
	}
}
