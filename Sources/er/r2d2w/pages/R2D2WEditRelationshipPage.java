package er.r2d2w.pages;

import com.webobjects.appserver.WOContext;

import er.directtoweb.pages.ERD2WEditRelationshipPage;

public class R2D2WEditRelationshipPage extends ERD2WEditRelationshipPage {
    public R2D2WEditRelationshipPage(WOContext context) {
        super(context);
    }

	public Boolean isEntityWritable() {
		return !isEntityReadOnly();
	}

}