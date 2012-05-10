#if ($entity.packageName)
package $entity.packageName;

#end
import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;

public#if (${entity.abstractEntity}) abstract#end class ${entity.classNameWithoutPackage} extends ${entity.prefixClassNameWithOptionalPackage} {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(${entity.classNameWithoutPackage}.class);

    public static final ${entity.classNameWithoutPackage}Clazz<${entity.classNameWithoutPackage}> clazz = new ${entity.classNameWithoutPackage}Clazz<${entity.classNameWithoutPackage}>();
    public static class ${entity.classNameWithoutPackage}Clazz<T extends ${entity.classNameWithoutPackage}> extends ${entity.prefixClassNameWithOptionalPackage}.${entity.prefixClassNameWithoutPackage}Clazz<T> {
        /* more clazz methods here */
    }

    /**
     * Initializes the EO. This is called when an EO is created, not when it is 
     * inserted into an EC.
     */
    public void init(EOEditingContext ec) {
        super.init(ec);
    }

}
