package er.r2d2w.components.misc;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXRadioButtonMatrix;

public class R2DRadioButtonFieldset extends ERXRadioButtonMatrix {
    public R2DRadioButtonFieldset(WOContext context) {
        super(context);
    }

	private String labelID;
	
	public void reset() {
		labelID = null;
		super.reset();
	}

	/**
	 * @return the labelID
	 */
	public String labelID() {
		return labelID;
	}

	public String generateID() {
		labelID = "id" + context().elementID();
		return labelID;
	}
}