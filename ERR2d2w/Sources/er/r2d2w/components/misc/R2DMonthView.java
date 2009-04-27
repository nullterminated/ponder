package er.r2d2w.components.misc;

import java.text.SimpleDateFormat;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;

import er.directtoweb.components.ERD2WStatelessComponent;
import er.extensions.foundation.ERXStringUtilities;
import er.r2d2w.foundation.R2DDateRangeGrouper;

public class R2DMonthView extends ERD2WStatelessComponent {
	
	//FIXME SimpleDateFormat is not reentrant.
	public static final SimpleDateFormat DEFAULT_WEEKDAY_FORMAT = new SimpleDateFormat("EEEE");
	public static final SimpleDateFormat DEFAULT_DAY_FORMAT = new SimpleDateFormat("d");
	
    public R2DMonthView(WOContext context) {
        super(context);
    }
    
    public R2DDateRangeGrouper rangeGrouper() {
    	return (R2DDateRangeGrouper)valueForBinding("rangeGrouper");
    }
    
    //TODO highlight the selected date
    public WOActionResults selectDateAction() {
        Object action = valueForBinding("action");
        WOActionResults nextPage = context().page();
        if(action == null) {
        	R2DDateRangeGrouper grouper = rangeGrouper();
            grouper.setSelectedDate(grouper.currentDate());
        } else {
            nextPage = performParentAction(action.toString());
        }
        return nextPage;
    }

	public String dayName() {
		// TODO  This should return something localized and rule system based
		return DEFAULT_WEEKDAY_FORMAT.format(rangeGrouper().currentDate());
	}
	
	public String dayString() {
		// TODO  This should return something localized and rule system based
		return DEFAULT_DAY_FORMAT.format(rangeGrouper().currentDate());
	}

	public String styleClasses() {
		String result = null;
		R2DDateRangeGrouper grouper = rangeGrouper();
		if(grouper.isToday()) { result = ERXStringUtilities.stringByAppendingCSSClass(result, "today"); }
		if(grouper.isSelectedDate()) { result = ERXStringUtilities.stringByAppendingCSSClass(result, "selected"); }
		if(!grouper.isInMonth()) { result = ERXStringUtilities.stringByAppendingCSSClass(result, "out"); }
		return result;
	}
	
}