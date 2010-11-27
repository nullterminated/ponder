package er.r2d2w.components;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.woextensions.WOStatsPage;

import er.directtoweb.ERD2WModel;
import er.directtoweb.ERDirectToWeb;
import er.extensions.ERXExtensions;
import er.extensions.components.ERXStatelessComponent;
import er.extensions.foundation.ERXProperties;
import er.extensions.statistics.ERXStatisticsPage;

public class R2D2WDebugFlags extends ERXStatelessComponent {
    public R2D2WDebugFlags(WOContext context) {
        super(context);
    }
    
    public WOComponent statisticsPage() {
        WOStatsPage nextPage = (WOStatsPage) pageWithName(ERXStatisticsPage.class.getName());
        nextPage.password = ERXProperties.stringForKey("WOStatisticsPassword");
        return nextPage.submit();
    }
    
    public void toggleD2WInfo() {
        boolean currentState=ERDirectToWeb.d2wDebuggingEnabled(session());
        ERDirectToWeb.setD2wDebuggingEnabled(session(), !currentState);
    }

    public void toggleAdaptorLogging() {
        boolean currentState=ERXExtensions.adaptorLogging();
        ERXExtensions.setAdaptorLogging(!currentState);
    }

    public void clearD2WRuleCache() {
        ERD2WModel.erDefaultModel().clearD2WRuleCache();
    }

}