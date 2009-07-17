package er.r2d2w.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WQueryDateRange;

import er.extensions.foundation.ERXStringUtilities;

public class R2D2WQueryDateRange extends D2WQueryDateRange {
    private String maxID;
	private String minID;

	public R2D2WQueryDateRange(WOContext context) {
        super(context);
    }

	public void reset() {
		maxID = null;
		minID = null;
		super.reset();
	}

	/**
	 * @return the maxID
	 */
	public String maxID() {
		if(maxID == null) {
			maxID = ERXStringUtilities.safeIdentifierName(context().elementID(),"id",'_');
		}
		return maxID;
	}

	/**
	 * @return the minID
	 */
	public String minID() {
		if(minID == null) {
			minID = ERXStringUtilities.safeIdentifierName(context().elementID(),"id",'_');
		}
		return minID;
	}

}