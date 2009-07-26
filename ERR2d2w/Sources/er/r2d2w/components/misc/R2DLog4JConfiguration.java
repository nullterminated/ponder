package er.r2d2w.components.misc;

import java.util.Enumeration;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.directtoweb.pages.ERD2WPage;
import er.extensions.ERXExtensions;
import er.extensions.eof.ERXConstant;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.localization.ERXLocalizer;
import er.extensions.logging.ERXPatternLayout;

public class R2DLog4JConfiguration extends ERD2WPage {
	private Logger _logger;
	private String _filterString;
	private String _ruleKey;
	private String _loggerName;
	
	public boolean showAll = false;

	public final static EOSortOrdering NAME_SORT_ORDERING = new EOSortOrdering("name", EOSortOrdering.CompareAscending);
	public final static NSMutableArray<EOSortOrdering> SORT_BY_NAME = new NSMutableArray<EOSortOrdering>(NAME_SORT_ORDERING);

	public R2DLog4JConfiguration(WOContext context) {
        super(context);
    }
    
	public Logger parentForLogger(Logger l) {
		Logger result = (Logger) l.getParent();
		return result;
	}

	public NSArray<Logger> loggers() {
		NSMutableArray<Logger> result = new NSMutableArray<Logger>();
		for (Enumeration<Logger> e = LogManager.getCurrentLoggers(); e.hasMoreElements();) {
			Logger log = e.nextElement();
			while (log != null) {
				addLogger(log, result);
				log = parentForLogger(log);
			}
		}
		EOSortOrdering.sortArrayUsingKeyOrderArray(result, SORT_BY_NAME);
		return result;
	}
	
	public void addLogger(Logger log, NSMutableArray<Logger> result) {
		if ((filterString() == null || filterString().length() == 0 || log.getName().toLowerCase().indexOf(filterString().toLowerCase()) != -1) && (showAll || log.getLevel() != null) && !result.containsObject(log)) {
			result.addObject(log);
		}
	}

	public WOComponent filter() {
		return null;
	}

	public WOComponent resetFilter() {
		_filterString = null;
		return null;
	}

	public WOComponent update() {
		ERXExtensions.configureAdaptorContext();
		return null;
	}

	public WOComponent showAll() {
		showAll = true;
		return null;
	}

	public WOComponent showExplicitlySet() {
		showAll = false;
		return null;
	}

	public WOComponent addLogger() {
		Logger.getLogger(loggerName());
		setFilterString(loggerName());
		return null;
	}

	// This functionality depends on ERDirectToWeb's presence..
	public WOComponent addRuleKey() {
		String prefix = "er.directtoweb.rules." + ruleKey();
		Logger.getLogger(prefix + ".fire");
		Logger.getLogger(prefix + ".cache");
		Logger.getLogger(prefix + ".candidates");
		showAll = true;
		setFilterString(prefix);
		return null;
	}

	public Integer offLevel() {
		return ERXConstant.integerForInt(Level.OFF.toInt());
	}

	public Integer debugLevel() {
		return ERXConstant.integerForInt(Level.DEBUG.toInt());
	}

	public Integer infoLevel() {
		return ERXConstant.integerForInt(Level.INFO.toInt());
	}

	public Integer warnLevel() {
		return ERXConstant.integerForInt(Level.WARN.toInt());
	}

	public Integer errorLevel() {
		return ERXConstant.integerForInt(Level.ERROR.toInt());
	}

	public Integer fatalLevel() {
		return ERXConstant.integerForInt(Level.FATAL.toInt());
	}

	public Integer unsetLevel() {
		return ERXConstant.MinusOneInteger;
	}

	public Integer loggerLevelValue() {
		return logger() != null && logger().getLevel() != null ? ERXConstant.integerForInt(logger().getLevel().toInt()) : ERXConstant.MinusOneInteger;
	}

	public boolean loggerIsNotOff() {
		return logger() != null && logger().getLevel() != Level.OFF;
	}

	public boolean loggerIsNotDebug() {
		return logger() != null && logger().getLevel() != Level.DEBUG;
	}

	public boolean loggerIsNotInfo() {
		return logger() != null && logger().getLevel() != Level.INFO;
	}

	public boolean loggerIsNotWarn() {
		return logger() != null && logger().getLevel() != Level.WARN;
	}

	public boolean loggerIsNotError() {
		return logger() != null && logger().getLevel() != Level.ERROR;
	}

	public boolean loggerIsNotFatal() {
		return logger() != null && logger().getLevel() != Level.FATAL;
	}

	public String loggerPropertiesString() {
		String result = "";
		for (Enumeration<Logger> e = loggers().objectEnumerator(); e.hasMoreElements();) {
			Logger log = e.nextElement();
			String name = log.getName();
			Level level = log.getLevel();
			if (level != null && !"root".equals(name)) {
				result += "log4j.category." + log.getName() + "=" + log.getLevel() + "\n";
			}
		}
		return result;
	}

	public void setLoggerLevelValue(Integer newValue) {
		int lvl = newValue != null ? newValue.intValue() : -1;
		logger().setLevel(lvl != -1 ? Level.toLevel(lvl) : null);
	}
	
	public void appendToResponse(WOResponse r, WOContext c) {
		if (session().objectForKey("ERXLog4JConfiguration.enabled") != null) {
			super.appendToResponse(r, c);
		}
		else {
			r.appendContentString("Please use the ERXDirectAction log4jAction to login first!");
		}
	}

	// * this assumes you use ERXPatternLayout
	public String conversionPattern() {
		return ERXPatternLayout.instance().getConversionPattern();
	}

	public void setConversionPattern(String newPattern) {
		ERXPatternLayout.instance().setConversionPattern(newPattern);
	}

	public WOComponent updateConversionPattern() {
		return null;
	}


	public Logger logger() {
		return _logger;
	}

	public void setLogger(Logger newValue) {
		_logger = newValue;
	}

	public String filterString() {
		return _filterString;
	}

	public void setFilterString(String newValue) {
		_filterString = newValue;
	}

	public String loggerName() {
		return _loggerName;
	}

	public void setLoggerName(String newValue) {
		_loggerName = newValue;
	}

	public String ruleKey() {
		return _ruleKey;
	}

	public void setRuleKey(String newValue) {
		_ruleKey = newValue;
	}


	public String languageCode() {
		return ERXLocalizer.currentLocalizer().languageCode();
	}

	private Integer index;

	/**
	 * @return the index
	 */
	public Integer index() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	public String rowClass() {
		String classes = index() % 2 == 1?"even":"odd";
		String level = (logger().getLevel() == null)?"unset":logger().getLevel().toString().toLowerCase();
		classes = ERXStringUtilities.stringByAppendingCSSClass(classes, level);
		return classes;
	}

}