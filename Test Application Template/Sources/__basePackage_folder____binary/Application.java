package ${basePackage};

import junit.textui.TestRunner;
import er.extensions.appserver.ERXApplication;

public class Application extends ERXApplication {
	public static void main(String[] argv) {
		ERXApplication.main(argv, Application.class);
	}

	public Application() {
		ERXApplication.log.info("Welcome to " + name() + " !");
		/* ** put your initialization code in here ** */
		setAllowsConcurrentRequestHandling(true);
		setAutoOpenInBrowser(false);
	}

	protected boolean isLaunchingFromEclipse() {
		String classPath = System.getProperty("java.class.path");
		return classPath != null && classPath.contains("org.eclipse.osgi/bundles");
	}
	
	public void didFinishLaunching() {
		super.didFinishLaunching();
		
		if(!isLaunchingFromEclipse()) {
			TestRunner.run(${testClassesPrefix}TestSuite.suite());
			System.exit(0);
		}
	}
}
