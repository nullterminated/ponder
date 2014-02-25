package er.corebl.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSForwardException;

import er.attachment.model.ERAttachment;
import er.attachment.model.ERDatabaseAttachment;
import er.attachment.model.ERFileAttachment;
import er.attachment.processors.ERAttachmentProcessor;
import er.extensions.foundation.ERXFileUtilities;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;
import er.javamail.ERMailAttachment;
import er.javamail.ERMailDataAttachment;
import er.javamail.ERMailFileAttachment;

public class ERCMailAttachment extends er.corebl.model.eogen._ERCMailAttachment {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERCMailAttachment.class);

	public static final ERCMailAttachmentClazz<ERCMailAttachment> clazz = new ERCMailAttachmentClazz<ERCMailAttachment>();

	public static class ERCMailAttachmentClazz<T extends ERCMailAttachment> extends
			er.corebl.model.eogen._ERCMailAttachment._ERCMailAttachmentClazz<T> {
		/* more clazz methods here */
	}

	/**
	 * Initialize the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
		setToken(UUID.randomUUID().toString());
		setIsInline(Boolean.FALSE);
	}

	public String cidURL() {
		return "cid:" + token();
	}
	
	public ERMailAttachment mailAttachment() {
		ERMailAttachment result = null;
		String token = "<" + token() + ">";
		if (filePath() != null) {
			String fileName = ERXFileUtilities.fileNameFromBrowserSubmittedPath(filePath());
			File content = new File(filePath());
			result = new ERMailFileAttachment(fileName, token, content);
		} else {
			ERAttachment attachment = attachment();
			String fileName = attachment.fileName();
			if (attachment instanceof ERFileAttachment) {
				ERFileAttachment fileAttachment = (ERFileAttachment) attachment;
				File content = new File(fileAttachment.filesystemPath());
				result = new ERMailFileAttachment(fileName, token, content);
			} else if (attachment instanceof ERDatabaseAttachment) {
				ERDatabaseAttachment databaseAttachment = (ERDatabaseAttachment) attachment;
				NSData content = databaseAttachment.attachmentData().data();
				result = new ERMailDataAttachment(fileName, token, content);
			} else {
				ERAttachmentProcessor<ERAttachment> processor = ERAttachmentProcessor.processorForType(attachment);
				try {
					InputStream stream = processor.attachmentInputStream(attachment);
					NSData content = new NSData(stream, 4096);
					result = new ERMailDataAttachment(fileName, token, content);
				} catch (IOException e) {
					throw NSForwardException._runtimeExceptionForThrowable(e);
				}
			}
		}
		return result;
	}

	public void validateForSave() {
		ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
		if (StringUtils.isBlank(filePath()) && attachment() == null) {
			ERXValidationException ex = factory.createCustomException(this, "MissingAttachmentOrFilePath");
			throw ex;
		} else if (StringUtils.isNotBlank(filePath()) && attachment() != null) {
			ERXValidationException ex = factory.createCustomException(this, "HasAttachmentAndFilePath");
			throw ex;
		}
	}
}
