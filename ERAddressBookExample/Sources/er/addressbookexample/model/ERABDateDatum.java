package er.addressbookexample.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

public class ERABDateDatum extends er.addressbookexample.model.eogen._ERABDateDatum {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERABDateDatum.class);
	private static final String subtypeValue = "erab_date";

	public static final ERABDateDatumClazz<ERABDateDatum> clazz = new ERABDateDatumClazz<ERABDateDatum>();

	public static class ERABDateDatumClazz<T extends ERABDateDatum> extends
			er.addressbookexample.model.eogen._ERABDateDatum._ERABDateDatumClazz<T> {
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
