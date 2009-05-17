package er.r2d2w.components.buttons;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.ConfirmPageInterface;
import com.webobjects.directtoweb.D2W;
import com.webobjects.directtoweb.D2WPage;
import com.webobjects.eocontrol.EOEnterpriseObject;

import er.directtoweb.components.buttons.ERDDeleteButton;
import er.directtoweb.delegates.ERDPageDelegate;
import er.extensions.eof.ERXGuardedObjectInterface;
import er.extensions.foundation.ERXValueUtilities;
import er.extensions.localization.ERXLocalizer;
import er.r2d2w.delegates.R2DDeletionDelegate;

public class R2DDeleteButton extends ERDDeleteButton {

	public R2DDeleteButton(WOContext context) {
        super(context);
    }
	
    public WOComponent deleteObjectAction() {
        ConfirmPageInterface nextPage = (ConfirmPageInterface)D2W.factory().pageForConfigurationNamed((String)valueForBinding("confirmDeleteConfigurationName"), session());

        //TODO assuming patch is accepted, delete this override and delegate class
        nextPage.setConfirmDelegate(new R2DDeletionDelegate(object(), context().page()));
        //nextPage.setConfirmDelegate(new ERDDeletionDelegate(object(), dataSource(), context().page()));

        nextPage.setCancelDelegate(new ERDPageDelegate(context().page()));
        D2WPage d2wPage = ((D2WPage)nextPage);
        
        String message = ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("ERDTrashcan.confirmDeletionMessage", d2wContext()); 
        nextPage.setMessage(message);
        d2wPage.setObject(object());
        return (WOComponent) nextPage;
    }
    
    public boolean canDelete() {
    	EOEnterpriseObject o = object();
        return (o != null) && (o instanceof ERXGuardedObjectInterface ? ((ERXGuardedObjectInterface)o).canDelete() : true) && ERXValueUtilities.booleanValueWithDefault(d2wContext().valueForKey("isEntityDeletable"), false);
    }

}