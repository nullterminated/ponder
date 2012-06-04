package er.r2d2w.delegates;

import org.apache.log4j.Logger;

import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

import er.directtoweb.pages.ERD2WPage;
import er.directtoweb.pages.ERD2WPage.ValidationDelegate;

public class EditListValidationDelegate extends ValidationDelegate {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(EditListValidationDelegate.class);

	private NSMutableDictionary<Object, NSMutableArray<String>> _keyPathsWithValidationExceptions = new NSMutableDictionary<Object, NSMutableArray<String>>();

	public EditListValidationDelegate(ERD2WPage page) {
		super(page);
	}

	@Override
	public boolean hasValidationExceptionForPropertyKey() {
		Object obj = _page.object();
		NSMutableArray<String> keyPaths = _keyPathsWithValidationExceptions.objectForKey(obj);
		return keyPaths != null && keyPaths.count() > 0;
	}

	@Override
	public void validationFailedWithException(Throwable e, Object value, String keyPath) {
		// TODO Auto-generated method stub
        if (log.isDebugEnabled()) {
            log.debug("Validation failed with exception: " + e + " value: " + value + " keyPath: " + keyPath);
        }
	}
	
	@Override
	public void clearValidationFailed() {
		_keyPathsWithValidationExceptions.removeAllObjects();
	}

}
