package er.corebl.components;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXComponent;

public class DefaultUnsubscribePage extends ERXComponent {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
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
