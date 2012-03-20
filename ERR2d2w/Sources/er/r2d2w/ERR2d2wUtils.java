package er.r2d2w;

import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;

public class ERR2d2wUtils {
	private static final String acceptKey = "accept";
	private static final String contentTypeKey = "content-type";
	private static final String xhtmlType = "application/xhtml+xml";

	public static Boolean acceptsXHTML(WORequest request) {
		NSArray<String> acceptTypes = request.headersForKey(acceptKey);
		boolean result = acceptTypes.contains(xhtmlType);
		return Boolean.valueOf(result);
	}
	
	public static void setXHTMLContentType(WOResponse response) {
		response.setHeader(xhtmlType, contentTypeKey);
	}
}
