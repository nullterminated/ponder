package er.r2d2w.components.misc;

import java.util.Locale;
import java.util.TimeZone;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOMessage;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.extensions.appserver.ERXSession;
import er.extensions.components.ERXStatelessComponent;

public class R2DTimeZoneMenu extends ERXStatelessComponent {
    public R2DTimeZoneMenu(WOContext context) {
        super(context);
    }

	private TimeZone zone;
	private NSArray<TimeZone> zoneList;
	
	public void reset() {
		zone = null;
		zoneList = null;
	}

	/**
	 * @return the zone
	 */
	public TimeZone zone() {
		return zone;
	}

	/**
	 * @param zone the zone to set
	 */
	public void setZone(TimeZone zone) {
		this.zone = zone;
	}
	
	public String zoneDisplayName() {
		Locale l = ((ERXSession)session()).localizer().locale();
		//TODO use a ERXLocalizer override since Java's defaults kinda suck
		return WOMessage.stringByEscapingHTMLString(zone().getDisplayName(l));
	}

	/**
	 * @return the zoneList
	 */
	public NSArray<TimeZone> zoneList() {
		if(zoneList == null) {
			NSMutableArray<TimeZone> result = new NSMutableArray<TimeZone>();
			NSArray<String> zoneIDs = (NSArray<String>)valueForBinding("list");
			if(zoneIDs == null || zoneIDs.count() == 0) {
				zoneIDs = new NSArray<String>(TimeZone.getAvailableIDs());
			}
			for(String id: zoneIDs) {
				TimeZone tz = TimeZone.getTimeZone(id);
				if(!result.contains(tz)) { result.addObject(tz); }
			}
			zoneList = result.immutableClone();
		}
		return zoneList;
	}
	
}