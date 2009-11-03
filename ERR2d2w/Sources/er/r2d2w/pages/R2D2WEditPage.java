package er.r2d2w.pages;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;

import er.directtoweb.pages.ERD2WInspectPage;

public class R2D2WEditPage extends ERD2WInspectPage {
    public R2D2WEditPage(WOContext context) {
        super(context);
    }
    
    /**
     * Overridden from parent to refresh the saved object in the EC
     * to get values for derived attributes.
     */
    public WOComponent submitAction() throws Throwable {
    	WOComponent result = super.submitAction();
    	if(errorMessages.count() == 0) {
    		EOEditingContext ec = object().editingContext();
    		ec.refreshObject(object());
    	}
    	return result;
    }
}