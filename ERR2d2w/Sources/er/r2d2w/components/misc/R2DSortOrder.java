package er.r2d2w.components.misc;

import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WContext;

import er.extensions.components.ERXSortOrder;
import er.extensions.localization.ERXLocalizer;

public class R2DSortOrder extends ERXSortOrder {
    public R2DSortOrder(WOContext context) {
        super(context);
    }

    public boolean isAscending() {
    	return (currentState() == SortedAscending);
    }
    
    public boolean isDescending() {
    	return (currentState() == SortedDescending);
    }
    
    public boolean isUnsorted() {
    	return (currentState() == Unsorted);
    }
    
    public String helpString() {
    	D2WContext c = (D2WContext)valueForBinding("d2wContext");
    	ERXLocalizer loc = ERXLocalizer.currentLocalizer();
    	int state = currentState();
    	String helpString = (state == SortedAscending)?
    			loc.localizedTemplateStringForKeyWithObject("R2DSortOrder.sortDesc", c):
    			loc.localizedTemplateStringForKeyWithObject("R2DSortOrder.sortAsc", c);
    	return helpString;
    }
    
}