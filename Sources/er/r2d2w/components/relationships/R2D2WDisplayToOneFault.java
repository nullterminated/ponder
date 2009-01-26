package er.r2d2w.components.relationships;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2W;
import com.webobjects.directtoweb.D2WDisplayToOneFault;
import com.webobjects.directtoweb.InspectPageInterface;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableDictionary;

import er.extensions.localization.ERXLocalizer;
import er.r2d2w.R2D2WDirectAction;

public class R2D2WDisplayToOneFault extends D2WDisplayToOneFault {
    public R2D2WDisplayToOneFault(WOContext context) {
        super(context);
    }

    public String helpString() {
    	return ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("R2D2W.displayToOneFault.helpString", d2wContext());
    }
    
    public WOComponent toOneAction() {
    	EOEntity destinationEntity = (EOEntity)d2wContext().valueForKey("destinationEntity");
    	InspectPageInterface ipi = D2W.factory().inspectPageForEntityNamed(destinationEntity.name(), session());
    	EOEnterpriseObject eo = (EOEnterpriseObject)object().valueForKeyPath(propertyKey());
        ipi.setObject(eo);
        ipi.setNextPage(context().page());
        return (WOComponent)ipi;
    }
    
    public String directActionName() {
    	EOEntity entity = (EOEntity)d2wContext().valueForKey("destinationEntity");
    	return "Inspect" + entity.name();
    }
    
	public NSDictionary<String, Object> queryDictionary() {
		NSDictionary d = EOUtilities.destinationKeyForSourceObject(object().editingContext(), object(), propertyKey());
		if(d != null && d.count() > 0) {
			Object o = d.allValues().objectAtIndex(0);
			if(o.equals(NSKeyValueCoding.NullValue)) {
				NSMutableDictionary<String, Object> qd = new NSMutableDictionary<String, Object>();
				if(ERXLocalizer.isLocalizationEnabled()) {
					qd.put(R2D2WDirectAction.LANGUAGE_KEY, ERXLocalizer.currentLocalizer().languageCode());
				}
				return qd;
			} else {
				return R2D2WDirectAction.inspectQueryDictionaryForKey(o.toString());
			}
		} else {
			//CHECKME set up prefetch defaults?
			return R2D2WDirectAction.inspectQueryDictionary((EOEnterpriseObject)object().valueForKeyPath(propertyKey()));			
		}
    }
}