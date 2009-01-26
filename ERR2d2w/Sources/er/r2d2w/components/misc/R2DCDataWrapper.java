package er.r2d2w.components.misc;

import com.webobjects.appserver.WOContext;
import er.extensions.components.ERXStatelessComponent;
import er.extensions.foundation.ERXValueUtilities;

public class R2DCDataWrapper extends ERXStatelessComponent {
	
	public static final String CDATA_START = "<![CDATA[";
	public static final String CDATA_END = "]]>";
	public static final String CDATA_ESCAPE = "]]]]><![CDATA[>";
	
    public R2DCDataWrapper(WOContext context) {
        super(context);
    }

	public String stringValue() {
		String result = (String)valueForBinding("value");
		Boolean escapeHTML = escapeValue();
		if (!escapeHTML && result.contains(CDATA_END)) {
			result = result.replace(CDATA_END, CDATA_ESCAPE);
		}
		return result;
	}
	
	public Boolean escapeValue() {
		return ERXValueUtilities.booleanValueWithDefault(valueForBinding("escapeHTML"), Boolean.TRUE);
	}

	// Just keeping the WOD parser happy here.
	// It complains if I try to use the constant fields directly.
	public String cdataEnd() {
		return CDATA_END;
	}
	
	public String cdataStart() {
		return CDATA_START;
	}
}