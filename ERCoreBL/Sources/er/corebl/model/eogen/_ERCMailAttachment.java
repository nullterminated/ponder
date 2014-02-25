// DO NOT EDIT.  Make changes to er.corebl.model.ERCMailAttachment.java instead.
package er.corebl.model.eogen;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;


@SuppressWarnings("all")
public abstract class _ERCMailAttachment extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERCMailAttachment";

  // Attributes
  public static final ERXKey<String> FILE_PATH = new ERXKey<String>("filePath");
  public static final String FILE_PATH_KEY = FILE_PATH.key();
  public static final ERXKey<Boolean> IS_INLINE = new ERXKey<Boolean>("isInline");
  public static final String IS_INLINE_KEY = IS_INLINE.key();
  public static final ERXKey<String> TOKEN = new ERXKey<String>("token");
  public static final String TOKEN_KEY = TOKEN.key();

  // Relationships
  public static final ERXKey<er.attachment.model.ERAttachment> ATTACHMENT = new ERXKey<er.attachment.model.ERAttachment>("attachment");
  public static final String ATTACHMENT_KEY = ATTACHMENT.key();
  public static final ERXKey<er.corebl.model.ERCMailMessage> MAIL_MESSAGE = new ERXKey<er.corebl.model.ERCMailMessage>("mailMessage");
  public static final String MAIL_MESSAGE_KEY = MAIL_MESSAGE.key();

  public static class _ERCMailAttachmentClazz<T extends er.corebl.model.ERCMailAttachment> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERCMailAttachment.class);

  public er.corebl.model.ERCMailAttachment.ERCMailAttachmentClazz clazz() {
    return er.corebl.model.ERCMailAttachment.clazz;
  }

  public String filePath() {
    return (String) storedValueForKey(_ERCMailAttachment.FILE_PATH_KEY);
  }

  public void setFilePath(String value) {
    if (_ERCMailAttachment.LOG.isDebugEnabled()) {
    	_ERCMailAttachment.LOG.debug( "updating filePath from " + filePath() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailAttachment.FILE_PATH_KEY);
  }

  public Boolean isInline() {
    return (Boolean) storedValueForKey(_ERCMailAttachment.IS_INLINE_KEY);
  }

  public void setIsInline(Boolean value) {
    if (_ERCMailAttachment.LOG.isDebugEnabled()) {
    	_ERCMailAttachment.LOG.debug( "updating isInline from " + isInline() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailAttachment.IS_INLINE_KEY);
  }

  public String token() {
    return (String) storedValueForKey(_ERCMailAttachment.TOKEN_KEY);
  }

  public void setToken(String value) {
    if (_ERCMailAttachment.LOG.isDebugEnabled()) {
    	_ERCMailAttachment.LOG.debug( "updating token from " + token() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailAttachment.TOKEN_KEY);
  }

  public er.attachment.model.ERAttachment attachment() {
    return (er.attachment.model.ERAttachment)storedValueForKey(_ERCMailAttachment.ATTACHMENT_KEY);
  }

  public void setAttachment(er.attachment.model.ERAttachment value) {
    takeStoredValueForKey(value, _ERCMailAttachment.ATTACHMENT_KEY);
  }

  public void setAttachmentRelationship(er.attachment.model.ERAttachment value) {
    if (_ERCMailAttachment.LOG.isDebugEnabled()) {
      _ERCMailAttachment.LOG.debug("updating attachment from " + attachment() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setAttachment(value);
    }
    else if (value == null) {
    	er.attachment.model.ERAttachment oldValue = attachment();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCMailAttachment.ATTACHMENT_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCMailAttachment.ATTACHMENT_KEY);
    }
  }
  public er.corebl.model.ERCMailMessage mailMessage() {
    return (er.corebl.model.ERCMailMessage)storedValueForKey(_ERCMailAttachment.MAIL_MESSAGE_KEY);
  }

  public void setMailMessage(er.corebl.model.ERCMailMessage value) {
    takeStoredValueForKey(value, _ERCMailAttachment.MAIL_MESSAGE_KEY);
  }

  public void setMailMessageRelationship(er.corebl.model.ERCMailMessage value) {
    if (_ERCMailAttachment.LOG.isDebugEnabled()) {
      _ERCMailAttachment.LOG.debug("updating mailMessage from " + mailMessage() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setMailMessage(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCMailMessage oldValue = mailMessage();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCMailAttachment.MAIL_MESSAGE_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCMailAttachment.MAIL_MESSAGE_KEY);
    }
  }

}
