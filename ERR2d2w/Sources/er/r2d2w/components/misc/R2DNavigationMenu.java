package er.r2d2w.components.misc;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSKeyValueCoding;

import er.extensions.appserver.navigation.ERXNavigationItem;
import er.extensions.appserver.navigation.ERXNavigationMenu;
import er.r2d2w.components.buttons.R2DDefaultButtonContent;

public class R2DNavigationMenu extends ERXNavigationMenu {	
	public R2DNavigationMenu(WOContext context) {
		super(context);
	}

	public ERXNavigationItem navigationItem() {
		return aNavigationItem;
	}

	public void setNavigationItem(ERXNavigationItem navigationItem) {
		aNavigationItem = navigationItem;
		navigationContext().takeValueForKey(navigationItem, "navigationItem");
	}

	public String navigationButtonContentComponentName() {
		String name = (String)navigationContext().valueForKey("navigationButtonContentComponentName");
		if(name == null) {
			name = R2DDefaultButtonContent.class.getSimpleName();
		}
		return name;
	}
	
	public NSKeyValueCoding navContext() {
		return navigationContext();
	}
}