package er.r2d2w.components;

import com.webobjects.appserver.WOContext;

import er.directtoweb.components.strings.ERD2WQueryStringOperator;

public class R2D2WQueryStringOperator extends ERD2WQueryStringOperator {
    private String labelID;

	public R2D2WQueryStringOperator(WOContext context) {
        super(context);
    }

	/**
	 * @return the labelID
	 */
	public String labelID() {
		return labelID;
	}

	/**
	 * @param labelID the labelID to set
	 */
	public void setLabelID(String labelID) {
		this.labelID = labelID;
	}
}