package er.corebl.components;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXComponent;

public class DefaultUnsubscribePage extends ERXComponent {
	private static final long serialVersionUID = 1L;
	private String errorMessage;

	public DefaultUnsubscribePage(WOContext context) {
        super(context);
    }

	/**
	 * @return the errorMessage
	 */
	public String errorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}