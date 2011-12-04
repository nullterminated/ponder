package er.addressbookexample.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

public class ERABGroup extends er.addressbookexample.model.eogen._ERABGroup {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERABGroup.class);

    public static final ERABGroupClazz<ERABGroup> clazz = new ERABGroupClazz<ERABGroup>();
    public static class ERABGroupClazz<T extends ERABGroup> extends er.addressbookexample.model.eogen._ERABGroup._ERABGroupClazz<T> {
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
