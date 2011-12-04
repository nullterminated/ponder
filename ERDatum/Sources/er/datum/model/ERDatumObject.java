package er.datum.model;

import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.datum.model.ERDatum.ERDatumClazz;

public abstract class ERDatumObject extends er.datum.model.eogen._ERDatumObject {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERDatumObject.class);

	public static final ERDatumObjectClazz<ERDatumObject> clazz = new ERDatumObjectClazz<ERDatumObject>();

	public static class ERDatumObjectClazz<T extends ERDatumObject> extends
			er.datum.model.eogen._ERDatumObject._ERDatumObjectClazz<T> {
		/* more clazz methods here */
		public ERDatumClazz<? extends ERDatum> datumClazzForTypeAndValue(String type, Object value) {
			return null;
		}
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

	public Object handleQueryWithUnboundKey(String key) {
		NSArray<ERDatum> data = objectData(ERDatum.TYPE.eq(key));
		if(data.count() > 1) {
			return super.handleQueryWithUnboundKey(key);
		}
		if(data.isEmpty()) {
			return null;
		}
		return data.lastObject().value();
	}

	public void handleTakeValueForUnboundKey(Object value, String key) {
		NSArray<ERDatum> data = objectData(ERDatum.TYPE.eq(key));
		if(data.count() > 1) {
			super.handleTakeValueForUnboundKey(value, key);
		}
		if(value == null) {
			if(!data.isEmpty()) {
				data.lastObject().delete();
			}
		} else {
			ERDatumClazz<? extends ERDatum> dclazz = clazz().datumClazzForTypeAndValue(key, value);
			if(dclazz == null) {
				super.handleTakeValueForUnboundKey(value, key);
			} else {
				if(data.isEmpty()) {
					ERDatum d = dclazz.createAndInsertObject(editingContext());
					d.takeValueForKeyPath(key, ERDatum.TYPE_KEY);
					d.addObjectsToBothSidesOfRelationshipWithKey(new NSArray<ERDatumObject>(this), ERDatum.DATUM_OBJECT_KEY);
					d.validateTakeValueForKeyPath(value, ERDatum.VALUE_KEY);
				} else {
					ERDatum d = data.lastObject();
					d.validateTakeValueForKeyPath(value, ERDatum.VALUE_KEY);
				}
			}
		}
	}
}
