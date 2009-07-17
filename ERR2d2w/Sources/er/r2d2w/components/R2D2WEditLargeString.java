package er.r2d2w.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WEditLargeString;

import er.extensions.foundation.ERXStringUtilities;

public class R2D2WEditLargeString extends D2WEditLargeString {
    
	public R2D2WEditLargeString(WOContext context) {
        super(context);
    }

    private static final String _COMPONENT_CLASS = "largeString";
	private String labelID;

    public String componentClasses() {
    	return _COMPONENT_CLASS;
    }
    
    public void reset() {
    	super.reset();
    	labelID = null;
    }

	public String labelID() {
		if(labelID == null) {
			labelID = ERXStringUtilities.safeIdentifierName(context().elementID(), "id", '_');
		}
		return labelID;
	}

}