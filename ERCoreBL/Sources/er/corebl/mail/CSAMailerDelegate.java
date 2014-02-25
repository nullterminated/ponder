package er.corebl.mail;

import er.corebl.components.DefaultMessageFooter;
import er.corebl.model.ERCMailClob;
import er.corebl.model.ERCMailMessage;
import er.extensions.appserver.ERXApplication;

/**
 * The default delegate is designed to support compliance with the
 * CAN SPAM Act. It modifies the mail message so that the message
 * complies with regulations.
 */
public abstract class CSAMailerDelegate {
	/**
	 * The CAN SPAM act requires this exact prefix on the email subject
	 * line if an email message is sexually explicit.
	 */
	public static final String SEXUALLY_EXPLICIT_PREFIX = "SEXUALLY-EXPLICIT: ";
	
	/**
	 * CAN SPAM requires that you must disclose clearly and conspicuously
	 * that your message is an advertisement. The law provides a lot of
	 * leeway in how to do this. Here we just note it in the subject line.
	 */
	public static final String COMMERCIAL_PREFIX = "[AD] ";
	
	/**
	 * Append information required in the subject line and body of a
	 * message for CAN SPAM compliance. If the {@link ERCMailMessage#dateSent()}
	 * of the message is not null, then it is assumed the message has
	 * been processed already and nothing further is appended to it.
	 *
	 * @param mailMessage the mail message to process
	 */
	public void appendRequiredDisclaimers(ERCMailMessage mailMessage) {
		if(mailMessage.dateSent() != null) {
			/*
			 * If a message has been sent previously, assumed it has
			 * been processed for CAN SPAM compliance also.
			 */
			return;
		}
		String subject = mailMessage.subject();
		if(isCommercial(mailMessage)) {
			subject = COMMERCIAL_PREFIX + subject;
		}
		if(isSexuallyExplicit(mailMessage)) {
			subject = SEXUALLY_EXPLICIT_PREFIX + subject;
		}
		if(!subject.equals(mailMessage.subject())) {
			mailMessage.setSubject(subject);
		}
		
		DefaultMessageFooter footer = (DefaultMessageFooter) ERXApplication.instantiatePage(footerComponentClass().getName());
		footer.setMailMessage(mailMessage);
		setPostalAddress(mailMessage, footer);
		
		String plainText = ERCMailMessage.PLAIN_CLOB.dot(ERCMailClob.MESSAGE).valueInObject(mailMessage);
		if(plainText != null) {
			footer.setPlainText(true);
			String footerText = ERCMailMessage.clazz.componentContentWithFullURLs(footer);
			plainText += footerText;
			ERCMailMessage.PLAIN_CLOB.dot(ERCMailClob.MESSAGE).takeValueInObject(plainText, mailMessage);
		}
		
		String htmlText = ERCMailMessage.HTML_CLOB.dot(ERCMailClob.MESSAGE).valueInObject(mailMessage);
		if(htmlText != null) {
			footer.setPlainText(false);
			String footerHTML = ERCMailMessage.clazz.componentContentWithFullURLs(footer);
			int idx = htmlText.indexOf("</body>");
			if(idx == -1) { idx = htmlText.indexOf("</html>"); }
			if(idx == -1) {
				htmlText += footerHTML;
			} else {
				StringBuilder sb = new StringBuilder(htmlText);
				sb.insert(idx, footerHTML);
				htmlText = sb.toString();
			}
			ERCMailMessage.HTML_CLOB.dot(ERCMailClob.MESSAGE).takeValueInObject(htmlText, mailMessage);
		}
		mailMessage.editingContext().saveChanges();
	}
	
	/**
	 * @param mailMessage the mail message
	 * @return true if a the mailMessage is commercial, false otherwise
	 */
	protected abstract boolean isCommercial(ERCMailMessage mailMessage);
	
	/**
	 * @param mailMessage the mail message
	 * @return true if a the mailMessage is sexually explicit, false otherwise
	 */
	protected abstract boolean isSexuallyExplicit(ERCMailMessage mailMessage);
	
	/**
	 * Provide a subclass of the default class used for the footer.
	 *
	 * @return class for the footer component
	 */
	protected abstract Class<? extends DefaultMessageFooter> footerComponentClass();

	/**
	 * Set the postal address of the sender on the footer component.
	 *
	 * @param footer the footer component
	 */
	protected abstract void setPostalAddress(ERCMailMessage mailMessage, DefaultMessageFooter footer);
}
