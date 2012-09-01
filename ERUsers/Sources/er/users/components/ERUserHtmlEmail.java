package er.users.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;

import er.extensions.components.ERXComponent;
import er.users.model.ERUser;

public abstract class ERUserHtmlEmail extends ERXComponent {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;
	
	private ERUser _user;

	public ERUserHtmlEmail(WOContext context) {
		super(context);
	}
	
	/**
	 * Overridden to ensure complete URLs are generated in the response
	 */
	@Override
	public void appendToResponse(WOResponse response, WOContext context) {
		context.generateCompleteURLs();
		super.appendToResponse(response, context);
	}
	
	public ERUser user() {
		return _user;
	}
	
	public void setUser(ERUser user) {
		_user = user;
	}
	
	public String activateUserHref() {
		return user().activateUserHrefInContext(context());
	}
	
	public String resetPasswordHref() {
		return user().resetPasswordHrefInContext(context());
	}
}
