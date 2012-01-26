package er.r2d2w.components.misc;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSRange;

import er.extensions.components.ERXStatelessComponent;
import er.extensions.eof.ERXConstant;
import er.extensions.foundation.ERXValueUtilities;

public class R2DBatchNavigationBar extends ERXStatelessComponent {
	private WODisplayGroup displayGroup;
	private transient D2WContext d2wContext;
	private Object batchSize;
	private NSRange middleRange;
	private Integer index;

	public R2DBatchNavigationBar(WOContext context) {
		super(context);
	}

	public void reset() {
		super.reset();
		displayGroup = null;
		d2wContext = null;
		batchSize = null;
		middleRange = null;
	}

	public WODisplayGroup displayGroup() {
		if (displayGroup == null) {
			displayGroup = (WODisplayGroup) valueForBinding("displayGroup");
		}
		return displayGroup;
	}
	
	public D2WContext d2wContext() {
		if (d2wContext == null) {
			d2wContext = (D2WContext) valueForBinding("d2wContext");
		}
		return d2wContext;
	}

	/**
	 * @return the batchSize
	 */
	public Object batchSize() {
		return batchSize;
	}

	/**
	 * @param batchSize
	 *            the batchSize to set
	 */
	public void setBatchSize(Object batchSize) {
		this.batchSize = batchSize;
	}

	public void selectBatchSize() {
		int batchSize = ERXValueUtilities.intValue(batchSize());
		displayGroup().setNumberOfObjectsPerBatch(batchSize);
		displayGroup().setCurrentBatchIndex(1);
		displayGroup().clearSelection();
		NSNotificationCenter.defaultCenter().postNotification("BatchSizeChanged",
				ERXConstant.integerForInt(batchSize),
				new NSDictionary<String, Object>(d2wContext(), "d2wContext"));
	}

	public boolean currentBatchSizeSelected() {
		int batchSize = ERXValueUtilities.intValue(batchSize());
		return displayGroup().numberOfObjectsPerBatch() == batchSize;
	}

	public void selectFirstBatch() {
		displayGroup().setCurrentBatchIndex(1);
	}

	public boolean isFirstBatchSelected() {
		return displayGroup().currentBatchIndex() == 1;
	}

	public void selectLastBatch() {
		displayGroup().setCurrentBatchIndex(displayGroup().batchCount());
	}

	public boolean isLastBatchSelected() {
		return displayGroup().currentBatchIndex() == displayGroup().batchCount();
	}

	/**
	 * @return the middleRange
	 */
	public NSRange middleRange() {
		if(middleRange == null) {
			int batchCount = displayGroup().batchCount();
			if(batchCount == 2) {
				middleRange = new NSRange(2,0);
				return middleRange;
			}
			int currentBatch = displayGroup().currentBatchIndex();
			int batchLimit = ERXValueUtilities.intValue(d2wContext().valueForKey("batchLimit"));
			batchLimit = Math.max(batchLimit, 5);
			batchLimit = Math.min(batchLimit, batchCount);
			int centerBatch = batchLimit/2 + batchLimit%2;
			int length = batchLimit - 2;
			int location;
			if(currentBatch <= centerBatch) {
				location = 2;
			} else if(currentBatch > batchCount - centerBatch) {
				location = batchCount - batchLimit + 2;
			} else {
				location = currentBatch - centerBatch + 2;
			}
			middleRange = new NSRange(location, length);
		}
		return middleRange;
	}

	public String batchSizeString() {
		if(ERXValueUtilities.intValue(batchSize()) == 0) {
			String all = localizer().localizedStringForKey("R2DBatchSize.all");
			return all;
		}
		return String.valueOf(batchSize());
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

	public String batchIndex() {
		return String.valueOf(middleRange().location() + index().intValue());
	}

	public boolean isMiddleBatchSelected() {
		int currentBatch = displayGroup().currentBatchIndex();
		return currentBatch == middleRange().location() + index().intValue();
	}

	public void selectMiddleBatch() {
		displayGroup().setCurrentBatchIndex(middleRange().location() + index().intValue());
		middleRange = null;
	}

	public boolean hasFirstEllipsis() {
		return middleRange().location() != 2 && middleRange().length() != 0;
	}
	
	public boolean hasLastEllipsis() {
		return middleRange().maxRange() != displayGroup().batchCount();
	}
}