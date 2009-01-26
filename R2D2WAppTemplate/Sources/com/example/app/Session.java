package com.example.app;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSLog;

import er.extensions.appserver.ERXSession;

public class Session extends ERXSession {
	
	public Session() {
		NSLog.out.appendln("Session created: " + sessionID());
		
		setStoresIDsInCookies(true);
		setStoresIDsInURLs(false);
	}

	private Boolean acceptsXHTML = null;
	private static final String acceptKey = "accept";
	private static final String contentTypeKey = "content-type";
	private static final String htmlType = "text/html";
	private static final String xhtmlType = "application/xhtml+xml";

	/**
	 * <p>Since R2D2W is generating XHTML, override this method
	 * to set the response <code>content-type</code> header to
	 * <code>application/xhtml+xml</code> if the browser accepts it. 
	 * This should:</p>
	 * <ul><li>Allow for faster page rendering because the browser 
	 * does not need to correct errors.</li>
	 * <li>Cause rendering to fail if the page is not well formed.</li>
	 * <li>Allow for interesting things like inline SVG</li></ul>
	 */
	public void appendToResponse(WOResponse response, WOContext context) {
		if (acceptsXHTML == null) {
			String acceptTypes = context.request().headerForKey(acceptKey);
			acceptsXHTML = (acceptTypes != null && acceptTypes.contains(xhtmlType));
		}
		if (acceptsXHTML) {
			String responseType = response.headerForKey(contentTypeKey);
			if (responseType.equals(htmlType)) {
				response.setHeader(xhtmlType, contentTypeKey);	
			}
		}
		super.appendToResponse(response, context);

	}	

}