package er.auth.components;

import com.webobjects.appserver.WOContext;

import er.auth.ERAuthDirectAction;
import er.auth.model.ERTwoFactorAuthenticationRequest;
import er.extensions.appserver.ERXWOContext;
import er.extensions.components.ERXStatelessComponent;

public class ERATwoFactorLogin extends ERXStatelessComponent {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	private String requestType = ERTwoFactorAuthenticationRequest.ENTITY_NAME;
	private String lcid;
	private String usernameFieldID;
	private String passwordFieldID;

	public ERATwoFactorLogin(WOContext context) {
		super(context);
	}
	
	public void reset() {
		requestType = ERTwoFactorAuthenticationRequest.ENTITY_NAME;
		lcid = null;
		usernameFieldID = null;
		passwordFieldID = null;
	}

	/**
	 * @return the requestType
	 */
	public String requestType() {
		return requestType;
	}

	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String lastCID() {
		if(lcid == null) {
			lcid = context().request().stringFormValueForKey(ERAuthDirectAction.lastContextIDKey);
		}
		if(lcid == null) {
			lcid = context().request().stringFormValueForKey(ERAuthDirectAction.contextIDKey);
		}
		return lcid;
	}
	
	public void setLastCID(String cid) {
		this.lcid = cid;
	}

	public String passwordFieldName() {
		return ERTwoFactorAuthenticationRequest.PASSWORD_KEY;
	}

	public String usernameFieldName() {
		return ERTwoFactorAuthenticationRequest.USERNAME_KEY;
	}

	/**
	 * @return the usernameFieldID
	 */
	public String usernameFieldID() {
		if(usernameFieldID == null) {
			usernameFieldID = ERXWOContext.safeIdentifierName(context(), false);
		}
		return usernameFieldID;
	}

	/**
	 * @return the passwordFieldID
	 */
	public String passwordFieldID() {
		if(passwordFieldID == null) {
			passwordFieldID = ERXWOContext.safeIdentifierName(context(), false);
		}
		return passwordFieldID;
	}
}