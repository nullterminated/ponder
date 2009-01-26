package com.example.app;

import org.apache.log4j.Logger;

import er.extensions.appserver.ERXApplication;

public class Application extends ERXApplication {
	private static Logger log = Logger.getLogger(Application.class);
	
    public static void main(String argv[]) {
        ERXApplication.main(argv, Application.class);
    }

    public Application() {
        log.info("Welcome to " + this.name() + " !");
        /* ** put your initialization code in here ** */
        
        // Fix for WO 5.4.x bug.  Note, heisenbug. frameworksBaseURL must be called
        // at least once or the value for setFrameworksBaseURL will be overwritten by it later.
        String defaultFrameworksBaseURL = frameworksBaseURL();
        String frameworksBaseURL = System.getProperty("WOFrameworksBaseURL", defaultFrameworksBaseURL);
        if(!defaultFrameworksBaseURL.equals(frameworksBaseURL)) {
        	log.warn("WOFrameworksBaseURL was ignored. I am correcting the setting.");
        	setFrameworksBaseURL(frameworksBaseURL);
        }
        
    }
}