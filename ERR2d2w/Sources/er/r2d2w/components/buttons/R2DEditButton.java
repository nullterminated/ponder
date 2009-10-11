package er.r2d2w.components.buttons;

import com.webobjects.appserver.WOContext;

import er.directtoweb.components.buttons.ERDEditButton;

public class R2DEditButton extends ERDEditButton {
    public R2DEditButton(WOContext context) {
        super(context);
    }
    
	public Boolean isNotEditable() {
		return !isEditable();
	}

}