package er.auth;

import java.lang.reflect.InvocationTargetException;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation._NSUtilities;

import er.extensions.eof.EOEnterpriseObjectClazz;
import er.extensions.eof.ERXEOAccessUtilities;
import er.extensions.foundation.ERXMultiKey;

public class ERASelector<T,U> {
	protected final String methodName;
	protected final Class<U> target;
	protected final Class<?>[] args;
	protected final NSSelector<T> defaultSelector;
	protected final NSMutableDictionary<Class<?>, NSMutableDictionary<ERXMultiKey, NSSelector<T>>> 
		selectors = new NSMutableDictionary<Class<?>, NSMutableDictionary<ERXMultiKey,NSSelector<T>>>();
	
	public ERASelector(String methodName, Class<U> target, Class<?> ... args) {
		this.methodName = methodName;
		this.target = target;
		this.args = args;
		this.defaultSelector = new NSSelector<T>(methodName, args);
	}
	
	public T invoke(U targetInstance, Object ... parameters) {
		if(args.length != parameters.length) {
			throw new IllegalArgumentException("Invalid number of arguments");
		}
		
		verifyClass(target, targetInstance);

		if(EOEnterpriseObjectClazz.class.equals(args[0])) {
			Object obj = parameters[0];
			if(obj instanceof String) {
				parameters[0] = EOEnterpriseObjectClazz.clazzForEntityNamed((String)obj);
			}
			if(obj instanceof EOEntity) {
				EOEntity e = (EOEntity)obj;
				parameters[0] = EOEnterpriseObjectClazz.clazzForEntityNamed(e.name());
			}
		}
		
		for(int i = 0; i < args.length; i++) {
			verifyClass(args[i], parameters[i]);			
		}
		
		NSSelector<T> sel = getSelectorForTarget(targetInstance, parameters);
		
		try {
			return sel.invoke(targetInstance, parameters);
		} catch (IllegalAccessException e) {
			throw NSForwardException._runtimeExceptionForThrowable(e);
		} catch (InvocationTargetException e) {
			throw NSForwardException._runtimeExceptionForThrowable(e);
		} catch (NoSuchMethodException e) {
			throw NSForwardException._runtimeExceptionForThrowable(e);
		}			
	}
	
	protected NSSelector<T> getSelectorForTarget(Object obj, Object[] parameters) {
		NSMutableDictionary<ERXMultiKey, NSSelector<T>> dict = selectors.get(obj.getClass());
		NSSelector<T> sel = null;
		if(dict != null) {
			ERXMultiKey key = keyForParameters(parameters);
			sel = dict.get(key);
		}
		if(sel == null) {
			sel = createSelectorForTarget(obj, parameters);
		}
		return sel;
	}
	
	protected synchronized NSSelector<T> createSelectorForTarget(Object obj, Object[] parameters) {
		NSMutableDictionary<ERXMultiKey, NSSelector<T>> dict = selectors.get(obj.getClass());
		if(dict == null) {
			dict = new NSMutableDictionary<ERXMultiKey, NSSelector<T>>();
			selectors.setObjectForKey(dict, obj.getClass());
		}
		
		Class<?>[] params = argsForParameters(parameters);
		ERXMultiKey key = new ERXMultiKey(params);

		if(EOEnterpriseObjectClazz.class.equals(args[0])) {
			EOEnterpriseObjectClazz<?> clazz = (EOEnterpriseObjectClazz<?>)parameters[0];
			while(!EOEnterpriseObjectClazz.class.equals(clazz.getClass())) {
				NSSelector<T> sel = new NSSelector<T>(methodName, params);
				if(sel.implementedByObject(obj)) {
					dict.setObjectForKey(sel, key);
					return sel;
				}
				EOEntity e = clazz.entity().parentEntity();
				if(e == null) { break; }
				clazz = EOEnterpriseObjectClazz.clazzForEntityNamed(e.name());
				params[0] = clazz.getClass();
			}
			dict.setObjectForKey(defaultSelector, key);
			return defaultSelector;
		} else if(EOEnterpriseObject.class.equals(args[0])) {
			EOEnterpriseObject eo = (EOEnterpriseObject)parameters[0];
			EOEntity e = ERXEOAccessUtilities.entityForEo(eo);
			while(e != null) {
				NSSelector<T> sel = new NSSelector<T>(methodName, params);
				if(sel.implementedByObject(obj)) {
					dict.setObjectForKey(sel, key);
					return sel;
				}
				e = e.parentEntity();
				if(e != null) {
					params[0] = _NSUtilities.classWithName(e.className());
				}
			}
			dict.setObjectForKey(defaultSelector, key);
			return defaultSelector;
		}

		return defaultSelector;
	}
	
	protected ERXMultiKey keyForParameters(Object[] parameters) {
		Class<?>[] classes = argsForParameters(parameters);
		ERXMultiKey key = new ERXMultiKey(classes);
		return key;
	}
	
	protected Class<?>[] argsForParameters(Object[] parameters) {
		Class<?>[] newArgs = new Class[parameters.length];
		for(int i = 0; i < parameters.length; i++) {
			newArgs[i] = parameters[i].getClass();
		}
		return newArgs;
	}
	
	private void verifyClass(Class<?> expected, Object actual) {
		if(!expected.isAssignableFrom(actual.getClass())) {
			StringBuilder sb = new StringBuilder(200);
			sb.append("Class found: ");
			sb.append(actual.getClass().getName());
			sb.append(" Class expected: ");
			sb.append(expected.getName());
			throw new IllegalArgumentException(sb.toString());
		}
	}
}
