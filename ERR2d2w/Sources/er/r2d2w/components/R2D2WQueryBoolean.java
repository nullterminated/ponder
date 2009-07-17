package er.r2d2w.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import er.directtoweb.components.bool.ERD2WCustomQueryBoolean;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.localization.ERXLocalizer;

public class R2D2WQueryBoolean extends ERD2WCustomQueryBoolean {
	
    private String labelID;

	public R2D2WQueryBoolean(WOContext context) {
        super(context);
    }
    
	public void reset() {
		labelID = null;
		super.reset();
	}
	
    public String displayString() {
    	NSArray<String> choicesNames = choicesNames();
        String result;
        int choicesIndex = queryNumbers.indexOf(item);
        choicesIndex = choicesIndex == 0 ? 2 : choicesIndex - 1;
        if(choicesNames == null || choicesIndex >= choicesNames.count()) {
            result = super.displayString();
        } else {
        	result = (String)choicesNames.objectAtIndex(choicesIndex);
        }
        return ERXLocalizer.currentLocalizer().localizedStringForKeyWithDefault(result);
    }

	/**
	 * @return the labelID
	 */
	public String labelID() {
		if(labelID == null) {
			labelID = ERXStringUtilities.safeIdentifierName(context().elementID(),"id",'_');
		}
		return labelID;
	}

}