package er.users.components;

import er.users.model.ERActivateUserToken;

public interface ERUserActivationHtmlEmailInterface {
	
	public void setToken(ERActivateUserToken token);

	public void setHtmlContentFileName(String htmlContentFileName);

	public void setHtmlContentFrameworkName(String htmlContentFrameworkName);
	
	public void setEmailTitle(String emailTitle);
}
