package er.r2d2w.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WDisplayNumber;

public class R2D2WDisplayNumber extends D2WDisplayNumber {
    public R2D2WDisplayNumber(WOContext context) {
        super(context);
    }

	public Boolean hasWrapper() {
		Boolean b = Boolean.FALSE;
		if(objectPropertyValueIsNonNull()) {
			Object val = d2wContext().valueForKey("wrapperElement");
			b = (val == null || val.toString().equals("")?Boolean.FALSE:Boolean.TRUE);
		}
		return b;
	}
}