package er.r2d2w.pages;

import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.appserver.WOResponse;

import er.directtoweb.pages.ERD2WCalendarPage;
import er.extensions.appserver.ERXSession;
import er.r2d2w.foundation.R2DDateRangeGrouper;

public class R2D2WCalendarPage extends ERD2WCalendarPage {

	/** logging support */
	public final static Logger log = Logger.getLogger(R2D2WCalendarPage.class);
	
	public R2D2WCalendarPage(WOContext context) {
        super(context);
    }
	
	public void appendToResponse(WOResponse r, WOContext c) {
		if(ERXSession.class.isAssignableFrom(session().getClass())) {
			TimeZone tz = ((ERXSession)session()).timeZone();
			if(!rangeGrouper().currentTimeZone().equals(tz)) {
				log.debug("Updating to new time zone: " + tz.getID());
				rangeGrouper().setCurrentTimeZone(tz);
			}
		}
		super.appendToResponse(r, c);
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
			if(ERXSession.class.isAssignableFrom(session().getClass())) {
				grouper.setCurrentTimeZone(((ERXSession)session()).timeZone());
			}
			_displayGroup = grouper;
		}
		return _displayGroup;
	}

	public R2DDateRangeGrouper rangeGrouper() {
		return (R2DDateRangeGrouper)displayGroup();
	}
	
}