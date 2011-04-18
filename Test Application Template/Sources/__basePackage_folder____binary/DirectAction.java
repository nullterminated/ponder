package ${basePackage};

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WODirectAction;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;

public class DirectAction extends WODirectAction {
	public DirectAction(WORequest request) {
		super(request);
	}

	public WOActionResults defaultAction() {
		WOResponse r = new WOResponse();
		r.setContent("Test Application");
		return r;
	}
}
