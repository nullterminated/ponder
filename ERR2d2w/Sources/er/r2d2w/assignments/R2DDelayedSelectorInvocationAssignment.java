package er.r2d2w.assignments;

import com.webobjects.directtoweb.D2WContext;
import com.webobjects.eocontrol.EOKeyValueArchiver;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSSelector;

import er.directtoweb.assignments.ERDComputingAssignmentInterface;
import er.directtoweb.assignments.delayed.ERDDelayedAssignment;
import er.extensions.foundation.ERXSelectorUtilities;

public class R2DDelayedSelectorInvocationAssignment 
		extends ERDDelayedAssignment implements ERDComputingAssignmentInterface {

	public R2DDelayedSelectorInvocationAssignment(EOKeyValueUnarchiver u) { super(u); }
	public R2DDelayedSelectorInvocationAssignment(String key, Object value) { super(key, value); }
	public void encodeWithKeyValueArchiver(EOKeyValueArchiver archiver) { super.encodeWithKeyValueArchiver(archiver); }
	public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver unarchiver) { return new R2DDelayedSelectorInvocationAssignment(unarchiver); }
	
	public NSArray<String> dependentKeys(String keyPath) {
		return null;
	}
	
	@Override
	public Object fireNow(D2WContext c) {
		Object result = null;
		NSArray<Object> value = (NSArray<Object>)value();
		int valueCount = value.count();
		
		if(valueCount != 2 && valueCount != 4) {
			throw new IllegalArgumentException("Wrong number of parameters.");
		}
		
		Object target = c.valueForKeyPath((String)value.objectAtIndex(0));
		String selectorName = (String)value.objectAtIndex(1);
		if(valueCount == 2) {
			NSSelector<?> sel = new NSSelector(selectorName);
			result = ERXSelectorUtilities.invoke(sel, target);
			return result;
		}
		
		NSArray<String> classNames = (NSArray<String>)value.objectAtIndex(2);
		NSArray<String> contextKeys = (NSArray<String>)value.objectAtIndex(3);
		int classCount = classNames.count();
		int paramCount = contextKeys.count();
		if(classCount != paramCount) {
			throw new IllegalArgumentException("Class and parameter array must be equal length");
		}
		
		Class<?>[] parameterTypes = new Class[classCount];
		try {
			for(int i = 0; i < classCount; i++) {
				Class<?> paramType = Class.forName(classNames.objectAtIndex(i));
				parameterTypes[i] = paramType;
			}
		} catch (ClassNotFoundException cnfe) {
			throw NSForwardException._runtimeExceptionForThrowable(cnfe);
		}

		Object[] parameters = new Object[paramCount];
		for(int j = 0; j < paramCount; j++) {
			String key = contextKeys.objectAtIndex(j);
			Object o = c.valueForKeyPath(key);
			if(!parameterTypes[j].isInstance(o)) {
				throw new IllegalArgumentException("Object for context key (" + key + ") is not an instance of " + parameterTypes[j].toString() + ". Object: " + o.toString());
			}
			parameters[j] = o;
		}
		
		NSSelector<?> sel = new NSSelector<Object>(selectorName, parameterTypes);
		result = ERXSelectorUtilities.invoke(sel, target, parameters);
		
		return result;
	}

}
