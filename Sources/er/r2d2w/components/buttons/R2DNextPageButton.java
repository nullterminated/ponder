package er.r2d2w.components.buttons;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WPage;

import er.directtoweb.components.buttons.ERDActionButton;

public class R2DNextPageButton extends ERDActionButton {
    public R2DNextPageButton(WOContext context) {
        super(context);
    }

	public WOActionResults nextPageAction() {
		return nextPageInPage(parentD2WPage());
	}

	public Boolean hasNextPage() {
		Boolean result = Boolean.FALSE;
		D2WPage page = parentD2WPage();
		if(page != null) {
			if(page.nextPageDelegate() != null || page.nextPage() != null) {
				result = Boolean.TRUE;
			}
		}
		return result;
	}
}