package er.r2d2w.pages;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;

import er.directtoweb.pages.ERD2WCalendarPage;
import er.r2d2w.foundation.R2DDateRangeGrouper;

public class R2D2WCalendarPage extends ERD2WCalendarPage {

	/** logging support */
	public final static Logger log = Logger.getLogger(R2D2WCalendarPage.class);
	
	public R2D2WCalendarPage(WOContext context) {
        super(context);
    }
	
	public WODisplayGroup displayGroup() {
		if(_displayGroup == null) {
			R2DDateRangeGrouper grouper = new R2DDateRangeGrouper();
			Object calendarViewPref = d2wContext().valueForKey("calendarView");
			grouper.setCalendarView(
					Enum.valueOf(R2DDateRangeGrouper.CalendarView.class,
					(String)calendarViewPref)
					);
			String dateKey = (String)d2wContext().valueForKey("dateGroupingKey");
			if(dateKey != null) {
				grouper.setDateKeyPath(dateKey);
				String durationKey = (String)d2wContext().valueForKey("durationGroupingKey");
				if(durationKey != null) { grouper.setDurationKeyPath(durationKey); }
			}
			_displayGroup = grouper;
		}
		return _displayGroup;
	}

	public R2DDateRangeGrouper rangeGrouper() {
		return (R2DDateRangeGrouper)displayGroup();
	}

}