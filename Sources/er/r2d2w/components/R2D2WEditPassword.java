package er.r2d2w.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORequest;
import com.webobjects.foundation.NSValidation;

import er.directtoweb.components.ERDCustomEditComponent;
import er.extensions.localization.ERXLocalizer;
import er.extensions.validation.ERXValidationFactory;

public class R2D2WEditPassword extends ERDCustomEditComponent {
    public R2D2WEditPassword(WOContext context) {
        super(context);
    }
    
    private static final String _COMPONENT_CLASS = "password";
	private String labelID;
	private String confirmLabelID;
	private String confirmPassword;
	private String password;
	private boolean confirmSetToNull = false;
	private boolean setToNull = false;
    
	/**
	 * @return the confirmLabelID
	 */
	public String r2ConfirmLabelID() {
		return confirmLabelID;
	}

	/**
	 * @param confirmLabelID the confirmLabelID to set
	 */
	public void setR2ConfirmLabelID(String confirmLabelID) {
		this.confirmLabelID = confirmLabelID;
	}

	/**
	 * @return the _r2FieldLabelID
	 */
	public String _r2FieldLabelID() {
		return labelID;
	}

	/**
	 * @param labelID the _r2FieldLabelID to set
	 */
	public void set_r2FieldLabelID(String labelID) {
		this.labelID = labelID;
	}
	
	public String componentClasses() {
    	return _COMPONENT_CLASS;
    }
    
	/**
	 * @return the confirmPassword
	 */
	public String confirmPassword() {		
		if(confirmSetToNull) {return null;}
		return (confirmPassword == null)?(String)objectPropertyValue():confirmPassword;
	}

	/**
	 * @param confirmPassword the confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		if(confirmPassword == null || confirmPassword.length() == 0) {confirmSetToNull = true;}
		this.confirmPassword = confirmPassword;
	}

	/**
	 * @return the password
	 */
	public String password() {
		if(setToNull) {return null;}
		return (password == null)?(String)objectPropertyValue():password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		if(password == null || password.length() == 0) {setToNull = true;}
		this.password = password;
	}

    public void reset() {
    	super.reset();
    	confirmLabelID = null;
    	confirmPassword = null;
    	confirmSetToNull = false;
    	labelID = null;
    	password = null;
    	setToNull = false;
    }

    public void fail(String errorCode) {
        if(log.isDebugEnabled()) {
            log.debug("fail:<object:" + object() + "; key:" + key() + ";  password: " + password() + "; code:" + errorCode + ";>");
        }
        validationFailedWithException(ERXValidationFactory.defaultFactory().createException(object(), key(), password(), errorCode), password(), key());
    }

    // Yeah, it looks like (is) a total hack, but it seems to work.
    //TODO figure out how to do this in a cleaner way
    protected void checkPasswords() {
    	if(password() == null || password().length() == 0 || confirmPassword() == null || confirmPassword().length() == 0) {
    		confirmPassword = (String)objectPropertyValue();
    		password = (String)objectPropertyValue();
    		confirmSetToNull = false;
    		setToNull = false;
    		fail("PasswordsFillBothFieldsException");
    	} else if (!password().equals(confirmPassword())) {
    		confirmPassword = (String)objectPropertyValue();
    		password = (String)objectPropertyValue();
    		confirmSetToNull = false;
    		setToNull = false;
    		fail("PasswordsDontMatchException");    		
    	} else {
            try {
    			object().validateTakeValueForKeyPath(password(), key());
    		} catch(NSValidation.ValidationException ex) {
        		confirmPassword = (String)objectPropertyValue();
        		password = (String)objectPropertyValue();
        		confirmSetToNull = false;
        		setToNull = false;
    			validationFailedWithException(ex, password(), key());
    		}
    	}
    }
    
    public void takeValuesFromRequest(WORequest r, WOContext c) {
        super.takeValuesFromRequest(r,c);
        checkPasswords();
	}

	public String confirmToolTip() {
		return ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("R2D2WEditPassword.confirmToolTip", d2wContext());
	}

}