package er.r2d2w.components;

import com.webobjects.appserver.WOContext;

import er.directtoweb.components.ERD2WStatelessComponent;

public class R2D2WSimpleHeader extends ERD2WStatelessComponent {
    public R2D2WSimpleHeader(WOContext context) {
        super(context);
    }

	public String headerElementName() {
		if(!d2wContext().frame()) { return "h2"; }
		String parentConfig = (String)d2wContext().valueForKey("parentPageConfiguration");
		if(parentConfig != null && parentConfig.contains("Embedded")) { return "h4"; }
		return "h3";
	}
}