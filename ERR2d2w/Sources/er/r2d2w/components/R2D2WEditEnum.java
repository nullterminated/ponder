package er.r2d2w.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSMutableArray;

import er.directtoweb.components.ERD2WStatelessComponent;
import er.extensions.foundation.ERXStringUtilities;

public class R2D2WEditEnum<T extends Enum<T>> extends ERD2WStatelessComponent {
    public R2D2WEditEnum(WOContext context) {
        super(context);
    }
	private String labelID;
	private Class<T> clazz;

	public void reset() {
		labelID = null;
		clazz = null;
		super.reset();
	}
	
	/**
	 * @return the labelID
	 */
	public String labelID() {
		if(labelID == null) {
			labelID = ERXStringUtilities.safeIdentifierName(context().elementID(), "id", '_');
		}
		return labelID;
	}
	
	/**
	 * Finds the enum class
	 * @return The enum class
	 */
	@SuppressWarnings("unchecked")
	protected Class<T> enumClass() {
		if(clazz == null) {
			EOEntity entity = EOUtilities.entityForObject(object().editingContext(), object());
			EOAttribute attribute = entity.attributeNamed(d2wContext().propertyKey());
			String className = attribute.className();
			try {
				clazz = (Class<T>) Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw NSForwardException._runtimeExceptionForThrowable(e);
			}
		}
		return clazz;
	}

	@SuppressWarnings("unchecked")
	public NSArray<T> restrictedChoiceList() {
		//First check for a restricted choice key.
		String restrictedChoiceKey=(String)d2wContext().valueForKey("restrictedChoiceKey");
        if( restrictedChoiceKey!=null &&  restrictedChoiceKey.length()>0 ) {
        	//CHECKME why isn't this d2wContext().valueForKeyPath() in the ERX variety?
            return (NSArray<T>) d2wContext().valueForKeyPath(restrictedChoiceKey);
        }
        
        Class<T> klass = enumClass();
		
        //If no restricted choice key is provided, check for possibleChoices
        NSArray<String> possibleChoices = (NSArray<String>)d2wContext().valueForKey("possibleChoices");
        if(possibleChoices != null) {
        	NSMutableArray<T> result = new NSMutableArray<T>();
        	for(String choice : possibleChoices) {
        		result.add(Enum.valueOf(klass, choice));
        	}
        	return result;
        }
        
		//If possibleChoices does not exist, return all enum values as choices
		return new NSArray<T>(klass.getEnumConstants());
	}

}