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
import er.extensions.localization.ERXLocalizer;


public class R2DDefaultUIStringAssignment extends ERDAssignment implements ERDLocalizableAssignmentInterface {
	public static final String ATTRIBUTE_ABBR = "attributeAbbr";
	public static final String INPUT_INFO = "inputInfo";
	public static final String TABLE_CAPTION = "tableCaption";
	public static final String TABLE_SUMMARY = "tableSummary";
	public static final String DYNAMIC_PAGE_PREFIX = "Page.";
	
	private static final NSArray<String> attributeAbbrDependentKeys = 
		new NSArray<String>(new String[] { D2WModel.PropertyKeyKey, D2WModel.EntityKey });
	private static final NSArray<String> inputInfoDependentKeys = 
		new NSArray<String>(new String[] { D2WModel.PropertyKeyKey, D2WModel.EntityKey });
	private static final NSArray<String> tableCaptionDependentKeys = 
		new NSArray<String>(new String[] { D2WModel.DynamicPageKey });
	private static final NSArray<String> tableSummaryDependentKeys = 
		new NSArray<String>(new String[] { D2WModel.DynamicPageKey });
	
	private static final NSArray<NSArray<String>> allKeys = 
		new NSArray<NSArray<String>>(attributeAbbrDependentKeys)
		.arrayByAddingObject(inputInfoDependentKeys)
		.arrayByAddingObject(tableCaptionDependentKeys)
		.arrayByAddingObject(tableSummaryDependentKeys);
	
	private static final NSDictionary<String, NSArray<String>> dependentKeyDict = 
		new NSDictionary<String, NSArray<String>>(allKeys, new NSArray<String>(
				new String[] {ATTRIBUTE_ABBR,
						INPUT_INFO,
						TABLE_CAPTION,
						TABLE_SUMMARY}));

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
		if(c.propertyKey() != null && c.entity() != null) {
			StringBuilder sb = new StringBuilder(100);
			String key = sb.append(c.entity().name()).append(NSKeyValueCodingAdditions._KeyPathSeparatorChar).append(c.propertyKey()).append(NSKeyValueCodingAdditions._KeyPathSeparatorChar).append(ATTRIBUTE_ABBR).toString();
			result = localizedStringForKey(key, c);
		}
		return result;
	}
	
	public String inputInfo(D2WContext c) {
		String result = null;
		if(c.propertyKey() != null && c.entity() != null) {
			StringBuilder sb = new StringBuilder(100);
			String key = sb.append(c.entity().name()).append(NSKeyValueCodingAdditions._KeyPathSeparatorChar).append(c.propertyKey()).append(NSKeyValueCodingAdditions._KeyPathSeparatorChar).append(INPUT_INFO).toString();
			result = localizedStringForKey(key, c);
		}
		return result;
	}
	
	public String tableCaption(D2WContext c) {
		String result = null;
		if(c.dynamicPage() != null && c.dynamicPage().length() > 0) {
			StringBuilder entityNameBuilder = new StringBuilder(100);
			String key = entityNameBuilder.append(DYNAMIC_PAGE_PREFIX).append(c.dynamicPage()).append(NSKeyValueCodingAdditions._KeyPathSeparatorChar).append(TABLE_CAPTION).toString();
			result = localizedStringForKey(key, c);
		}
		return result;
	}
	
	public String tableSummary(D2WContext c) {
		String result = null;
		if(c.dynamicPage() != null && c.dynamicPage().length() > 0) {
			StringBuilder entityNameBuilder = new StringBuilder(100);
			String key = entityNameBuilder.append(DYNAMIC_PAGE_PREFIX).append(c.dynamicPage()).append(NSKeyValueCodingAdditions._KeyPathSeparatorChar).append(TABLE_SUMMARY).toString();
			result = localizedStringForKey(key, c);
		}
		return result;
	}
	
	public String localizedStringForKey(String key, D2WContext c) {
		String template = (String)ERXLocalizer.currentLocalizer().valueForKey(key);
		if(template == null) {
			template = "";
			ERXLocalizer.currentLocalizer().takeValueForKey(template, key);
		}
		if(template.length() == 0) { return null; }
		return ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject(template, c);
	}
}
