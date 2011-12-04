package er.addressbookexample.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

public class ERABEmailDatum extends er.addressbookexample.model.eogen._ERABEmailDatum {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERABEmailDatum.class);
	private static final String subtypeValue = "erab_email";

	public static final ERABEmailDatumClazz<ERABEmailDatum> clazz = new ERABEmailDatumClazz<ERABEmailDatum>();

	public static class ERABEmailDatumClazz<T extends ERABEmailDatum> extends
			er.addressbookexample.model.eogen._ERABEmailDatum._ERABEmailDatumClazz<T> {
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
