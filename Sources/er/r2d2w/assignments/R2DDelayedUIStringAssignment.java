package er.r2d2w.assignments;

import com.webobjects.directtoweb.D2WContext;
import com.webobjects.eocontrol.EOKeyValueArchiver;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSKeyValueCodingAdditions;

import er.directtoweb.assignments.delayed.ERDDelayedAssignment;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.localization.ERXLocalizer;

public class R2DDelayedUIStringAssignment extends ERDDelayedAssignment {
	public static final String BANNER_STRING = "bannerString";
	public static final String DISPLAY_NAME_FOR_DYNAMIC_PAGE = "displayNameForPageConfiguration";
	private static final StringBuilder taskBuilder = new StringBuilder("ERD2W.tasks.");
	private static final StringBuilder pageTitleBuilder = new StringBuilder("PageTitle.");
	private static final String defaultPageTitleKey = "PageTitle.DefaultPageTitle";
	private static final String subTaskKey = "subTask";

	public R2DDelayedUIStringAssignment(EOKeyValueUnarchiver u) {
		super(u);
	}

	public R2DDelayedUIStringAssignment(String key, Object value) {
		super(key, value);
	}
	
	public void encodeWithKeyValueArchiver(EOKeyValueArchiver archiver) {
		super.encodeWithKeyValueArchiver(archiver);
	}

	public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver unarchiver) {
		return new R2DDelayedUIStringAssignment(unarchiver);
	}

	@Override
	public Object fireNow(D2WContext c) {
		if(BANNER_STRING.equals(keyPath())) {
			return bannerString(c);
		} else if (DISPLAY_NAME_FOR_DYNAMIC_PAGE.equals(keyPath())) {
			return displayNameForPageConfiguration(c);
		}
		return null;
	}

	public String bannerString(D2WContext c) {
		String result = null;
		if(c.task() != null) {
			ERXLocalizer loc = ERXLocalizer.currentLocalizer();
			String locKey = null;
			
			taskBuilder.append(c.task());
			if(!ERXStringUtilities.stringIsNullOrEmpty((String)c.valueForKey(subTaskKey))) {
				taskBuilder.append(NSKeyValueCodingAdditions.KeyPathSeparator).append(c.valueForKey(subTaskKey));
			}
			locKey = taskBuilder.toString();
			taskBuilder.setLength(12);
			result = (loc.valueForKey(locKey) == null)?null:loc.localizedTemplateStringForKeyWithObject(locKey, c);
		}
		return result;
	}
	
	public String displayNameForPageConfiguration(D2WContext c) {
		String result = null;
		ERXLocalizer loc = ERXLocalizer.currentLocalizer();
		
		if(c.dynamicPage() != null) {
			String locKey = pageTitleBuilder.append(c.dynamicPage()).toString();
			pageTitleBuilder.setLength(10);
			result = (loc.valueForKey(locKey)==null)?null:loc.localizedTemplateStringForKeyWithObject(locKey, c);
		}
		
		if(result==null){
			result = loc.localizedTemplateStringForKeyWithObject(defaultPageTitleKey, c);
		}
		
		return result;
	}

}
