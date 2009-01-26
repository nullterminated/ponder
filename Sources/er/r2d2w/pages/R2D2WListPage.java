package er.r2d2w.pages;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.directtoweb.pages.ERD2WListPage;
import er.extensions.appserver.ERXResponseRewriter;
import er.extensions.appserver.ERXSession;

public class R2D2WListPage extends ERD2WListPage {
    public R2D2WListPage(WOContext context) {
        super(context);
    }

	public void appendToResponse(WOResponse aResponse, WOContext aContext) {
		super.appendToResponse(aResponse, aContext);
		if(((ERXSession)session()).javaScriptEnabled()) {
			ERXResponseRewriter.addScriptResourceInHead(aResponse, aContext, "ERR2d2w", "dragtable.js");
		}
	}
	    
    @SuppressWarnings("unchecked")
	public NSArray displayKeys() {
    	NSMutableArray keys = new NSMutableArray();
    	D2WContext c = d2wContext();
    	keys.addObjectsFromArray((NSArray)c.valueForKey("displayPropertyKeys"));
    	keys.addObjectsFromArray((NSArray)c.valueForKey("tableActions"));
    	return keys;
    }
        
}