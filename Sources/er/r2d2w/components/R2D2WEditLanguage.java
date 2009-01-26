package er.r2d2w.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEnterpriseObject;

import er.directtoweb.components.ERDCustomEditComponent;
import er.extensions.appserver.ERXSession;
import er.extensions.foundation.ERXValueUtilities;

public class R2D2WEditLanguage extends ERDCustomEditComponent {

	public R2D2WEditLanguage(WOContext context) {
        super(context);
    }

    private static final String _COMPONENT_CLASS = "string";
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

	public String noSelectionString() {
		String result = null;
		if(ERXValueUtilities.booleanValue(d2wContext().valueForKeyPath("smartAttribute.allowsNull"))) {
			result = (String)d2wContext().valueForKey("noSelectionStringValue");
		}
		return result;
	}

	public void setObjectPropertyValue(Object newValue) {
		super.setObjectPropertyValue(newValue);
		if(ERXValueUtilities.booleanValue(d2wContext().valueForKey("updatesCurrentLanguage"))) {
			ERXSession session = (ERXSession)session();
			EOEnterpriseObject user = (EOEnterpriseObject)session.objectForKey("user");
			if(user != null) {
				EOEnterpriseObject localUser = EOUtilities.localInstanceOfObject(object().editingContext(), user);
				if(object().equals(localUser)) { session.setLanguage((String)newValue); }
			}
		}
	}
}