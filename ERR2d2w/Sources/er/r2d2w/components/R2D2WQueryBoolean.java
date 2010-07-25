package er.r2d2w.components;

import com.webobjects.appserver.WOContext;

import er.directtoweb.components.bool.ERD2WCustomQueryBoolean;
import er.extensions.foundation.ERXStringUtilities;

public class R2D2WQueryBoolean extends ERD2WCustomQueryBoolean {
	
    private String labelID;

	public R2D2WQueryBoolean(WOContext context) {
        super(context);
    }
    
	public void reset() {
		labelID = null;
		super.reset();
	}
	
	/**
	 * @return the labelID
	 */
	public String labelID() {
		if(labelID == null) {
			labelID = ERXStringUtilities.safeIdentifierName(context().elementID(),"id",'_');
		}
		return labelID;
	}

}