package er.r2d2w.components;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSValidation;

import er.directtoweb.components.ERDCustomEditComponent;
import er.extensions.localization.ERXLocalizer;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;

public class R2D2WEditDate extends ERDCustomEditComponent {
	public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
	private String dateString;

	public R2D2WEditDate(WOContext context) {
		super(context);
	}

	public void appendToResponse(WOResponse r, WOContext c) {
		NSTimestamp date = (NSTimestamp) objectPropertyValue();
		if (date != null) {
			try {
				dateString = dateFormatter().format(date);
			} catch (IllegalArgumentException nsfe) {
				log.debug("Failed to format date value:" + nsfe);
			}
		} else {
			dateString = null;
		}
		super.appendToResponse(r, c);
	}

	public Object value() {
		return dateString;
	}

	public void takeValuesFromRequest(WORequest request, WOContext context) {
		super.takeValuesFromRequest(request, context);
		NSTimestamp date = null;
		try {
			if (dateString != null) {
				Date d = dateFormatter().parse(dateString);
				date = new NSTimestamp(d);
			}
			if (object() != null) {
				object().validateTakeValueForKeyPath(date, key());
			}
		} catch (java.text.ParseException npse) {
			log.debug("java.text.ParseException:" + npse);
			ERXValidationException v = ERXValidationFactory.defaultFactory().createException(object(), key(), dateString, "InvalidDateFormatException");
			parent().validationFailedWithException(v, date, key());
		} catch (NSValidation.ValidationException v) {
			log.debug("NSValidation.ValidationException:" + v);
			parent().validationFailedWithException(v, date, key());
		} catch (Exception e) {
			log.debug("Exception:" + e);
			parent().validationFailedWithException(e, date, key());
		}
	}

	public SimpleDateFormat dateFormatter() {
		return new SimpleDateFormat(formatPattern());
	}

	public String formatPattern() {
		String format = (String)d2wContext().valueForKey("formatter");
		if(format == null) {
			format = ERXLocalizer.currentLocalizer().localizedStringForKey("R2D2W.dateFormat");
		}
		if(format == null) {
			format = DEFAULT_DATE_FORMAT;
		}
		return format;
	}

	/**
	 * @return the dateString
	 */
	public String dateString() {
		return dateString;
	}

	/**
	 * @param dateString
	 *            the dateString to set
	 */
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	
    private static final String _COMPONENT_CLASS = "date";
	private String labelID;
    
    public String componentClasses() {
    	return _COMPONENT_CLASS;
    }
    
    public void reset() {
    	super.reset();
    	dateString = null;
    	labelID = null;
    }

	/**
	 * @return the _r2FieldLabelID
	 */
	public String _r2FieldLabelID() {
		return labelID;
	}

	/**
	 * @param labelID the _r2FieldLabelID to set
	 */
	public void set_r2FieldLabelID(String labelID) {
		this.labelID = labelID;
	}

}