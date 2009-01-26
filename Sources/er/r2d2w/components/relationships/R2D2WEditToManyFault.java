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
    	return (browserList() != null && browserList().count() > 0)?_r2FieldLabelID():null;
    }

    private static final String _COMPONENT_CLASS = "toMany";
	private String labelID;
    
    public String componentClasses() {
    	return _COMPONENT_CLASS;
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