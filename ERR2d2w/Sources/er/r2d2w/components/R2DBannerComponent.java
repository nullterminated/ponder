package er.r2d2w.components;

import com.webobjects.appserver.WOContext;

import er.directtoweb.components.ERDBannerComponent;

public class R2DBannerComponent extends ERDBannerComponent {
    public R2DBannerComponent(WOContext context) {
        super(context);
    }

	public String templateString() {
		StringBuilder sb = new StringBuilder("ERD2W.tasks.").append(d2wContext().task().toString());
		Object subtask = d2wContext().valueForKey("subTask");
		if(subtask!=null) {
			sb.append(".").append(subtask.toString());
		}
		return sb.toString();
	}
}