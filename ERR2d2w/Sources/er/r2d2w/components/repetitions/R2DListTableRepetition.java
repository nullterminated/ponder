package er.r2d2w.components.repetitions;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSKeyValueCodingAdditions;
import com.webobjects.foundation.NSMutableDictionary;

import er.directtoweb.components.repetitions.ERDAttributeRepetition;
import er.extensions.ERXExtensions;
import er.extensions.appserver.ERXWOContext;

public class R2DListTableRepetition extends ERDAttributeRepetition {

	private Object sublistSection;
	private NSArray<Object> sublist;
	private Integer index;
	private NSMutableDictionary<String, String> attributeHeaderIDs = new NSMutableDictionary<String, String>();
	private NSMutableDictionary<Object, String> groupingHeaderIDs = new NSMutableDictionary<Object, String>();
	private NSMutableDictionary<String, String> sectionHeaderIDs = new NSMutableDictionary<String, String>();
	
	public R2DListTableRepetition(WOContext context) {
        super(context);
    }

	public boolean isNotMultipleSections() {
		return !(sectionsContents().count() > 1);
	}

    public String groupingItemKey() {
        return stringValueForBinding("groupingItemKey");
    }

    public Object section() {
		Object section = NSKeyValueCodingAdditions.Utility.valueForKeyPath(d2wContext().valueForKey("object"), groupingItemKey());
		return section;
    }

	/**
	 * @return the sublistSection
	 */
	public Object sublistSection() {
		return sublistSection;
	}

	/**
	 * @param sublistSection the sublistSection to set
	 */
	public void setSublistSection(Object sublistSection) {
		this.sublistSection = sublistSection;
	}

	/**
	 * @return the sublist
	 */
	public NSArray<Object> sublist() {
		return sublist;
	}

	/**
	 * @param sublist the sublist to set
	 */
	public void setSublist(NSArray<Object> sublist) {
		this.sublist = sublist;
	}

	/**
	 * @return the index
	 */
	public Integer index() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	public boolean isGrouping() {
		return ERXExtensions.safeEquals(d2wContext().valueForKey("subTask"), "group");
	}

	public String attributeHeaderID() {
		String key = d2wContext().propertyKey();
		if(!attributeHeaderIDs.containsKey(key)) {
			attributeHeaderIDs.setObjectForKey(ERXWOContext.safeIdentifierName(context(), true), key);
		}
		return attributeHeaderIDs.objectForKey(key);
	}

	public String groupingHeaderID() {
		Object key = sublistSection();
		if(key == null) { key = NSKeyValueCoding.NullValue; }
		if(!groupingHeaderIDs.containsKey(key)) {
			groupingHeaderIDs.setObjectForKey(ERXWOContext.safeIdentifierName(context(), true), key);
		}
		return groupingHeaderIDs.objectForKey(key);
	}

	public String sectionHeaderID() {
		String key = currentSection().name;
		if(!sectionHeaderIDs.containsKey(key)) {
			sectionHeaderIDs.setObjectForKey(ERXWOContext.safeIdentifierName(context(), true), key);
		}
		return sectionHeaderIDs.objectForKey(key);
	}
	
	public String headers() {
		StringBuilder sb = new StringBuilder(attributeHeaderID());
		if(sectionsContents().count() > 1) {
			sb.append(' ').append(sectionHeaderID());
		}
		if(isGrouping()) {
			sb.append(' ').append(groupingHeaderID());
		}
		return sb.toString();
	}
}