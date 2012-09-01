package er.users.components;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResourceManager;
import com.webobjects.foundation.NSArray;

public class ERDefaultResetPasswordHtmlEmail extends ERUserHtmlEmail {
    public ERDefaultResetPasswordHtmlEmail(WOContext context) {
        super(context);
    }

	public String htmlContentUrl() {
		WOResourceManager rm = WOApplication.application().resourceManager();
		NSArray<String> languages = new NSArray<String>(localizer().language());
		String url = rm.pathURLForResourceNamed("ResetPasswordHtmlEmailContent.html", "ERUsers", languages).toExternalForm();
		return url;
	}
}