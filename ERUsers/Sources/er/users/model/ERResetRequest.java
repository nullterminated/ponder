package er.users.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

public class ERResetRequest extends er.users.model.eogen._ERResetRequest {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERResetRequest.class);

    public static final ERResetRequestClazz<ERResetRequest> clazz = new ERResetRequestClazz<ERResetRequest>();
    public static class ERResetRequestClazz<T extends ERResetRequest> extends er.users.model.eogen._ERResetRequest._ERResetRequestClazz<T> {
        /* more clazz methods here */
    }

    /**
     * Initializes the EO. This is called when an EO is created, not when it is 
     * inserted into an EC.
     */
    public void init(EOEditingContext ec) {
        super.init(ec);
    }

}
