package er.r2d2w.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.directtoweb.components.ERDCustomComponent;
import er.extensions.localization.ERXLocalizer;

public class R2DInfoPanel extends ERDCustomComponent {
	private NSMutableArray<String> infoStrings;
	private String infoString;
	
    public R2DInfoPanel(WOContext context) {
        super(context);
    }

	public NSArray<String> infoStrings() {
		if(infoStrings==null) {
			infoStrings = new NSMutableArray<String>();
			ERXLocalizer loc = ERXLocalizer.currentLocalizer();
			D2WContext c = d2wContext();
			NSArray<String> propKeys = (NSArray<String>)c.valueForKey("displayPropertyKeys");
			for(Object prop: propKeys) {
				String locKey = "PropertyKey." + prop.toString() + ".inputInfo";
				c.setPropertyKey(prop.toString());
				String result = (loc.valueForKey(locKey)==null)?null:loc.localizedTemplateStringForKeyWithObject(locKey, c);
				if(result!=null) {infoStrings.add(result);}
			}
		}
		return infoStrings;
	}
	
	public void reset() {
		infoStrings = null;
		infoString = null;
	}

	/**
	 * @return the infoString
	 */
	public String infoString() {
		return infoString;
	}

	/**
	 * @param infoString the infoString to set
	 */
	public void setInfoString(String infoString) {
		this.infoString = infoString;
	}
}