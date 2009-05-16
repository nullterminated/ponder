package er.r2d2w.pages;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSDictionary;

import er.directtoweb.pages.ERD2WListPage;
import er.extensions.eof.ERXEOControlUtilities;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.foundation.ERXValueUtilities;
import er.extensions.localization.ERXLocalizer;
import er.r2d2w.R2D2WDirectAction;

public class R2D2WListRSSPage extends ERD2WListPage {
	
	/** logging support */
	public final static Logger log = Logger.getLogger(R2D2WListRSSPage.class);

	public static final String CONTENT_TYPE_KEY = "content-type";
	public static final String CONTENT_TYPE_VALUE = "application/xml";
	
	public R2D2WListRSSPage(WOContext context) {
        super(context);
    }
	
	public void appendToResponse(WOResponse response, WOContext context) {
		super.appendToResponse(response, context);
		response.setHeader(CONTENT_TYPE_VALUE, CONTENT_TYPE_KEY);
	}
	
	public String otherTagStringForRSSTag() {
		StringBuilder sb = new StringBuilder();
		NSDictionary<String,Object> rssNamespaces = ERXValueUtilities.dictionaryValueWithDefault(d2wContext().valueForKey("rssNamespaces"), NSDictionary.EmptyDictionary);
		for(Object key: rssNamespaces.allKeys()) {
			sb.append("xmlns:").append(key).append("=\"").append(rssNamespaces.objectForKey(key)).append("\" ");
		}
		sb.append("version=\"2.0\"");
		return sb.toString();
	}

	public String selfLink() {
		return context().directActionURLForActionNamed("RSS" + d2wContext().entity().name(), queryDictionary(), false, 0, true);
	}
	
	public NSDictionary<String, Object> queryDictionary() {
		return R2D2WDirectAction.queryDictionaryForListPage(dataSource(), displayGroup().sortOrderings());
	}

	public void awake() {
		super.awake();
		context().generateCompleteURLs();
	}

	public Boolean hasTTL() {
		return rssTTL() > 0;
	}

	public String itemAction() {
		return "Inspect" + object().entityName();
	}
	
	public String itemID() {
		return object().entityName() + ERXEOControlUtilities.primaryKeyStringForObject(object());
	}

	public NSDictionary<String, Object> itemQueryDictionary() {
		EOEnterpriseObject eo = (EOEnterpriseObject)object();
		return R2D2WDirectAction.inspectQueryDictionary(eo);
	}
	
	public String itemURL() {
		return context().directActionURLForActionNamed(itemAction(), itemQueryDictionary());
	}
	
	public String languageCode() {
		if (!ERXLocalizer.isLocalizationEnabled()) { return null; }
		return ERXLocalizer.currentLocalizer().languageCode();
	}

	public int rssTTL() {
		int result = 0;
		D2WContext c = d2wContext();
		String ttl = (String)c.valueForKey("rssTTL");
		if (!ERXStringUtilities.stringIsNullOrEmpty(ttl)) {
			try {
				result = Integer.parseInt(ttl);
			} catch (NumberFormatException e) {
				// Could not parse string value
				log.warn(e.getMessage(), e);
			}
		}		
		return result;
	}

	public boolean missingAtom() {
		NSDictionary<String,Object> rssNamespaces = ERXValueUtilities.dictionaryValueWithDefault(d2wContext().valueForKey("rssNamespaces"), NSDictionary.EmptyDictionary);
		return !rssNamespaces.containsKey("atom");
	}

}