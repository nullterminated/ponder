package er.r2d2w.components;

import java.text.Format;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSValidation;

import er.directtoweb.components.ERD2WStatelessComponent;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;

public class R2D2WEditDate extends ERD2WStatelessComponent {
	private static final Logger log = Logger.getLogger(R2D2WEditDate.class);
    private static final String _COMPONENT_CLASS = "date";

    private String labelID;
	private String dateString;

	public R2D2WEditDate(WOContext context) {
		super(context);
	}

    public void reset() {
    	super.reset();
    	dateString = null;
    	labelID = null;
    }

	public void appendToResponse(WOResponse r, WOContext c) {
		Object date = objectPropertyValue();
		if (date != null) {
			setDateString(dateFormatter().format(date));
		} else {
			setDateString(null);
		}
		super.appendToResponse(r, c);
	}

	public void takeValuesFromRequest(WORequest request, WOContext context) {
		super.takeValuesFromRequest(request, context);
		Object obj = null;
		try {
			if (dateString() != null) {
				obj = dateFormatter().parseObject(dateString());
			}
			if (object() != null) {
				object().validateTakeValueForKeyPath(obj, propertyKey());
			}
		} catch (java.text.ParseException npse) {
			log.debug("java.text.ParseException:" + npse);
			ERXValidationException v = ERXValidationFactory.defaultFactory().createException(object(), propertyKey(), dateString(), "InvalidDateFormatException");
			parent().validationFailedWithException(v, obj, propertyKey());
		} catch (NSValidation.ValidationException v) {
			log.debug("NSValidation.ValidationException:" + v);
			parent().validationFailedWithException(v, obj, propertyKey());
		} catch (Exception e) {
			//FIXME this does not seem to work
//			log.debug("Exception:" + e);
//			parent().validationFailedWithException(e, obj, propertyKey());
			throw NSForwardException._runtimeExceptionForThrowable(e);
		}
	}

	public Format dateFormatter() {
		Format result = (Format)d2wContext().valueForKey("formatObject");
		return result;
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
	
    public String componentClasses() {
    	return _COMPONENT_CLASS;
    }
    
	public String labelID() {
		if(labelID == null) {
			labelID = ERXStringUtilities.safeIdentifierName(context().elementID(), "id", '_');
		}
		return labelID;
	}

}