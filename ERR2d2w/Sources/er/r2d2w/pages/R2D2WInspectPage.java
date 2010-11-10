package er.r2d2w.pages;

import com.webobjects.appserver.WOContext;

import er.directtoweb.pages.ERD2WTabInspectPage;
import er.extensions.foundation.ERXValueUtilities;

public class R2D2WInspectPage extends ERD2WTabInspectPage {
	public R2D2WInspectPage(WOContext context) {
		super(context);
	}

	public boolean disableForm() {
		boolean hasForm = ERXValueUtilities.booleanValue(d2wContext().valueForKey("hasForm"));
		return context().isInForm() || !hasForm;
	}
}