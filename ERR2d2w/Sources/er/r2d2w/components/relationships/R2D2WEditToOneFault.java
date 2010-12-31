package er.r2d2w.components.relationships;

import com.webobjects.appserver.WOContext;

import er.directtoweb.components.relationships.ERD2WEditToOneFault;
import er.extensions.localization.ERXLocalizer;

public class R2D2WEditToOneFault extends ERD2WEditToOneFault {
    public R2D2WEditToOneFault(WOContext context) {
        super(context);
    }

    public String helpString() {
    	String helpString = (String)d2wContext().valueForKey("tooltip");
    	if(helpString==null) {
    		helpString = ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("R2D2W.editToOneFault.helpString", d2wContext());
    	}
    	return helpString;
    }

    private static final String _COMPONENT_CLASS = "toOne";
    
    public String componentClasses() {
    	return _COMPONENT_CLASS;
    }

}