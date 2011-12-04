package er.addressbookexample.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

import er.datum.model.ERDatum;
import er.datum.model.ERDatum.ERDatumClazz;

public class ERABAddress extends er.addressbookexample.model.eogen._ERABAddress {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERABAddress.class);
	private static final String subtypeValue = "erab_address";

	public static final ERABAddressClazz<ERABAddress> clazz = new ERABAddressClazz<ERABAddress>();

	public static class ERABAddressClazz<T extends ERABAddress> extends er.addressbookexample.model.eogen._ERABAddress._ERABAddressClazz<T> {
		/* more clazz methods here */
		@Override
		public ERDatumClazz<? extends ERDatum> datumClazzForTypeAndValue(String type, Object value) {
			if (value instanceof String) {
				return ERABStringDatum.clazz;
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
