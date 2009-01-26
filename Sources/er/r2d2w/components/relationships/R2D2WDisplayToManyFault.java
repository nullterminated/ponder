package er.r2d2w.components.relationships;

import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WDisplayToManyFault;
import com.webobjects.eocontrol.EOEnterpriseObject;

import er.extensions.localization.ERXLocalizer;
import er.r2d2w.ERR2d2w;

public class R2D2WDisplayToManyFault extends D2WDisplayToManyFault {
	public R2D2WDisplayToManyFault(WOContext context) {
		super(context);
	}

	public String helpString() {
		return ERXLocalizer.currentLocalizer()
				.localizedTemplateStringForKeyWithObject(
						"R2D2W.displayToManyFault.helpString", d2wContext());
	}

	public String relationshipCount() {
		EOEnterpriseObject eo = (EOEnterpriseObject) valueForKey("object");
		String derivedCountKey = new StringBuilder(d2wContext().propertyKey())
				.append(ERR2d2w.DERIVED_COUNT).toString();
		Object att, count;
		att = d2wContext().entity().attributeNamed(derivedCountKey);
		count = (att == null) ? null : eo.valueForKey(derivedCountKey);
		return (count == null ? d2wContext().displayNameForProperty() : count
				.toString());
	}
}