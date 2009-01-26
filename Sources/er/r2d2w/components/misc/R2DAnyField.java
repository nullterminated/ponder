package er.r2d2w.components.misc;

import com.webobjects.appserver.WOContext;
import com.webobjects.woextensions.WOAnyField;

import er.extensions.localization.ERXLocalizer;

public class R2DAnyField extends WOAnyField {
    
	public R2DAnyField(WOContext context) {
        super(context);
    }
    
	public String localizedOperator() {
		return ERXLocalizer.currentLocalizer().localizedStringForKey(selectedOperatorItem);
	}
}