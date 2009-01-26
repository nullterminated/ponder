package er.r2d2w.components;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.D2WPage;

import er.directtoweb.components.ERD2WStatelessComponent;
import er.extensions.localization.ERXLocalizer;

public class R2DBreadCrumb extends ERD2WStatelessComponent {
	public R2DBreadCrumb(WOContext context) {
		super(context);
	}

	public Boolean hasMoreCrumbs() {
		D2WPage page = (D2WPage) valueForBinding("page");
		WOComponent nextPage = page.nextPage();
		if (nextPage instanceof D2WPage) {
			D2WPage d2wPage = (D2WPage) nextPage;
			return (d2wPage.nextPage() != null);
		}
		return false;
	}
	
	public String nextPageString() {
		D2WPage page = (D2WPage) valueForBinding("page");
		WOComponent nextPage = page.nextPage();
		if (nextPage instanceof D2WPage) {
			D2WContext c = (D2WContext) page.valueForKeyPath("nextPage.d2wContext");
			StringBuilder sb = new StringBuilder("ERD2W.tasks.").append(c.task().toString());
			Object subtask = c.valueForKey("subTask");
			if(subtask!=null) {
				sb.append(".").append(subtask.toString());
			}
			return ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject(sb.toString(), c);
		}
		// TODO return something localized and more appropriate
		return nextPage.getClass().getSimpleName();
	}

}