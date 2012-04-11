package er.auth;

import org.apache.log4j.Logger;

import com.webobjects.foundation.NSMutableDictionary;

import er.auth.model.ERAuthenticationRequest;
import er.auth.model.ERTwoFactorAuthenticationRequest;
import er.auth.processing.ERAuthenticationProcessor;
import er.auth.processing.ERTwoFactorAuthenticationProcessor;
import er.corebusinesslogic.ERCoreBusinessLogic;
import er.extensions.ERXFrameworkPrincipal;
import er.extensions.eof.EOEnterpriseObjectClazz;
import er.javamail.ERJavaMail;

/**
 * Authentication and Authorization framework
 * @author Ramsey Gurley
 *
 */
public class ERAuth extends ERXFrameworkPrincipal {
	public static final Class<?>[] REQUIRES = new Class[] {ERCoreBusinessLogic.class, ERJavaMail.class};
	
	protected static volatile ERAuth sharedInstance;
	
	private static final Logger log = Logger.getLogger(ERAuth.class);
	
	private static NSMutableDictionary<String, ERAuthenticationProcessor<?>> _processors;
	
	// Registers the class as the framework principal
	static {
    	log.debug("Static Initializer for ERAuth");
		setUpFrameworkPrincipalClass(ERAuth.class);
		_processors = new NSMutableDictionary<String, ERAuthenticationProcessor<?>>();
		_processors.setObjectForKey(new ERTwoFactorAuthenticationProcessor(), ERTwoFactorAuthenticationRequest.ENTITY_NAME);
		ERStageManager.INSTANCE.getClass();
	}
	
	@Override
	public void finishInitialization() {
		//Set the auth clazz factory to prevent clazz duplication 
		EOEnterpriseObjectClazz.setFactory(new ERAuthClazzFactory());
	}

	public static ERAuth sharedInstance() {
		if(sharedInstance == null) {
			synchronized (ERAuth.class) {
				if(sharedInstance == null) {
					sharedInstance = sharedInstance(ERAuth.class);
				}
			}
		}
		return sharedInstance;
	}
	
	public synchronized <T extends ERAuthenticationRequest> ERAuthenticationProcessor<T> processorForType(T authRequest) {
		return processorForType(authRequest == null?null:authRequest.subtype());
	}
	
	@SuppressWarnings("unchecked")
	public synchronized <T extends ERAuthenticationRequest> ERAuthenticationProcessor<T> processorForType(String requestType) {
		ERAuthenticationProcessor<T> processor = (ERAuthenticationProcessor<T>)_processors.objectForKey(requestType);
		if(processor == null) {
			throw new IllegalArgumentException("No Auth processor found for request type: " + requestType);
		}
		return processor;
	}
	
	public synchronized <T extends ERAuthenticationRequest> void registerProcessorForType(String requestType, ERAuthenticationProcessor<T> processor) {
		_processors.takeValueForKey(processor, requestType);
	}

}
