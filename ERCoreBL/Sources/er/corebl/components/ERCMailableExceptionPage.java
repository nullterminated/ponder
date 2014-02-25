package er.corebl.components;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.foundation.ERXConfigurationManager;
import er.extensions.foundation.ERXUtilities;

public class ERCMailableExceptionPage extends WOComponent {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	public String errorMessage;
    public EOEnterpriseObject actor;
    public Throwable exception;
    public NSArray<String> _reasonLines;
    public String currentReasonLine;
    public String formattedMessage;
    public NSDictionary extraInfo;

    public String currentUserInfoKey;
    public Object currentUserInfoValue;

    public ERCMailableExceptionPage(WOContext context) {
		super(context);
	}

    public boolean isEventLoggingEnabled() {
        return false;
    }

    public void setException(Throwable value) {
        exception = value;
    }

    public void setActor(EOEnterpriseObject value) {
        actor = value;
    }

    public void setExtraInfo(NSDictionary value) {
        extraInfo = value;
    }

    public void setFormattedMessage(String value) {
        formattedMessage = value;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String shortErrorMessage() {
        return exception != null ? exception.getClass().getName() : errorMessage;
    }

    public void setReasonLines(NSArray reasonLines) {
        _reasonLines = reasonLines;
    }

    public NSArray reasonLines() {
        if (_reasonLines==null && exception!=null) {
            _reasonLines = NSArray.componentsSeparatedByString(ERXUtilities.stackTrace(exception), "\n\t");
        }
        return _reasonLines;
    }

    public String hostName() {
		return ERXConfigurationManager.defaultManager().hostName();
	}

    public NSTimestamp now() {
        return new NSTimestamp();
    }
}
