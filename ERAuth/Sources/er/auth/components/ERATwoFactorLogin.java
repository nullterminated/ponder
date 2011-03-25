package er.auth.components;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

import er.auth.ERAuthDirectAction;
import er.auth.model.ERTwoFactorAuthenticationRequest;
import er.extensions.components.ERXStatelessComponent;
//TODO make secure
public class ERATwoFactorLogin extends ERXStatelessComponent {
	private String username;
	private String password;
	private WOComponent nextPage;
	private String requestType = ERTwoFactorAuthenticationRequest.ENTITY_NAME;
	private String pageWrapperName;
	private String lcid;

	public ERATwoFactorLogin(WOContext context) {
		super(context);
	}
	
	public void reset() {
		username = null;
		password = null;
		nextPage = null;
		requestType = ERTwoFactorAuthenticationRequest.ENTITY_NAME;
		pageWrapperName = null;
		lcid = null;
	}

	/**
	 * @return the username
	 */
	public String username() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String password() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the nextPage
	 */
	public WOComponent nextPage() {
		return nextPage;
	}

	/**
	 * @param nextPage the nextPage to set
	 */
	public void setNextPage(WOComponent nextPage) {
		this.nextPage = nextPage;
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

	/**
	 * @return the pageWrapperName
	 */
	public String pageWrapperName() {
		if(pageWrapperName == null) {
			pageWrapperName = stringValueForBinding("pageWrapperName");
		}
		return pageWrapperName;
	}

	/**
	 * @param pageWrapperName the pageWrapperName to set
	 */
	public void setPageWrapperName(String pageWrapperName) {
		this.pageWrapperName = pageWrapperName;
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
}