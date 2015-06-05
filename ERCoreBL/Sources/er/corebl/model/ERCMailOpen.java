package er.corebl.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

public class ERCMailOpen extends er.corebl.model.eogen._ERCMailOpen {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERCMailOpen.class);

    public static final ERCMailOpenClazz<ERCMailOpen> clazz = new ERCMailOpenClazz<ERCMailOpen>();
    public static class ERCMailOpenClazz<T extends ERCMailOpen> extends er.corebl.model.eogen._ERCMailOpen._ERCMailOpenClazz<T> {
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
