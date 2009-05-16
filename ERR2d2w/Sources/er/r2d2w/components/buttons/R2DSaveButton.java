package er.r2d2w.components.buttons;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WPage;
import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSValidation;

import er.directtoweb.components.buttons.ERDActionButton;
import er.extensions.eof.ERXEOAccessUtilities;
import er.extensions.foundation.ERXValueUtilities;
import er.extensions.localization.ERXLocalizer;

public class R2DSaveButton extends ERDActionButton {
	
    public static final Logger validationLog = Logger.getLogger("er.directtoweb.validation.R2DSaveButton");

    public R2DSaveButton(WOContext context) {
        super(context);
    }

	public WOActionResults saveAction() {
        WOComponent returnComponent = context().page();
        D2WPage parentPage = parentD2WPage();
        // catch the case where the user hits cancel and then the back button
        if (object()!=null && object().editingContext()==null) {
            parentPage.takeValueForKey(ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("ERD2WInspect.alreadyAborted", d2wContext()), "errorMessage");
            clearValidationFailed();
        } else {
        	NSDictionary<String,String> errorMessages = ERXValueUtilities.dictionaryValueWithDefault(parentPage.valueForKey("errorMessages"), NSDictionary.EmptyDictionary);
            if (errorMessages.count()==0) {
                try {
                	// CHECKME objectsaver interface on edit page is not going to work
                	// like this. Should it be supported? Needs setObjectWasSaved method
                	// added to ERD2WInspectPage if so
                    //_objectWasSaved=true;
                    returnComponent = tryToSaveChanges(true) ? nextPageInPage(parentPage) : returnComponent;
                } finally {
                    //_objectWasSaved=false;
                }
            } else {
                // if we don't do this, we end up with the error message in two places
                // in errorMessages and errorMessage (super class)
            	parentPage.takeValueForKey(null, "errorMessage");
            }
        }

        // refresh newly created object to get values for derived attributes.
        NSDictionary<String,String> errorMessages = ERXValueUtilities.dictionaryValueWithDefault(parentPage.valueForKey("errorMessages"), NSDictionary.EmptyDictionary);
    	if(errorMessages.count() == 0) {
    		EOEditingContext ec = object().editingContext();
    		ec.refreshObject(object());
    	}

        return returnComponent;
	}
	
    public boolean shouldSaveChanges() { return ERXValueUtilities.booleanValue(d2wContext().valueForKey("shouldSaveChanges")); }
    public boolean shouldValidateBeforeSave() { return ERXValueUtilities.booleanValue(d2wContext().valueForKey("shouldValidateBeforeSave")); }
    public boolean shouldRecoverFromOptimisticLockingFailure() { return ERXValueUtilities.booleanValueWithDefault(d2wContext().valueForKey("shouldRecoverFromOptimisticLockingFailure"), false); }
    public boolean shouldRevertUponSaveFailure() { return ERXValueUtilities.booleanValueWithDefault(d2wContext().valueForKey("shouldRevertUponSaveFailure"), false); }

	
    public boolean tryToSaveChanges(boolean validateObject) {
    	validationLog.debug("tryToSaveChanges calling validateForSave");
    	boolean saved = false;
        D2WPage parentPage = parentD2WPage();
        EOEnterpriseObject eo = object();

    	if(eo!=null) {
    		EOEditingContext ec = eo.editingContext();
    		boolean shouldRevert = false;
    		try {
    			if (validateObject && shouldValidateBeforeSave()) {
    				if (ec.insertedObjects().containsObject(eo))
    					eo.validateForInsert();
    				else
    					eo.validateForUpdate();
    			}
            if (shouldSaveChanges() && ec.hasChanges()) {
                try {
                    ec.saveChanges();
                } catch (RuntimeException e) {
                    if( shouldRevertUponSaveFailure() ) {
                        shouldRevert = true;
                    }
                    throw e;
                }
    		}
    			saved = true;
    		} catch (NSValidation.ValidationException ex) {
    			parentPage.takeValueForKey(ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("CouldNotSave", ex), "errorMessage");
    			validationFailedWithException(ex, ex.object(), "saveChangesExceptionKey");
    		} catch(EOGeneralAdaptorException ex) {
    			if(ERXEOAccessUtilities.isOptimisticLockingFailure(ex) && shouldRecoverFromOptimisticLockingFailure()) {
    				EOEnterpriseObject refetchedEO = ERXEOAccessUtilities.refetchFailedObject(ec, ex);
    				parentPage.takeValueForKey(ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("CouldNotSavePleaseReapply", d2wContext()), "errorMessage");
    				validationFailedWithException(ex, refetchedEO, "CouldNotSavePleaseReapply");
    			} else {
    				throw ex;
    			}
			} finally {
				if( shouldRevert ) {
					ec.lock();
					try {
						ec.revert();
					} finally {
						ec.unlock();
					}
				}
			}
    	} else {
    		saved = true;
    	}
    	return saved;
    }

}