package er.datum.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

public abstract class ERDatum extends er.datum.model.eogen._ERDatum {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERDatum.class);
	
	public static final String VALUE_KEY = "value";

	public static final ERDatumClazz<ERDatum> clazz = new ERDatumClazz<ERDatum>();

	public static class ERDatumClazz<T extends ERDatum> extends er.datum.model.eogen._ERDatum._ERDatumClazz<T> {
		/* more clazz methods here */
	}

	/**
	 * Initializes the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
		setSubtype(subtypeValue());
	}

	public abstract String subtypeValue();
	public abstract Object value();
}
