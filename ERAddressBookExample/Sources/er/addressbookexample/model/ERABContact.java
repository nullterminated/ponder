package er.addressbookexample.model;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;

import com.webobjects.eocontrol.EOEditingContext;

import er.datum.model.ERDatum;
import er.datum.model.ERDatum.ERDatumClazz;

public class ERABContact extends er.addressbookexample.model.eogen._ERABContact {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERABContact.class);
	private static final String subtypeValue = "erab_contact";

	public static final ERABContactClazz<ERABContact> clazz = new ERABContactClazz<ERABContact>();

	public static class ERABContactClazz<T extends ERABContact> extends
			er.addressbookexample.model.eogen._ERABContact._ERABContactClazz<T> {
		/* more clazz methods here */
		@Override
		public ERDatumClazz<? extends ERDatum> datumClazzForTypeAndValue(String type, Object value) {
			if (value instanceof String) {
				return ERABStringDatum.clazz;
			} else if (value instanceof LocalDate) {
				return ERABDateDatum.clazz;
			}
			return super.datumClazzForTypeAndValue(type, value);
		}
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
