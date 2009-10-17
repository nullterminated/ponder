package er.r2d2w.components.misc;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXCheckboxMatrix;

public class R2DCheckboxFieldset extends ERXCheckboxMatrix {
    public R2DCheckboxFieldset(WOContext context) {
        super(context);
    }

    private String labelID;
	
	public void reset() {
		labelID = null;
		super.reset();
	}

	/**
	 * @return the labelID
	 */
	public String labelID() {
		return labelID;
	}

	public String generateID() {
		labelID = "id" + context().elementID();
		return labelID;
	}
	
	public boolean checked() {
		boolean checked = selections() != null && selections().containsObject(currentItem);
		return checked;
	}
	
	public boolean disabled() {
		boolean disabled = booleanValueForBinding("disabled", false);
		return disabled;
	}
	
    public String checkboxChecked() {
        return checked() ? "checked" : null;
    }
    
    public String checkboxDisabled() {
    	return disabled() ? "disabled" : null;
    }

}