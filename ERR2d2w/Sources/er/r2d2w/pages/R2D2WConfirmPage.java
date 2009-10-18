package er.r2d2w.pages;

import com.webobjects.appserver.WOContext;

/** 
 * Unless you need the special shouldProvideConfirmMessage, one should use R2D2WMessagePage instead 
 */
public class R2D2WConfirmPage extends R2D2WMessagePage {
    public R2D2WConfirmPage(WOContext context) {
        super(context);
    }
}