package er.r2d2w.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WEditLargeString;

public class R2D2WEditLargeString extends D2WEditLargeString {
    
	public R2D2WEditLargeString(WOContext context) {
        super(context);
    }

    private static final String _COMPONENT_CLASS = "largeString";
	private static final String _FIELD_LABEL_ID = "_r2FieldLabelID";
	private String labelID;

	/**
     * It seems the super component's version of this method assumes any value 
     * is the one being set for the object.  Since we want to store a 
     * label id here, the method has to be overridden.
     * @see com.webobjects.directtoweb.EditComponent#validateTakeValueForKeyPath(java.lang.Object, java.lang.String)
     */
    public Object validateTakeValueForKeyPath(Object value, String key) {
    	if(_FIELD_LABEL_ID.equals(key)) {
    		set_r2FieldLabelID(value.toString());
    		return value;
    	}
    	return super.validateTakeValueForKeyPath(value, key);
    }
    
    public String componentClasses() {
    	return _COMPONENT_CLASS;
    }
    
    public void reset() {
    	super.reset();
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