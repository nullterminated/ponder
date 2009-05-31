package er.r2d2w.components.misc;

import com.webobjects.appserver.WOContext;

import er.extensions.batching.ERXBatchNavigationBar;

/**
 * XHTML Strict version of ERXBatchNavigationBar to page through display groups.<br />
 * 
 * @binding d2wContext the D2W context that this component is in
 * @binding displayGroup the WODisplayGroup that is being controlled
 * @binding objectName the name of the type of object that is contained in the WODisplayGroup
 * @binding sortKeyList an NSArray of sort key paths that will be displayed in a popup button
 * @binding clearSelection boolean that indicates if the selection should be reset on paging (default false)
 */
public class R2DBatchNavigationBar extends ERXBatchNavigationBar {

	private String batchSizeID;
	private String batchIndexID;

	public R2DBatchNavigationBar(WOContext context) {
        super(context);
    }

	public void reset() {
		batchSizeID = null;
		batchIndexID = null;
		super.reset();
	}
	
	/**
	 * @return the batchSizeID
	 */
	public String batchSizeID() {
		if(batchSizeID == null) {
			batchSizeID = "id" + context().elementID();
		}
		return batchSizeID;
	}

	/**
	 * @return the batchIndexID
	 */
	public String batchIndexID() {
		if(batchIndexID == null) {
			batchIndexID = "id" + context().elementID();
		}
		return batchIndexID;
	}

}