package er.auth;

import er.directtoweb.pages.ERD2WPage;
import er.directtoweb.pages.ERD2WPage.ValidationDelegate;
import er.extensions.validation.ERXValidationException;

public class LoginValidationDelegate extends ValidationDelegate {

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

}
