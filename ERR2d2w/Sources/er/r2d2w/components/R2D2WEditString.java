package er.r2d2w.components;

import java.text.Format;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSValidation;

import er.directtoweb.components.strings.ERD2WEditString;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.validation.ERXValidationFactory;

public class R2D2WEditString extends ERD2WEditString {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(R2D2WEditString.class);

	public R2D2WEditString(WOContext context) {
		super(context);
	}

	private static final String _COMPONENT_CLASS = "string";
	private String labelID;

	public String componentClasses() {
		return _COMPONENT_CLASS;
	}

	public void reset() {
		super.reset();
		labelID = null;
	}

	public String labelID() {
		if (labelID == null) {
			labelID = ERXStringUtilities.safeIdentifierName(context().elementID(), "id", '_');
		}
		return labelID;
	}

	public Format formatObject() {
		return (Format) d2wContext().valueForKey("formatObject");
	}
	
	public String formatExceptionKey() {
		String formatExceptionKey = (String) d2wContext().valueForKey("formatExceptionKey");
		formatExceptionKey = StringUtils.defaultIfEmpty(formatExceptionKey, "InvalidValueException");
		return formatExceptionKey;
	}
	
	public Object formatStringValue(String value) throws NSValidation.ValidationException {
		Format formatObject = formatObject();
		if(formatObject != null) {
			try {
				Object result = formatObject.parseObject(value);
				return result;
			} catch (Exception e) {
	            log.debug("Unable to parse value: " + value + " in " + propertyKey());
	            throw ERXValidationFactory.defaultFactory().createException(object(), propertyKey(), value, formatExceptionKey());
			}
		}
		return value;
	}

	public Object validateTakeValueForKeyPath(Object value, String keyPath) throws NSValidation.ValidationException {
		Object formatValue = value;
		if (value instanceof String) {
			formatValue = formatStringValue((String)value);
		}
		return super.validateTakeValueForKeyPath(formatValue, keyPath);
	}
}