package er.r2d2w.assignments;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.D2WModel;
import com.webobjects.eocontrol.EOKeyValueArchiver;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCodingAdditions;

import er.directtoweb.assignments.ERDAssignment;
import er.directtoweb.assignments.ERDLocalizableAssignmentInterface;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.localization.ERXLocalizer;


public class R2DDefaultUIStringAssignment extends ERDAssignment implements ERDLocalizableAssignmentInterface {
	public static final String ATTRIBUTE_ABBR = "attributeAbbr";
	public static final String BANNER_STRING = "bannerString";
	public static final String DISPLAY_NAME_FOR_DYNAMIC_PAGE = "displayNameForPageConfiguration";
	public static final String INPUT_INFO = "inputInfo";
	public static final String TABLE_CAPTION = "tableCaption";
	public static final String TABLE_SUMMARY = "tableSummary";

	private static final String defaultPageTitleKey = "PageTitle.DefaultPageTitle";
	private static final String subTaskKey = "subTask";
	
	private static final NSArray<String> attributeAbbrDependentKeys = 
		new NSArray<String>(new String[] { D2WModel.PropertyKeyKey });
	private static final NSArray<String> bannerStringDependentKeys =
		new NSArray<String>(new String[] {D2WModel.TaskKey, subTaskKey});
	private static final NSArray<String> displayNameForPageConfigurationDependentKeys = 
		new NSArray<String>(new String[] { D2WModel.DynamicPageKey });
	private static final NSArray<String> inputInfoDependentKeys = 
		new NSArray<String>(new String[] { D2WModel.PropertyKeyKey });
	private static final NSArray<String> tableCaptionDependentKeys = 
		new NSArray<String>(new String[] { D2WModel.EntityKey });
	private static final NSArray<String> tableSummaryDependentKeys = 
		new NSArray<String>(new String[] { D2WModel.EntityKey });
	
	private static final NSArray<NSArray<String>> allKeys = 
		new NSArray<NSArray<String>>(attributeAbbrDependentKeys)
		.arrayByAddingObject(bannerStringDependentKeys)
		.arrayByAddingObject(displayNameForPageConfigurationDependentKeys)
		.arrayByAddingObject(inputInfoDependentKeys)
		.arrayByAddingObject(tableCaptionDependentKeys)
		.arrayByAddingObject(tableSummaryDependentKeys);
	
	private static final NSDictionary<String, NSArray<String>> dependentKeyDict = 
		new NSDictionary<String, NSArray<String>>(allKeys, new NSArray<String>(
				new String[] {ATTRIBUTE_ABBR,
						BANNER_STRING,
						DISPLAY_NAME_FOR_DYNAMIC_PAGE,
						INPUT_INFO,
						TABLE_CAPTION,
						TABLE_SUMMARY}));

	private static final StringBuilder propertyKeyBuilder = new StringBuilder("PropertyKey.");
	private static final StringBuilder entityNameBuilder = new StringBuilder("Entity.name.");
	private static final StringBuilder taskBuilder = new StringBuilder("ERD2W.tasks.");
	private static final StringBuilder pageTitleBuilder = new StringBuilder("PageTitle.");
	
	
	public R2DDefaultUIStringAssignment(EOKeyValueUnarchiver u) {
		super(u);
	}

	public R2DDefaultUIStringAssignment(String key, Object value) {
		super(key, value);
	}

	public void encodeWithKeyValueArchiver(EOKeyValueArchiver archiver) {
		super.encodeWithKeyValueArchiver(archiver);
	}

	public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver unarchiver) {
		return new R2DDefaultUIStringAssignment(unarchiver);
	}
	
	public NSArray<String> dependentKeys(String keyPath) {
		return dependentKeyDict.get(keyPath);
	}
	
	public String attributeAbbr(D2WContext c) {
		String result = null;
		ERXLocalizer loc = ERXLocalizer.currentLocalizer();
		String locKey = null;
		if(c.propertyKey() != null) {
			locKey = propertyKeyBuilder.append(c.propertyKey()).append(NSKeyValueCodingAdditions.KeyPathSeparator).append(ATTRIBUTE_ABBR).toString();
			propertyKeyBuilder.setLength(12);
			result = (loc.valueForKey(locKey) == null)?null:loc.localizedTemplateStringForKeyWithObject(locKey, c);
		}
		return result;
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

	public String inputInfo(D2WContext c) {
		String result = null;
		ERXLocalizer loc = ERXLocalizer.currentLocalizer();
		String locKey = null;
		if(c.propertyKey() != null) {
			locKey = propertyKeyBuilder.append(c.propertyKey()).append(NSKeyValueCodingAdditions.KeyPathSeparator).append(INPUT_INFO).toString();
			propertyKeyBuilder.setLength(12);
			result = (loc.valueForKey(locKey) == null)?null:loc.localizedTemplateStringForKeyWithObject(locKey, c);
		}
		return result;
	}
	
	public String tableCaption(D2WContext c) {
		String result = null;
		ERXLocalizer loc = ERXLocalizer.currentLocalizer();
		String locKey = null;
		if(c.entity() != null) {
			locKey = entityNameBuilder.append(c.entity().name()).append(NSKeyValueCodingAdditions.KeyPathSeparator).append(TABLE_CAPTION).toString();
			entityNameBuilder.setLength(12);
			result = (loc.valueForKey(locKey) == null)?null:loc.localizedTemplateStringForKeyWithObject(locKey, c);
		}
		return result;
	}
	
	public String tableSummary(D2WContext c) {
		String result = null;
		ERXLocalizer loc = ERXLocalizer.currentLocalizer();
		String locKey = null;
		if(c.entity() != null) {
			locKey = entityNameBuilder.append(c.entity().name()).append(NSKeyValueCodingAdditions.KeyPathSeparator).append(TABLE_SUMMARY).toString();
			entityNameBuilder.setLength(12);
			result = (loc.valueForKey(locKey) == null)?null:loc.localizedTemplateStringForKeyWithObject(locKey, c);
		}
		return result;
	}
	
}
