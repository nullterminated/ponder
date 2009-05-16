package er.r2d2w.delegates;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOComponent;
import com.webobjects.directtoweb.D2W;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.D2WModel;
import com.webobjects.directtoweb.ErrorPageInterface;
import com.webobjects.directtoweb.NextPageDelegate;
import com.webobjects.eoaccess.EODatabaseContext;
import com.webobjects.eoaccess.EODatabaseOperation;
import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eoaccess.EOObjectNotAvailableException;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSValidation;

import er.directtoweb.interfaces.ERDErrorPageInterface;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXEOAccessUtilities;
import er.extensions.eof.ERXEOControlUtilities;
import er.extensions.localization.ERXLocalizer;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;

public class R2DDeletionDelegate implements NextPageDelegate {

    /** logging support */
    public final static Logger log = Logger.getLogger(R2DDeletionDelegate.class.getName());
    
    private EOEnterpriseObject eo;
    private WOComponent nextPage;
    
    public R2DDeletionDelegate(EOEnterpriseObject eo, WOComponent nextPage) {
    	this.eo = eo;
    	this.nextPage = nextPage;
    }
    
	public WOComponent nextPage(WOComponent sender) {
		if(eo!=null) {
			NSValidation.ValidationException exception = null;
			
			try {
				EOEditingContext ec = ERXEC.newEditingContext();
				ec.setSharedEditingContext(null);
				EOEnterpriseObject leo = ERXEOControlUtilities.localInstanceOfObject(ec, eo);
				ec.deleteObject(leo);
				ec.saveChanges();
			} catch (NSValidation.ValidationException e) {
				exception = e;
			} catch (EOObjectNotAvailableException e) {
				exception = ERXValidationFactory.defaultFactory().createCustomException(eo, EOObjectNotAvailableException.class.getSimpleName());
			} catch (EOGeneralAdaptorException e) {
				NSDictionary<Object,Object> userInfo = e.userInfo();
            	if(userInfo != null) {
            		EODatabaseOperation op = (EODatabaseOperation)userInfo.objectForKey(EODatabaseContext.FailedDatabaseOperationKey);
            		if(op.databaseOperator() == EODatabaseOperation.DatabaseDeleteOperator) {
            			exception = ERXValidationFactory.defaultFactory().createCustomException(eo, EOObjectNotAvailableException.class.getSimpleName());
            		}
            	}
            	if(exception == null) {
            		exception = ERXValidationFactory.defaultFactory().createCustomException(eo, "Database error: " + e.getMessage());
            	}
			}
			
			if(exception!=null) {
                if (exception instanceof ERXValidationException) {
                    ERXValidationException ex = (ERXValidationException) exception;
                    D2WContext c = (D2WContext) sender.valueForKey("d2wContext");
                    Object o = ex.object();

                    if (o instanceof EOEnterpriseObject) {
                        EOEnterpriseObject exo = (EOEnterpriseObject) o;
                        c.takeValueForKey(ERXEOAccessUtilities.entityForEo(exo), D2WModel.EntityKey);
                        c.takeValueForKey(ex.propertyKey(), D2WModel.PropertyKeyKey);
                    }
                    ((ERXValidationException) exception).setContext(c);
                }
                String errorMessage = ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("CouldNotSave", exception);
                ErrorPageInterface epf = D2W.factory().errorPage(sender.session());
                if (epf instanceof ERDErrorPageInterface) {
                    ((ERDErrorPageInterface) epf).setException(exception);
                }
                epf.setMessage(errorMessage);
                epf.setNextPage(nextPage);
                return (WOComponent) epf;
			}
		}
		return nextPage;
	}

}
