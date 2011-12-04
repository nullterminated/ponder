package er.datum;

import org.apache.log4j.Logger;

import er.attributeextension.ERAttributeExtension;
import er.extensions.ERXFrameworkPrincipal;

/**
 * <h1>Principal class of the ERDatum framework.</h1>
 * 
 * <p> The ERDatum framework is another attempt at providing for storage of 
 * arbitrary data at runtime.  ERAddressBookExample app demonstrates usage.
 * </p>
 * 
 * @author Ramsey Gurley
 *
 */
public class ERDatumPrincipal extends ERXFrameworkPrincipal {
	public static final Class<?>[] REQUIRES = new Class[] { ERAttributeExtension.class };

	protected static volatile ERDatumPrincipal sharedInstance;

	private static final Logger log = Logger.getLogger(ERDatumPrincipal.class);

	// Registers the class as the framework principal
	static {
		log.debug("Static Initializer for ERR2d2w");
		setUpFrameworkPrincipalClass(ERDatumPrincipal.class);
	}

	public static ERDatumPrincipal sharedInstance() {
		if (sharedInstance == null) {
			synchronized (ERDatumPrincipal.class) {
				if (sharedInstance == null) {
					sharedInstance = sharedInstance(ERDatumPrincipal.class);
				}
			}
		}
		return sharedInstance;
	}

	@Override
	public void finishInitialization() {
		// TODO Auto-generated method stub

	}
}
