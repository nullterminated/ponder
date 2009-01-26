package com.example.app;

import com.example.components.Main;
import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;

import er.r2d2w.R2D2WDirectAction;

public class DirectAction extends R2D2WDirectAction {

    public DirectAction(WORequest aRequest) {
        super(aRequest);
    }
   
    public WOActionResults defaultAction() {
        return pageWithName(Main.class.getName());
    }
    
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
    public WOActionResults performActionNamed(String actionName) {
    	WOActionResults result = super.performActionNamed(actionName);
    	return new ResponseWrapper(result);
    }
    
    /**
     * Checks if a page configuration is allowed to render.
     * Override for a more intelligent access scheme as the default just returns false.
     * @param pageConfiguration
     */
    protected boolean allowPageConfiguration(String pageConfiguration) {
        return true;
    }
    
	public class ResponseWrapper implements WOActionResults {
    	private static final String acceptKey = "accept";
    	private static final String contentTypeKey = "content-type";
    	private static final String htmlType = "text/html";
    	private static final String xhtmlType = "application/xhtml+xml";
    	private WOActionResults responseObject;
    	
    	public ResponseWrapper(WOActionResults wrappedResponse) {
    		responseObject = wrappedResponse;
    	}
    	
    	public WOResponse generateResponse() {
    		WOResponse response = responseObject.generateResponse();
    		WORequest request = DirectAction.this.request();
    		String accept = request.headerForKey(acceptKey);
    		if(accept != null && accept.contains(xhtmlType)) {
    			String contentType = response.headerForKey(contentTypeKey);
    			if(contentType != null && contentType.contains(htmlType)) {
    				response.setHeader(xhtmlType, contentTypeKey);
    			}
    		}
    		
    		return response;
    	}
    	
    }

}