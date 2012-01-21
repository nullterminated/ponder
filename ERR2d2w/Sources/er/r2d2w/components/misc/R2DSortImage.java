package er.r2d2w.components.misc;

import com.webobjects.appserver.WOContext;

import er.directtoweb.components.ERD2WStatelessComponent;
import er.extensions.components.ERXSortOrder;
import er.extensions.foundation.ERXValueUtilities;

public class R2DSortImage extends ERD2WStatelessComponent {
    public R2DSortImage(WOContext context) {
        super(context);
    }
    
    public int sortState() {
    	return ERXValueUtilities.intValue(valueForBinding("sortState"));
    }

	public String imageID() {
		switch(sortState()) {
		case ERXSortOrder.SortedAscending:
			return "sort_asc";
		case ERXSortOrder.SortedDescending:
			return "sort_desc";
		default:
			return "sort_unsort";
		}
	}
}