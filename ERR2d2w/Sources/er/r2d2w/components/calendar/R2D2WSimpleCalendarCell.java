package er.r2d2w.components.calendar;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import er.directtoweb.components.ERD2WStatelessComponent;
import er.extensions.localization.ERXLocalizer;
import er.r2d2w.foundation.R2DDateRangeGrouper;
import java.text.SimpleDateFormat;

public class R2D2WSimpleCalendarCell extends ERD2WStatelessComponent {

	private static final String format = "d";
	private SimpleDateFormat dateFormat;

	public R2D2WSimpleCalendarCell(WOContext context) {
        super(context);
    }
	
	public void reset() {
		dateFormat = null;
		super.reset();
	}
    
    public R2DDateRangeGrouper rangeGrouper() {
    	return (R2DDateRangeGrouper)valueForBinding("rangeGrouper");
    }
    
    public WOActionResults selectDateAction() {
        WOActionResults nextPage = context().page();
        R2DDateRangeGrouper grouper = rangeGrouper();
        grouper.setSelectedDate(grouper.currentDate());
        return nextPage;
    }

	/**
	 * @return the dateFormat
	 */
	public SimpleDateFormat dateFormat() {
		if(dateFormat == null) {
			dateFormat = new SimpleDateFormat(format, ERXLocalizer.currentLocalizer().locale());
		}
		return dateFormat;
	}


}