package com.example.components;

import com.webobjects.appserver.WOContext;
import er.extensions.components.ERXComponent;
import er.extensions.localization.ERXLocalizer;

public class MailWrapper extends ERXComponent {
    public MailWrapper(WOContext context) {
        super(context);
    }
    
	public String languageCode() {
		return ERXLocalizer.currentLocalizer().languageCode();
	}

}