package er.r2d2w.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WQueryNumberRange;

public class R2D2WQueryNumberRange extends D2WQueryNumberRange {
    private String maxID;
	private String minID;

	public R2D2WQueryNumberRange(WOContext context) {
        super(context);
    }

	/**
	 * @return the maxID
	 */
	public String maxID() {
		return maxID;
	}

	/**
	 * @param maxID the maxID to set
	 */
	public void setMaxID(String maxID) {
		this.maxID = maxID;
	}

	/**
	 * @return the minID
	 */
	public String minID() {
		return minID;
	}

	/**
	 * @param minID the minID to set
	 */
	public void setMinID(String minID) {
		this.minID = minID;
	}
}