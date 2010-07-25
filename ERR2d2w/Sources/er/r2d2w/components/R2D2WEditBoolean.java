package er.r2d2w.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WEditBoolean;
import com.webobjects.foundation.NSArray;

import er.extensions.foundation.ERXStringUtilities;

public class R2D2WEditBoolean extends D2WEditBoolean {
    public R2D2WEditBoolean(WOContext context) {
        super(context);
    }

    private static final String _COMPONENT_CLASS = "boolean";
	private String labelID;
	private NSArray<String> _choicesNames;

	public String componentClasses() {
    	return _COMPONENT_CLASS;
    }
    
    public void reset() {
    	super.reset();
    	labelID = null;
        _choicesNames = null;
    }

	public String labelID() {
		if(labelID == null) {
			labelID = ERXStringUtilities.safeIdentifierName(context().elementID(), "id", '_');
		}
		return labelID;
	}

    public NSArray<String> choicesNames() {
        if (_choicesNames == null)
            _choicesNames = (NSArray)d2wContext().valueForKey("choicesNames");
        return _choicesNames;
    }

    public String stringForYes() {
        return choicesNames().objectAtIndex(0);
    }
    
    public String stringForNo() {
        return choicesNames().objectAtIndex(1);
    }
    
    public String stringForNull() {
        if(choicesNames().count() > 2) {
            return choicesNames().objectAtIndex(2);
        }
        return null;
    }
    
}