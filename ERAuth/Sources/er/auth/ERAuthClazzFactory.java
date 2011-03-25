package er.auth;

import java.lang.reflect.Field;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.foundation.NSForwardException;

import er.extensions.eof.EOEnterpriseObjectClazz;
import er.extensions.eof.EOEnterpriseObjectClazz.DefaultClazzFactory;

public class ERAuthClazzFactory extends DefaultClazzFactory {

    /**
     * Overridden to always return the single static clazz instance if possible.
     * @param entity to generate the clazz for
     * @return clazz object for the given entity
     */
	@SuppressWarnings("rawtypes")
	@Override
    public EOEnterpriseObjectClazz classFromEntity(EOEntity entity) {
        EOEnterpriseObjectClazz clazz = null;
        if(entity == null) {
            clazz = newInstanceOfDefaultClazz();
        } else {
            try {
                String className = entity.className();
                if(classNameIsGenericRecord(className)) {
                    clazz = newInstanceOfGenericRecordClazz();
                } else {
                	try {
	                	Class klass = Class.forName(className);
	                	Field field = klass.getDeclaredField("clazz");
	                	Object obj = field.get(null);
	                	return (EOEnterpriseObjectClazz)obj;
                	} catch (ClassNotFoundException e) {
                		//Can't find entity class?
                		throw NSForwardException._runtimeExceptionForThrowable(e);
                	} catch (NoSuchFieldException e) {
                		//Field doesn't exist. Then try to instantiate clazzName
                	} catch (IllegalAccessException e) {
                		//Field is not public. Try to instantiate clazzName
                	}
                	String clazzName = clazzNameForEntity(entity);
                    clazz = (EOEnterpriseObjectClazz)Class.forName(clazzName).newInstance();
                }
            } catch (InstantiationException ex) {
            } catch (ClassNotFoundException ex) {
            } catch (IllegalAccessException ex) {
            }
        }
        if(clazz == null) return classFromEntity(entity.parentEntity());
        return clazz;
    }

}
