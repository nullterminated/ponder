package er.r2d2w.components.misc;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXTabPanel;

public class R2DTabPanel extends ERXTabPanel {
	public R2DTabPanel(WOContext context) {
		super(context);
	}

	public boolean hasTabs() {
		return tabs().count() > 1;
	}
}