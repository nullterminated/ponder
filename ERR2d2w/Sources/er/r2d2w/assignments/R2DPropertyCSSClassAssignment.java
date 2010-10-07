package er.r2d2w.assignments;

import com.webobjects.directtoweb.D2WContext;
import com.webobjects.eocontrol.EOKeyValueArchiver;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.directtoweb.assignments.delayed.ERDDelayedAssignment;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.foundation.ERXValueUtilities;

public class R2DPropertyCSSClassAssignment extends ERDDelayedAssignment {
	public static final String ERROR_CLASS = "error";
	public static final String MANDATORY_CLASS = "mandatory";

	public R2DPropertyCSSClassAssignment(EOKeyValueUnarchiver u) {
		super(u);
	}

	public R2DPropertyCSSClassAssignment(String key, Object value) {
		super(key, value);
	}

	public void encodeWithKeyValueArchiver(EOKeyValueArchiver archiver) {
		super.encodeWithKeyValueArchiver(archiver);
	}

	public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver unarchiver) {
		return new R2DPropertyCSSClassAssignment(unarchiver);
	}

	public Object fireNow(D2WContext c) {
		NSMutableArray<String> classes = new NSMutableArray<String>();
		String propertyKey = c.propertyKey();
		NSArray<String> exceptions = (NSArray<String>)c.valueForKey("keyPathsWithValidationExceptions");
		if(exceptions != null && exceptions.containsObject(propertyKey)) {
			classes.add(ERROR_CLASS);
		}
		if(ERXValueUtilities.booleanValue(c.valueForKey("displayRequiredMarker"))){
			classes.add(MANDATORY_CLASS);
		}
		classes.add(ERXStringUtilities.safeIdentifierName(c.propertyKey()));
		return classes.componentsJoinedByString(" ");
	}
	
}
