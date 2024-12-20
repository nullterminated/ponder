// Generated by the WOLips Template engine Plug-in at Jan 10, 2010 12:00:00 PM
package er.addressbookexample.components;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.D2WPage;
import com.webobjects.directtoweb.ERD2WContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;

import er.extensions.appserver.navigation.ERXNavigationManager;
import er.extensions.components.ERXStatelessComponent;
import er.extensions.localization.ERXLocalizer;
import er.r2d2w.ERR2d2wUtils;

public class PageWrapper extends ERXStatelessComponent {
	private static final String acceptsXHTMLKey = "acceptsXHTML";
	private static final NSArray<String> availableTimeZones = new NSArray<>(new String[] { "US/Hawaii",
			"US/Alaska", "US/Pacific", "US/Mountain", "US/Central", "US/Eastern", "GMT", "Asia/Tokyo" });

	public PageWrapper(final WOContext aContext) {
		super(aContext);
	}

	@Override
	public void appendToResponse(final WOResponse response, final WOContext context) {
		super.appendToResponse(response, context);
		if (ERR2d2wUtils.acceptsXHTML(context().request())) {
			ERR2d2wUtils.setXHTMLContentType(response);
		}
	}

	public D2WContext d2wContext() {
		if (context().page() instanceof D2WPage) {
			final D2WPage d2wPage = (D2WPage) context().page();
			return d2wPage.d2wContext();
		}
		return null;
	}

	public NSKeyValueCoding navigationContext() {
		NSKeyValueCoding _navigationContext = null;

		if (context().page() instanceof D2WPage) {
			_navigationContext = d2wContext();
		}

		if (_navigationContext == null) {
			_navigationContext = new ERD2WContext(session());
		}
		ERXNavigationManager.manager().navigationStateForSession(session());
		return _navigationContext;
	}

	public boolean hasMultipleLanguages() {
		return ERXLocalizer.availableLanguages().count() > 1;
	}

	public WOActionResults submitAction() {
		return context().page();
	}

	/**
	 * @return the availableTimeZones
	 */
	public NSArray<String> availableTimeZones() {
		return availableTimeZones;
	}
}
