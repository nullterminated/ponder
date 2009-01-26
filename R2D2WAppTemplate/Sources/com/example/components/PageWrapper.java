package com.example.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2W;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.D2WPage;

import er.extensions.components.ERXComponent;
import er.extensions.localization.ERXLocalizer;

public class PageWrapper extends ERXComponent {

    public PageWrapper(WOContext aContext) {
        super(aContext);
    }
    
    public D2WContext d2wContext() {
    	if (context().page() instanceof D2WPage) {
			D2WPage d2wPage = (D2WPage) context().page();
			return d2wPage.d2wContext();
		}
    	return null;
    }

	public String languageCode() {
		return ERXLocalizer.currentLocalizer().languageCode();
	}

	public String homeLink() {
		return D2W.factory().homeHrefInContext(context());
	}

}
