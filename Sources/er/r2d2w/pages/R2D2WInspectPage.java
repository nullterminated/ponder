package er.r2d2w.pages;

import com.webobjects.appserver.WOContext;

import er.directtoweb.pages.ERD2WInspectPage;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.localization.ERXLocalizer;

public class R2D2WInspectPage extends ERD2WInspectPage {
	public R2D2WInspectPage(WOContext context) {
		super(context);
	}

	public Boolean omitAttributeWrapper() {
		return ERXStringUtilities.stringIsNullOrEmpty((String)d2wContext().valueForKey("attributeWrapper"));
	}

	public String emptyObjectMessage() {
		return ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("R2D2WInspectPage.emptyObjectMessage", d2wContext());
	}
}