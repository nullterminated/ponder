package er.auth.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

public class EREntityPermission extends er.auth.model.gen._EREntityPermission {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(EREntityPermission.class);

    public static final EREntityPermissionClazz<EREntityPermission> clazz = new EREntityPermissionClazz<EREntityPermission>();
    public static class EREntityPermissionClazz<T extends EREntityPermission> extends er.auth.model.gen._EREntityPermission._EREntityPermissionClazz<T> {
        /* more clazz methods here */
    }

    /**
     * Initializes the EO. This is called when an EO is created, not when it is 
     * inserted into an EC.
     */
    public void init(EOEditingContext ec) {
        super.init(ec);
        setAllowCreate(Boolean.FALSE);
        setAllowDelete(Boolean.FALSE);
        setAllowQuery(Boolean.FALSE);
        setAllowRead(Boolean.FALSE);
        setAllowUpdate(Boolean.FALSE);
    }

}
