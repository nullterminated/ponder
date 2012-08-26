package er.users.components;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResourceManager;
import com.webobjects.foundation.NSArray;

/**
 * <p>This component exists as an example of how you might want to design your
 * user activation email. It is also the default used by the framework. It
 * can be replaced by sub-classing ERUserHtmlEmail with your own component and
 * designating your component as the replacement with a rule like:</p>
 * 
 * <p><code>100 : *true* => templateNameForUserActivationEmail = "MyActivationComponent" [com.webobjects.directtoweb.Assignment]</code></p>
 *
 * <p>You would also need to include the appropriate localized strings for 
 * message subject and a plain text message alternative in your app.</p>
 */
public class ERDefaultUserActivationHtmlEmail extends ERUserHtmlEmail {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 2L;

	public ERDefaultUserActivationHtmlEmail(WOContext context) {
        super(context);
    }

	public String htmlContentUrl() {
		WOResourceManager rm = WOApplication.application().resourceManager();
		NSArray<String> languages = new NSArray<String>(localizer().language());
		String url = rm.pathURLForResourceNamed("UserActivationHtmlEmailContent.html", "ERUsers", languages).toExternalForm();
		return url;
	}

}