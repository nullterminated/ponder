package er.corebl.components;

import com.webobjects.appserver.WOContext;

import er.corebl.model.ERCMailMessage;
import er.extensions.components.ERXStatelessComponent;

public class DefaultMessageFooter extends ERXStatelessComponent {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	private boolean isPlainText;
	private ERCMailMessage mailMessage;
	private String streetAddress;
	private String city;
	private String state;
	private String zipCode;
	private String sender;

	public DefaultMessageFooter(WOContext context) {
        super(context);
    }
	
	public void reset() {
		isPlainText = false;
		mailMessage = null;
		streetAddress = null;
		city = null;
		state = null;
		zipCode = null;
		sender = null;
	}

	/**
	 * @return the isPlainText
	 */
	public boolean isPlainText() {
		return isPlainText;
	}

	/**
	 * @param isPlainText the isPlainText to set
	 */
	public void setPlainText(boolean isPlainText) {
		this.isPlainText = isPlainText;
	}

	/**
	 * @return the mailMessage
	 */
	public ERCMailMessage mailMessage() {
		return mailMessage;
	}

	/**
	 * @param mailMessage the mailMessage to set
	 */
	public void setMailMessage(ERCMailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}

	/**
	 * @return the streetAddress
	 */
	public String streetAddress() {
		return streetAddress;
	}

	/**
	 * @param streetAddress the streetAddress to set
	 */
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	/**
	 * @return the city
	 */
	public String city() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String state() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zipCode
	 */
	public String zipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * @return the sender
	 */
	public String sender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
}
