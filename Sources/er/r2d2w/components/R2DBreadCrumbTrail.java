package er.r2d2w.components;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WPage;

import er.directtoweb.components.ERD2WStatelessComponent;

public class R2DBreadCrumbTrail extends ERD2WStatelessComponent {
    public R2DBreadCrumbTrail(WOContext context) {
        super(context);
    }

	public Boolean showTrail() {
		WOComponent page = (WOComponent) valueForBinding("page");
		if (page instanceof D2WPage) {
			D2WPage d2wPage = (D2WPage) page;
			return (d2wPage.nextPage() != null);
		}
		return false;
	}
}