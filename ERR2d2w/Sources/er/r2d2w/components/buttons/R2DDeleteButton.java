package er.r2d2w.components.buttons;

import com.webobjects.appserver.WOContext;

import er.directtoweb.components.buttons.ERDDeleteButton;

public class R2DDeleteButton extends ERDDeleteButton {

	public R2DDeleteButton(WOContext context) {
        super(context);
    }

	public Boolean disabled() {
		return !canDelete();
	}

}