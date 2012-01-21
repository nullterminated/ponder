package er.r2d2w.components.misc;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXBooleanSelector;
import er.extensions.foundation.ERXStringUtilities;

public class R2DBooleanSelector extends ERXBooleanSelector {
	private String labelID;
	
	public R2DBooleanSelector(WOContext context) {
		super(context);
	}
	
	public String labelID() {
		if(labelID == null) {
			labelID = ERXStringUtilities.safeIdentifierName(context().elementID(), "id", '_');
		}
		return labelID;
	}
	
	@Override
	public void reset() {
		super.reset();
		labelID = null;
	}
}