package er.r2d2w.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WDisplayDate;

public class R2D2WDisplayDate extends D2WDisplayDate {

	public R2D2WDisplayDate(WOContext context) {
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