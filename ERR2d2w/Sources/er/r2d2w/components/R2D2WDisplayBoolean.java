package er.r2d2w.components;

import com.webobjects.appserver.WOContext;

import er.directtoweb.components.ERD2WStatelessComponent;
import er.extensions.foundation.ERXValueUtilities;

public class R2D2WDisplayBoolean extends ERD2WStatelessComponent {
    public R2D2WDisplayBoolean(WOContext context) {
        super(context);
    }

	public Boolean isFalse() {
		if(objectPropertyValueIsNonNull()) {
			boolean b = ERXValueUtilities.booleanValue(objectPropertyValue());
			return Boolean.valueOf(!b);
		}
		return Boolean.FALSE;
	}
}