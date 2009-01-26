package er.r2d2w.components.buttons;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.ERD2WUtilities;
import com.webobjects.foundation.NSDictionary;

import er.directtoweb.components.buttons.ERDActionButton;
import er.directtoweb.pages.ERD2WListPage;
import er.extensions.foundation.ERXValueUtilities;
import er.r2d2w.R2D2WDirectAction;

public class R2DPodcastButton extends ERDActionButton {
    
    public static final String PODCAST_SCHEME = "itpc";

    public R2DPodcastButton(WOContext context) {
        super(context);
    }
    
	public boolean displayPodcast() {
		boolean result = false;
		D2WContext c = d2wContext();
		if(c != null) {
			boolean hasPodcast = ERXValueUtilities.booleanValueWithDefault(c.valueForKey("hasPodcast"), false);
			result = hasPodcast && "list".equals(d2wContext().task());
		}
		return result;
	}
	
	public String podcastLink() {
		String result = null;
		
		// Generate complete url
		Boolean generatesComplete = context().doesGenerateCompleteURLs();
		if(!generatesComplete) {context().generateCompleteURLs();}
		result = context().directActionURLForActionNamed(directActionName(), queryDictionary(), false, 0, true);
		if(!generatesComplete) {context().generateRelativeURLs();}
		
		// Replace scheme with itpc
		result = PODCAST_SCHEME + result.substring(result.indexOf(":"));
		
		return result;
	}

	public String directActionName() {
		return "RSS" + d2wContext().entity().name();
	}

	public NSDictionary<String, Object> queryDictionary() {
		ERD2WListPage lp = (ERD2WListPage)ERD2WUtilities.parentListPage(this);
		WODisplayGroup dg = lp.displayGroup();
		return R2D2WDirectAction.queryDictionaryForListPage(dg.dataSource(), dg.sortOrderings());
	}

}