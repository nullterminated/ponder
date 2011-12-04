package er.addressbookexample.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

public class ERABPhoneDatum extends er.addressbookexample.model.eogen._ERABPhoneDatum {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERABPhoneDatum.class);
	private static final String subtypeValue = "erab_phone";

	public static final ERABPhoneDatumClazz<ERABPhoneDatum> clazz = new ERABPhoneDatumClazz<ERABPhoneDatum>();

	public static class ERABPhoneDatumClazz<T extends ERABPhoneDatum> extends
			er.addressbookexample.model.eogen._ERABPhoneDatum._ERABPhoneDatumClazz<T> {
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
