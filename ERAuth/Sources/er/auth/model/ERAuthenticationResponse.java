package er.auth.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

public abstract class ERAuthenticationResponse extends er.auth.model.gen._ERAuthenticationResponse {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(ERAuthenticationResponse.class);

	public static final ERAuthenticationResponseClazz<ERAuthenticationResponse> clazz = new ERAuthenticationResponseClazz<ERAuthenticationResponse>();

	public static class ERAuthenticationResponseClazz<T extends ERAuthenticationResponse> extends er.auth.model.gen._ERAuthenticationResponse._ERAuthenticationResponseClazz<T> {
		/* more clazz methods here */
	}

	/**
	 * Initializes the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
		setSubtype(_subtype());
	}

	protected abstract String _subtype();

}
