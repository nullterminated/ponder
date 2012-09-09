package er.auth;

import com.webobjects.appserver.WOComponent;
import com.webobjects.directtoweb.D2WPage;
import com.webobjects.directtoweb.ERD2WUtilities;
import com.webobjects.directtoweb.NextPageDelegate;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSValidation.ValidationException;

import er.auth.components.ERAAuthorizationPage;
import er.directtoweb.delegates.ERDBranchDelegate;
import er.directtoweb.delegates.ERDBranchInterface;

public class AuthorizeEntityController extends ERDBranchDelegate {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	public WOComponent _save(WOComponent sender) {
		ERAAuthorizationPage page = ERD2WUtilities.enclosingComponentOfClass(sender, ERAAuthorizationPage.class);
		EOEditingContext ec = page.editingContext();
		try {
			ec.saveChanges();
			return _nextPageFromDelegate(page);
		} catch (ValidationException ex) {
			page.validationFailedWithException(ex, ex.object(), ex.key());
			return sender.context().page();
		}
	}
	
	public WOComponent _cancel(WOComponent sender) {
		ERAAuthorizationPage page = ERD2WUtilities.enclosingComponentOfClass(sender, ERAAuthorizationPage.class);
		EOEditingContext ec = page.editingContext();
		ec.dispose();
		return _nextPageFromDelegate(page);
	}

	/**
	 * Returns the next page from the D2WPage
	 * 
	 * @param page
	 *            the D2WPage
	 * @return the next page
	 */
	protected WOComponent _nextPageFromDelegate(D2WPage page) {
		WOComponent nextPage = null;
		NextPageDelegate delegate = page.nextPageDelegate();
		if (delegate != null) {
			if (!((delegate instanceof ERDBranchDelegate) && (((ERDBranchInterface) page).branchName() == null))) {
				/*
				 * we assume here, because nextPage() in ERDBranchDelegate is
				 * final, we can't do something reasonable when none of the
				 * branch buttons was selected. This allows us to throw a branch
				 * delegate at any page, even when no branch was taken
				 */
				nextPage = delegate.nextPage(page);
			}
		}
		if (nextPage == null) {
			nextPage = page.nextPage();
		}
		return nextPage;
	}
}
