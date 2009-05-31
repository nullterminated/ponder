package er.r2d2w.components;

import com.webobjects.appserver.WOContext;

import er.directtoweb.components.strings.ERD2WEditString;

public class R2D2WEditString extends ERD2WEditString {
	
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
		if(labelID == null) {
			labelID = "id" + context().elementID();
		}
		return labelID;
	}

}