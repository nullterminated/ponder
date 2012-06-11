package er.users.components;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResourceManager;
import com.webobjects.foundation.NSArray;

import er.extensions.components.ERXComponent;
import er.users.model.ERActivateUserToken;

public class ERDefaultUserActivationHtmlEmail extends ERXComponent implements ERUserActivationHtmlEmailInterface {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	private String emailTitle;
	private ERActivateUserToken token;
	private String htmlContentFileName;
	private String htmlContentFrameworkName;

	public ERDefaultUserActivationHtmlEmail(WOContext context) {
        super(context);
    }

	/**
	 * @return the emailTitle
	 */
	public String emailTitle() {
		return emailTitle;
	}

	/**
	 * @param emailTitle the emailTitle to set
	 */
	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}

	/**
	 * @return the token
	 */
	public ERActivateUserToken token() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(ERActivateUserToken token) {
		this.token = token;
	}

	public String htmlContentUrl() {
		WOResourceManager rm = WOApplication.application().resourceManager();
		NSArray<String> languages = new NSArray<String>(localizer().language());
		String url = rm.pathURLForResourceNamed(htmlContentFileName(), htmlContentFrameworkName(), languages).toExternalForm();
		return url;
	}
	
	public String htmlContentFileName() {
		return htmlContentFileName;
	}
	
	public void setHtmlContentFileName(String htmlContentFileName) {
		this.htmlContentFileName = htmlContentFileName;
	}
	
	public String htmlContentFrameworkName() {
		return htmlContentFrameworkName;
	}
	
	public void setHtmlContentFrameworkName(String htmlContentFrameworkName) {
		this.htmlContentFrameworkName = htmlContentFrameworkName;
	}

	public String tokenLinkString() {
		return localizer().localizedStringForKey("ERDefaultUserActivationHtmlEmail.tokenLinkString");
	}

	public String tokenHREF() {
		return token().tokenHrefInContext(context());
	}
}