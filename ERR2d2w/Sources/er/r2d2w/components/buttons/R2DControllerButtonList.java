package er.r2d2w.components.buttons;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSDictionary;

import er.directtoweb.components.buttons.ERDControllerButton;

public class R2DControllerButtonList extends ERDControllerButton {
    public R2DControllerButtonList(WOContext context) {
        super(context);
    }
    
    /**
     * Overriden from the parent to set the branch in the D2WContext.
     * This is to allow custom button contents based on the branch.
     */
    @SuppressWarnings("rawtypes")
	public void setBranch(NSDictionary branch) {
    	super.setBranch(branch);
    	d2wContext().takeValueForKey(branch, "branch");
    }
}