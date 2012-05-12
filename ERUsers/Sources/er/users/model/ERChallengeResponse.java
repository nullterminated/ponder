package er.users.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

public class ERChallengeResponse extends er.users.model.eogen._ERChallengeResponse {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERChallengeResponse.class);

    public static final ERChallengeResponseClazz<ERChallengeResponse> clazz = new ERChallengeResponseClazz<ERChallengeResponse>();
    public static class ERChallengeResponseClazz<T extends ERChallengeResponse> extends er.users.model.eogen._ERChallengeResponse._ERChallengeResponseClazz<T> {
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
