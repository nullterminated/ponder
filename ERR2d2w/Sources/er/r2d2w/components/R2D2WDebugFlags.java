package er.r2d2w.components;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.woextensions.WOStatsPage;

import er.directtoweb.ERD2WModel;
import er.directtoweb.ERDirectToWeb;
import er.extensions.ERXExtensions;
import er.extensions.foundation.ERXProperties;
import er.extensions.statistics.ERXStatisticsPage;

public class R2D2WDebugFlags extends er.extensions.components.ERXComponent {
    public R2D2WDebugFlags(WOContext context) {
        super(context);
    }
    
    public boolean isStateless() {
        return true;
    }

    public WOComponent statisticsPage() {
        WOStatsPage nextPage = (WOStatsPage) pageWithName(ERXStatisticsPage.class.getName());
        nextPage.password = ERXProperties.stringForKey("WOStatisticsPassword");
        return nextPage.submit();
    }
    
    public WOComponent toggleD2WInfo() {
        boolean currentState=ERDirectToWeb.d2wDebuggingEnabled(session());
        ERDirectToWeb.setD2wDebuggingEnabled(session(), !currentState);
        return null;
    }

    public WOComponent toggleAdaptorLogging() {
        boolean currentState=ERXExtensions.adaptorLogging();
        ERXExtensions.setAdaptorLogging(!currentState);
        return null;
    }

    public WOComponent clearD2WRuleCache() {
        ERD2WModel.erDefaultModel().clearD2WRuleCache();
        return null;
    }

}