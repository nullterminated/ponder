package er.r2d2w.components.buttons;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSDictionary;

import er.directtoweb.components.buttons.ERDInspectButton;
import er.r2d2w.R2D2WDirectAction;

public class R2DInspectDirectActionButton extends ERDInspectButton {
    public R2DInspectDirectActionButton(WOContext context) {
        super(context);
    }
    
    public String directActionName() {
    	return "Inspect" + object().entityName();
    }
    
	public NSDictionary<String, Object> queryDictionary() {
		return R2D2WDirectAction.inspectQueryDictionary(object());
    }

}