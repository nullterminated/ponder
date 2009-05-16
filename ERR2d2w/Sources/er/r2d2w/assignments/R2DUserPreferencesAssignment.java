package er.r2d2w.assignments;

import org.apache.log4j.Logger;

import com.webobjects.directtoweb.D2WContext;
import com.webobjects.eocontrol.EOKeyValueArchiver;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;

import er.directtoweb.assignments.ERDAssignment;

public class R2DUserPreferencesAssignment extends ERDAssignment {

	/** Logging support */
    public static final Logger log = Logger.getLogger(R2DUserPreferencesAssignment.class);
	
	private static final NSArray<String> dependentKeys = 
		new NSArray<String>(new String[] { "session", "session.objectStore.user"});
	
	public R2DUserPreferencesAssignment(EOKeyValueUnarchiver u) {
		super(u);
	}

	public R2DUserPreferencesAssignment(String key, Object value) {
		super(key, value);
	}

	public void encodeWithKeyValueArchiver(EOKeyValueArchiver archiver) {
		super.encodeWithKeyValueArchiver(archiver);
	}

	public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver unarchiver) {
		return new R2DUserPreferencesAssignment(unarchiver);
	}

	public NSArray<String> dependentKeys(String keyPath) {
		return dependentKeys;
	}

	public Object userPreferences(D2WContext c) {
		Object userPreferences = c.valueForKeyPath("session.objectStore.userPreferences");
		if(userPreferences == null) {
			userPreferences = new NSMutableDictionary<String, Object>();
			c.takeValueForKeyPath(userPreferences, "session.objectStore.userPreferences");
		}
		return userPreferences;
	}
		
}
