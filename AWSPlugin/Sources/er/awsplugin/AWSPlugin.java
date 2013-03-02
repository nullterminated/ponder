package er.awsplugin;

import org.apache.log4j.Logger;

import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eocontrol.EOClassDescription;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;

import er.awsplugin.model.SESBounceNotification;
import er.awsplugin.model.SESComplaintNotification;
import er.corebl.ERCoreBL;
import er.corebl.model.ERCMailAddress;
import er.extensions.ERXExtensions;
import er.extensions.ERXFrameworkPrincipal;
import er.extensions.eof.ERXEOAccessUtilities;
import er.extensions.eof.ERXModelGroup;
import er.extensions.foundation.ERXSelectorUtilities;

public class AWSPlugin extends ERXFrameworkPrincipal {

    public static final Logger log = Logger.getLogger(AWSPlugin.class);
    public final static Class<?>[] REQUIRES = new Class[] {ERXExtensions.class, ERCoreBL.class};

    protected static volatile AWSPlugin sharedInstance;

    // Registers the class as the framework principal
    static {
    	log.debug("Static Initializer for AWSPlugin");
    	setUpFrameworkPrincipalClass (AWSPlugin.class);
    }

    public static AWSPlugin sharedInstance() {
        if (sharedInstance == null) {
        	synchronized (AWSPlugin.class) {
        		if(sharedInstance == null) {
        			sharedInstance = sharedInstance(AWSPlugin.class);
        		}
        	}
        }
        return sharedInstance;
    }
    
    @Override
    public void finishInitialization() {
    	/*
    	 * Dynamically create reverse relationships for notifications
    	 * to prevent tying ERCoreBL to AWSPlugin
    	 */
    	NSNotificationCenter.defaultCenter().addObserver(
    			this, 
    			ERXSelectorUtilities.notificationSelector("modelGroupAdded"), 
    			ERXModelGroup.ModelGroupAddedNotification, 
    			null);
    }
    
    public void modelGroupAdded(NSNotification n) {
    	EOModelGroup group = (EOModelGroup) n.object();
    	if(group.modelNamed("ERCoreBL") != null && group.modelNamed("AWSPlugin") != null) {
        	ERXEOAccessUtilities.createRelationship("complaints", ERCMailAddress.ENTITY_NAME, "id", SESComplaintNotification.ENTITY_NAME, "mailAddressID", true, EOClassDescription.DeleteRuleDeny, false, true, false);
        	ERXEOAccessUtilities.createRelationship("bounces", ERCMailAddress.ENTITY_NAME, "id", SESBounceNotification.ENTITY_NAME, "mailAddressID", true, EOClassDescription.DeleteRuleDeny, false, true, false);
    	}
    }
}
