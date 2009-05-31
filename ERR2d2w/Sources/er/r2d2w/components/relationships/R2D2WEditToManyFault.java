package er.r2d2w.components.relationships;

import com.webobjects.appserver.WOContext;

import er.directtoweb.components.relationships.ERD2WEditToManyFault;
import er.extensions.localization.ERXLocalizer;

public class R2D2WEditToManyFault extends ERD2WEditToManyFault {
    public R2D2WEditToManyFault(WOContext context) {
        super(context);
    }

    public String helpString() {
    	String helpString = (String)d2wContext().valueForKey("inputInfo");
    	if(helpString==null) {
    		helpString = ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("R2D2W.editToManyFault.helpString", d2wContext());
    	}
    	return helpString;
    }
    
    public String labelFor() {
    	return (browserList() != null && browserList().count() > 0)?labelID():null;
    }

    private static final String _COMPONENT_CLASS = "toMany";
	private String labelID;
    
    public String componentClasses() {
    	return _COMPONENT_CLASS;
    }
    
	public String labelID() {
		if(labelID == null) {
			labelID = "id" + context().elementID();
		}
		return labelID;
	}

}