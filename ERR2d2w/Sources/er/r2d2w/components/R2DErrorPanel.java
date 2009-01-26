package er.r2d2w.components;

import com.webobjects.appserver.WOContext;

import er.directtoweb.components.buttons.ERDActionButton;
import er.directtoweb.pages.ERD2WPage;

public class R2DErrorPanel extends ERDActionButton {
    public R2DErrorPanel(WOContext context) {
        super(context);
    }
    
    public ERD2WPage parentERD2WPage() {
    	return (ERD2WPage)super.parentD2WPage();
    }
}