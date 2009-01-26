package er.r2d2w.components.buttons;

import com.webobjects.appserver.WOContext;
import er.directtoweb.components.buttons.ERDEditButton;
import er.extensions.eof.ERXGuardedObjectInterface;
import er.extensions.foundation.ERXValueUtilities;

public class R2DEditButton extends ERDEditButton {
    public R2DEditButton(WOContext context) {
        super(context);
    }
    
    public boolean isEditable() {
        boolean result = ERXValueUtilities.booleanValue(d2wContext().valueForKey("isEntityEditable"));
        Object o = object();
        if(o == null) {
        	result = false; //CHECKME set a 'create' flag perhaps?
        } else if (o instanceof ERXGuardedObjectInterface) {
            result = result && ((ERXGuardedObjectInterface)o).canUpdate();
        }
        return result;
    }

}