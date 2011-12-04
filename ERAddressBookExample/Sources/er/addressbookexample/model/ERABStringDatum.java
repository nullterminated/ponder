package er.addressbookexample.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

public class ERABStringDatum extends er.addressbookexample.model.eogen._ERABStringDatum {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERABStringDatum.class);
	private static final String subtypeValue = "erab_string";

    public static final ERABStringDatumClazz<ERABStringDatum> clazz = new ERABStringDatumClazz<ERABStringDatum>();
    public static class ERABStringDatumClazz<T extends ERABStringDatum> extends er.addressbookexample.model.eogen._ERABStringDatum._ERABStringDatumClazz<T> {
        /* more clazz methods here */
    }

    /**
     * Initializes the EO. This is called when an EO is created, not when it is 
     * inserted into an EC.
     */
    public void init(EOEditingContext ec) {
        super.init(ec);
    }

    public String subtypeValue() {
    	return subtypeValue;
    }
}
