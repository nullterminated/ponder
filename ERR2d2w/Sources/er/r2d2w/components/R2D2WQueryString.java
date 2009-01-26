package er.r2d2w.components;

import com.webobjects.appserver.WOContext;

import er.directtoweb.components.ERDCustomQueryComponent;

public class R2D2WQueryString extends ERDCustomQueryComponent {
    private String labelID;

	public R2D2WQueryString(WOContext context) {
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