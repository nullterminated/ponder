package er.auth.components;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORequest;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSSelector;

import er.auth.model.ERTwoFactorAuthenticationRequest;
import er.auth.processing.ERTwoFactorAuthenticationConfig;
import er.directtoweb.components.ERDCustomComponent;
import er.extensions.ERXExtensions;
import er.extensions.appserver.ERXWOContext;
import er.extensions.foundation.ERXSelectorUtilities;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;

public class ERAEditPassword extends ERDCustomComponent {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 2L;

	private static final Logger log = Logger.getLogger(ERAEditPassword.class);

	private Object storedPassword;
	private String oldPassword;
	private String newPassword;
	private String verifyPassword;
	private String oldID;
	private String newID;
	private String verifyID;
	private EOEnterpriseObject object;

	public ERAEditPassword(WOContext context) {
		super(context);
	}

	public void takeValuesFromRequest(WORequest request, WOContext context) {
		super.takeValuesFromRequest(request, context);
		ERTwoFactorAuthenticationConfig config = config();
		boolean hasOldPassword = oldPassword() != null;
		boolean hasNewPassword = !(newPassword() == null && verifyPassword() == null);
		
		//If all fields are empty, return
		if(!hasOldPassword && !hasNewPassword) { return; }
		
		try {
			//If the stored password is not null and password field does not match it, throw
			boolean hasPassword = hasPassword();
			if (hasPassword && (!hasOldPassword || !config.verifyPassword(object(), oldPassword()))) {
				ERXValidationException e = ERXValidationFactory.defaultFactory().createCustomException(object(),
						"InvalidPasswordException");
				validationFailedWithException(e, e.value(), e.key());
				return;
			}
			boolean match = ERXExtensions.safeEquals(newPassword(), verifyPassword());
			if (!match) {
				ERXValidationException e = ERXValidationFactory.defaultFactory().createCustomException(object(),
						"MismatchedPasswordException");
				validationFailedWithException(e, e.value(), e.key());
				return;
			} else {
				try {
					object().validateTakeValueForKeyPath(newPassword(), d2wContext().propertyKey());
				} catch (ValidationException e) {
					validationFailedWithException(e, e.object(), e.key());
				}
			}
		} finally {
			//Clear all the password fields before returning.
			clearPasswordFields();
		}
	}

	public ERTwoFactorAuthenticationConfig config() {
		ERTwoFactorAuthenticationConfig config = ERTwoFactorAuthenticationRequest.clazz.authenticationConfig();
		return config;
	}

	/**
	 * Retrieves the last stored password value based on the current two factor
	 * authentication configuration. If a null value is found, then
	 * NSKeyValueCoding.NullValue is returned.
	 * 
	 * @return the stored password value or NullValue
	 */
	public Object storedPassword() {
		if (storedPassword == null) {
			ERTwoFactorAuthenticationConfig config = config();
			String entityName = d2wContext().entity().name();
			if (!entityName.equals(config.userEntityName())) {
				log.error("Cannot edit password for entity named: " + entityName);
				throw new IllegalStateException("Configured to edit password for entity named: "
						+ config.userEntityName());
			}
			String keyPath = config.storedPasswordKeyPath();
			storedPassword = object().valueForKeyPath(keyPath);
			if (storedPassword == null) {
				storedPassword = NSKeyValueCoding.NullValue;
			}
			NSSelector<?> sel = ERXSelectorUtilities.notificationSelector("clearStoredPassword");
			NSNotificationCenter.defaultCenter().addObserver(this, sel,
					EOEditingContext.EditingContextDidSaveChangesNotification, object().editingContext());
		}
		return storedPassword;
	}

	/**
	 * Notification method to clear the stored password when the edited object
	 * successfully saves changes.
	 * 
	 * @param n
	 *            the notification
	 */
	public void clearStoredPassword(NSNotification n) {
		NSNotificationCenter.defaultCenter().removeObserver(this);
		storedPassword = null;
	}
	
	/**
	 * Clears all the password text fields
	 */
	public void clearPasswordFields() {
		oldPassword = null;
		newPassword = null;
		verifyPassword = null;
	}

	public boolean hasPassword() {
		return !NSKeyValueCoding.NullValue.equals(storedPassword());
	}

	/**
	 * @return the oldPassword
	 */
	public String oldPassword() {
		return oldPassword;
	}

	/**
	 * @param oldPassword
	 *            the oldPassword to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * @return the newPassword
	 */
	public String newPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword
	 *            the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @return the verifyPassword
	 */
	public String verifyPassword() {
		return verifyPassword;
	}

	/**
	 * @param verifyPassword
	 *            the verifyPassword to set
	 */
	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}

	public EOEnterpriseObject object() {
		return object;
	}

	public void setObject(EOEnterpriseObject object) {
		this.object = object;
	}

	/**
	 * @return the oldID
	 */
	public String oldID() {
		if (oldID == null) {
			oldID = ERXWOContext.safeIdentifierName(context(), true);
		}
		return oldID;
	}

	/**
	 * @return the newID
	 */
	public String newID() {
		if (newID == null) {
			newID = ERXWOContext.safeIdentifierName(context(), true);
		}
		return newID;
	}

	/**
	 * @return the verifyID
	 */
	public String verifyID() {
		if (verifyID == null) {
			verifyID = ERXWOContext.safeIdentifierName(context(), true);
		}
		return verifyID;
	}

	public String legendKey() {
		return hasPassword()?"ERAEditPassword.updateLegend":"ERAEditPassword.createLegend";
	}
}