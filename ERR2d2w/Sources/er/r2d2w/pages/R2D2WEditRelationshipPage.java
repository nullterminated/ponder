package er.r2d2w.pages;

import com.webobjects.appserver.WOContext;

import er.directtoweb.pages.ERD2WEditRelationshipPage;
import er.extensions.foundation.ERXStringUtilities;

public class R2D2WEditRelationshipPage extends ERD2WEditRelationshipPage {

	private String labelID;
	
	public R2D2WEditRelationshipPage(WOContext context) {
        super(context);
    }

	public Boolean isEntityWritable() {
		return !isEntityReadOnly();
	}

	/**
	 * @return the labelID
	 */
	public String labelID() {
		if(labelID == null) {
			labelID = ERXStringUtilities.safeIdentifierName(context().elementID(), "id", '_');
		}
		return labelID;
	}

}