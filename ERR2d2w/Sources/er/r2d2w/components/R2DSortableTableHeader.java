package er.r2d2w.components;

import com.webobjects.appserver.WOContext;

import er.directtoweb.components.ERD2WStatelessComponent;
import er.extensions.foundation.ERXValueUtilities;

public class R2DSortableTableHeader extends ERD2WStatelessComponent {
    public R2DSortableTableHeader(WOContext context) {
        super(context);
    }

	public boolean isNotSortable() {
		return !ERXValueUtilities.booleanValueWithDefault(d2wContext().valueForKey("propertyIsSortable"), false);
	}
}