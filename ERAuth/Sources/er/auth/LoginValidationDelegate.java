package er.auth;

import er.directtoweb.pages.ERD2WPage;
import er.directtoweb.pages.ERD2WPage.ValidationDelegate;
import er.extensions.validation.ERXValidationException;

public class LoginValidationDelegate extends ValidationDelegate {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	public LoginValidationDelegate(ERD2WPage page) {
		super(page);
	}

	@Override
	public boolean hasValidationExceptionForPropertyKey() {
		return false;
	}

	@Override
	public void validationFailedWithException(Throwable e, Object value, String keyPath) {
		if (e instanceof ERXValidationException) {
			ERXValidationException ve = (ERXValidationException)e;
			_page.d2wContext().takeValueForKey(ve.getMessage(), "extraErrorMessage");
		}
	}

	@Override
	public void clearValidationFailed() {
		// Does nothing
	}

	@Override
	public String errorMessageForPropertyKey() {
		// Does nothing
		return null;
	}

}
