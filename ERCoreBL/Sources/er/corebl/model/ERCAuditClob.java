package er.corebl.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

public class ERCAuditClob extends er.corebl.model.eogen._ERCAuditClob {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERCAuditClob.class);

    public static final ERCAuditClobClazz<ERCAuditClob> clazz = new ERCAuditClobClazz<ERCAuditClob>();
    public static class ERCAuditClobClazz<T extends ERCAuditClob> extends er.corebl.model.eogen._ERCAuditClob._ERCAuditClobClazz<T> {
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
