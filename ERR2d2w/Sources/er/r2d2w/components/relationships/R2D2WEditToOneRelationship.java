package er.r2d2w.components.relationships;

import com.webobjects.appserver.WOContext;

import er.directtoweb.components.relationships.ERD2WEditToOneRelationship;
import er.extensions.foundation.ERXStringUtilities;

public class R2D2WEditToOneRelationship extends ERD2WEditToOneRelationship {
    public R2D2WEditToOneRelationship(WOContext context) {
        super(context);
    }

	private String labelID;

	public void reset() {
		labelID = null;
		super.reset();
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