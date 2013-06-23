// DO NOT EDIT.  Make changes to er.corebl.model.ERCMailMessage.java instead.
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
public abstract class _ERCMailMessage extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERCMailMessage";

  // Attributes
  public static final ERXKey<NSTimestamp> DATE_READ = new ERXKey<NSTimestamp>("dateRead");
  public static final String DATE_READ_KEY = DATE_READ.key();
  public static final ERXKey<NSTimestamp> DATE_SENT = new ERXKey<NSTimestamp>("dateSent");
  public static final String DATE_SENT_KEY = DATE_SENT.key();
  public static final ERXKey<String> EXCEPTION_REASON = new ERXKey<String>("exceptionReason");
  public static final String EXCEPTION_REASON_KEY = EXCEPTION_REASON.key();
  public static final ERXKey<String> MESSAGE_ID = new ERXKey<String>("messageID");
  public static final String MESSAGE_ID_KEY = MESSAGE_ID.key();
  public static final ERXKey<er.corebl.mail.ERCMailState> STATE = new ERXKey<er.corebl.mail.ERCMailState>("state");
  public static final String STATE_KEY = STATE.key();
  public static final ERXKey<String> SUBJECT = new ERXKey<String>("subject");
  public static final String SUBJECT_KEY = SUBJECT.key();
  public static final ERXKey<String> UUID = new ERXKey<String>("uuid");
  public static final String UUID_KEY = UUID.key();
  public static final ERXKey<String> X_MAILER = new ERXKey<String>("xMailer");
  public static final String X_MAILER_KEY = X_MAILER.key();

  // Relationships
  public static final ERXKey<er.corebl.model.ERCMailAddress> FROM_ADDRESS = new ERXKey<er.corebl.model.ERCMailAddress>("fromAddress");
  public static final String FROM_ADDRESS_KEY = FROM_ADDRESS.key();
  public static final ERXKey<er.corebl.model.ERCMailClob> HTML_CLOB = new ERXKey<er.corebl.model.ERCMailClob>("htmlClob");
  public static final String HTML_CLOB_KEY = HTML_CLOB.key();
  public static final ERXKey<er.corebl.model.ERCMailAttachment> MAIL_ATTACHMENTS = new ERXKey<er.corebl.model.ERCMailAttachment>("mailAttachments");
  public static final String MAIL_ATTACHMENTS_KEY = MAIL_ATTACHMENTS.key();
  public static final ERXKey<er.corebl.model.ERCMailCategory> MAIL_CATEGORY = new ERXKey<er.corebl.model.ERCMailCategory>("mailCategory");
  public static final String MAIL_CATEGORY_KEY = MAIL_CATEGORY.key();
  public static final ERXKey<er.corebl.model.ERCMailRecipient> MAIL_RECIPIENTS = new ERXKey<er.corebl.model.ERCMailRecipient>("mailRecipients");
  public static final String MAIL_RECIPIENTS_KEY = MAIL_RECIPIENTS.key();
  public static final ERXKey<er.corebl.model.ERCMailClob> PLAIN_CLOB = new ERXKey<er.corebl.model.ERCMailClob>("plainClob");
  public static final String PLAIN_CLOB_KEY = PLAIN_CLOB.key();
  public static final ERXKey<er.corebl.model.ERCMailAddress> REPLY_TO_ADDRESS = new ERXKey<er.corebl.model.ERCMailAddress>("replyToAddress");
  public static final String REPLY_TO_ADDRESS_KEY = REPLY_TO_ADDRESS.key();

  public static class _ERCMailMessageClazz<T extends er.corebl.model.ERCMailMessage> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERCMailMessage.class);

  public er.corebl.model.ERCMailMessage.ERCMailMessageClazz clazz() {
    return er.corebl.model.ERCMailMessage.clazz;
  }

  public NSTimestamp dateRead() {
    return (NSTimestamp) storedValueForKey(_ERCMailMessage.DATE_READ_KEY);
  }

  public void setDateRead(NSTimestamp value) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
    	_ERCMailMessage.LOG.debug( "updating dateRead from " + dateRead() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailMessage.DATE_READ_KEY);
  }

  public NSTimestamp dateSent() {
    return (NSTimestamp) storedValueForKey(_ERCMailMessage.DATE_SENT_KEY);
  }

  public void setDateSent(NSTimestamp value) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
    	_ERCMailMessage.LOG.debug( "updating dateSent from " + dateSent() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailMessage.DATE_SENT_KEY);
  }

  public String exceptionReason() {
    return (String) storedValueForKey(_ERCMailMessage.EXCEPTION_REASON_KEY);
  }

  public void setExceptionReason(String value) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
    	_ERCMailMessage.LOG.debug( "updating exceptionReason from " + exceptionReason() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailMessage.EXCEPTION_REASON_KEY);
  }

  public String messageID() {
    return (String) storedValueForKey(_ERCMailMessage.MESSAGE_ID_KEY);
  }

  public void setMessageID(String value) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
    	_ERCMailMessage.LOG.debug( "updating messageID from " + messageID() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailMessage.MESSAGE_ID_KEY);
  }

  public er.corebl.mail.ERCMailState state() {
    return (er.corebl.mail.ERCMailState) storedValueForKey(_ERCMailMessage.STATE_KEY);
  }

  public void setState(er.corebl.mail.ERCMailState value) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
    	_ERCMailMessage.LOG.debug( "updating state from " + state() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailMessage.STATE_KEY);
  }

  public String subject() {
    return (String) storedValueForKey(_ERCMailMessage.SUBJECT_KEY);
  }

  public void setSubject(String value) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
    	_ERCMailMessage.LOG.debug( "updating subject from " + subject() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailMessage.SUBJECT_KEY);
  }

  public String uuid() {
    return (String) storedValueForKey(_ERCMailMessage.UUID_KEY);
  }

  public void setUuid(String value) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
    	_ERCMailMessage.LOG.debug( "updating uuid from " + uuid() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailMessage.UUID_KEY);
  }

  public String xMailer() {
    return (String) storedValueForKey(_ERCMailMessage.X_MAILER_KEY);
  }

  public void setXMailer(String value) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
    	_ERCMailMessage.LOG.debug( "updating xMailer from " + xMailer() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailMessage.X_MAILER_KEY);
  }

  public er.corebl.model.ERCMailAddress fromAddress() {
    return (er.corebl.model.ERCMailAddress)storedValueForKey(_ERCMailMessage.FROM_ADDRESS_KEY);
  }

  public void setFromAddress(er.corebl.model.ERCMailAddress value) {
    takeStoredValueForKey(value, _ERCMailMessage.FROM_ADDRESS_KEY);
  }

  public void setFromAddressRelationship(er.corebl.model.ERCMailAddress value) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
      _ERCMailMessage.LOG.debug("updating fromAddress from " + fromAddress() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setFromAddress(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCMailAddress oldValue = fromAddress();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCMailMessage.FROM_ADDRESS_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCMailMessage.FROM_ADDRESS_KEY);
    }
  }
  public er.corebl.model.ERCMailClob htmlClob() {
    return (er.corebl.model.ERCMailClob)storedValueForKey(_ERCMailMessage.HTML_CLOB_KEY);
  }

  public void setHtmlClob(er.corebl.model.ERCMailClob value) {
    takeStoredValueForKey(value, _ERCMailMessage.HTML_CLOB_KEY);
  }

  public void setHtmlClobRelationship(er.corebl.model.ERCMailClob value) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
      _ERCMailMessage.LOG.debug("updating htmlClob from " + htmlClob() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setHtmlClob(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCMailClob oldValue = htmlClob();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCMailMessage.HTML_CLOB_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCMailMessage.HTML_CLOB_KEY);
    }
  }
  public er.corebl.model.ERCMailCategory mailCategory() {
    return (er.corebl.model.ERCMailCategory)storedValueForKey(_ERCMailMessage.MAIL_CATEGORY_KEY);
  }

  public void setMailCategory(er.corebl.model.ERCMailCategory value) {
    takeStoredValueForKey(value, _ERCMailMessage.MAIL_CATEGORY_KEY);
  }

  public void setMailCategoryRelationship(er.corebl.model.ERCMailCategory value) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
      _ERCMailMessage.LOG.debug("updating mailCategory from " + mailCategory() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setMailCategory(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCMailCategory oldValue = mailCategory();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCMailMessage.MAIL_CATEGORY_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCMailMessage.MAIL_CATEGORY_KEY);
    }
  }
  public er.corebl.model.ERCMailClob plainClob() {
    return (er.corebl.model.ERCMailClob)storedValueForKey(_ERCMailMessage.PLAIN_CLOB_KEY);
  }

  public void setPlainClob(er.corebl.model.ERCMailClob value) {
    takeStoredValueForKey(value, _ERCMailMessage.PLAIN_CLOB_KEY);
  }

  public void setPlainClobRelationship(er.corebl.model.ERCMailClob value) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
      _ERCMailMessage.LOG.debug("updating plainClob from " + plainClob() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setPlainClob(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCMailClob oldValue = plainClob();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCMailMessage.PLAIN_CLOB_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCMailMessage.PLAIN_CLOB_KEY);
    }
  }
  public er.corebl.model.ERCMailAddress replyToAddress() {
    return (er.corebl.model.ERCMailAddress)storedValueForKey(_ERCMailMessage.REPLY_TO_ADDRESS_KEY);
  }

  public void setReplyToAddress(er.corebl.model.ERCMailAddress value) {
    takeStoredValueForKey(value, _ERCMailMessage.REPLY_TO_ADDRESS_KEY);
  }

  public void setReplyToAddressRelationship(er.corebl.model.ERCMailAddress value) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
      _ERCMailMessage.LOG.debug("updating replyToAddress from " + replyToAddress() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setReplyToAddress(value);
    }
    else if (value == null) {
    	er.corebl.model.ERCMailAddress oldValue = replyToAddress();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERCMailMessage.REPLY_TO_ADDRESS_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERCMailMessage.REPLY_TO_ADDRESS_KEY);
    }
  }
  public NSArray<er.corebl.model.ERCMailAttachment> mailAttachments() {
    return (NSArray<er.corebl.model.ERCMailAttachment>)storedValueForKey(_ERCMailMessage.MAIL_ATTACHMENTS_KEY);
  }

  public NSArray<er.corebl.model.ERCMailAttachment> mailAttachments(EOQualifier qualifier) {
    return mailAttachments(qualifier, null, false);
  }

  public NSArray<er.corebl.model.ERCMailAttachment> mailAttachments(EOQualifier qualifier, boolean fetch) {
    return mailAttachments(qualifier, null, fetch);
  }

  public NSArray<er.corebl.model.ERCMailAttachment> mailAttachments(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings, boolean fetch) {
    NSArray<er.corebl.model.ERCMailAttachment> results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(er.corebl.model.ERCMailAttachment.MAIL_MESSAGE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray<EOQualifier> qualifiers = new NSMutableArray<EOQualifier>();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = er.corebl.model.ERCMailAttachment.clazz.objectsMatchingQualifier(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = mailAttachments();
      if (qualifier != null) {
        results = (NSArray<er.corebl.model.ERCMailAttachment>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<er.corebl.model.ERCMailAttachment>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }

  public void addToMailAttachments(er.corebl.model.ERCMailAttachment object) {
    includeObjectIntoPropertyWithKey(object, _ERCMailMessage.MAIL_ATTACHMENTS_KEY);
  }

  public void removeFromMailAttachments(er.corebl.model.ERCMailAttachment object) {
    excludeObjectFromPropertyWithKey(object, _ERCMailMessage.MAIL_ATTACHMENTS_KEY);
  }

  public void addToMailAttachmentsRelationship(er.corebl.model.ERCMailAttachment object) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
      _ERCMailMessage.LOG.debug("adding " + object + " to mailAttachments relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToMailAttachments(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _ERCMailMessage.MAIL_ATTACHMENTS_KEY);
    }
  }

  public void removeFromMailAttachmentsRelationship(er.corebl.model.ERCMailAttachment object) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
      _ERCMailMessage.LOG.debug("removing " + object + " from mailAttachments relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromMailAttachments(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _ERCMailMessage.MAIL_ATTACHMENTS_KEY);
    }
  }

  public er.corebl.model.ERCMailAttachment createMailAttachmentsRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( er.corebl.model.ERCMailAttachment.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _ERCMailMessage.MAIL_ATTACHMENTS_KEY);
    return (er.corebl.model.ERCMailAttachment) eo;
  }

  public void deleteMailAttachmentsRelationship(er.corebl.model.ERCMailAttachment object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _ERCMailMessage.MAIL_ATTACHMENTS_KEY);
  }

  public void deleteAllMailAttachmentsRelationships() {
    Enumeration<er.corebl.model.ERCMailAttachment> objects = mailAttachments().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteMailAttachmentsRelationship(objects.nextElement());
    }
  }

  public NSArray<er.corebl.model.ERCMailRecipient> mailRecipients() {
    return (NSArray<er.corebl.model.ERCMailRecipient>)storedValueForKey(_ERCMailMessage.MAIL_RECIPIENTS_KEY);
  }

  public NSArray<er.corebl.model.ERCMailRecipient> mailRecipients(EOQualifier qualifier) {
    return mailRecipients(qualifier, null, false);
  }

  public NSArray<er.corebl.model.ERCMailRecipient> mailRecipients(EOQualifier qualifier, boolean fetch) {
    return mailRecipients(qualifier, null, fetch);
  }

  public NSArray<er.corebl.model.ERCMailRecipient> mailRecipients(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings, boolean fetch) {
    NSArray<er.corebl.model.ERCMailRecipient> results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(er.corebl.model.ERCMailRecipient.MAIL_MESSAGE_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray<EOQualifier> qualifiers = new NSMutableArray<EOQualifier>();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = er.corebl.model.ERCMailRecipient.clazz.objectsMatchingQualifier(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = mailRecipients();
      if (qualifier != null) {
        results = (NSArray<er.corebl.model.ERCMailRecipient>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<er.corebl.model.ERCMailRecipient>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }

  public void addToMailRecipients(er.corebl.model.ERCMailRecipient object) {
    includeObjectIntoPropertyWithKey(object, _ERCMailMessage.MAIL_RECIPIENTS_KEY);
  }

  public void removeFromMailRecipients(er.corebl.model.ERCMailRecipient object) {
    excludeObjectFromPropertyWithKey(object, _ERCMailMessage.MAIL_RECIPIENTS_KEY);
  }

  public void addToMailRecipientsRelationship(er.corebl.model.ERCMailRecipient object) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
      _ERCMailMessage.LOG.debug("adding " + object + " to mailRecipients relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToMailRecipients(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _ERCMailMessage.MAIL_RECIPIENTS_KEY);
    }
  }

  public void removeFromMailRecipientsRelationship(er.corebl.model.ERCMailRecipient object) {
    if (_ERCMailMessage.LOG.isDebugEnabled()) {
      _ERCMailMessage.LOG.debug("removing " + object + " from mailRecipients relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromMailRecipients(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _ERCMailMessage.MAIL_RECIPIENTS_KEY);
    }
  }

  public er.corebl.model.ERCMailRecipient createMailRecipientsRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( er.corebl.model.ERCMailRecipient.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _ERCMailMessage.MAIL_RECIPIENTS_KEY);
    return (er.corebl.model.ERCMailRecipient) eo;
  }

  public void deleteMailRecipientsRelationship(er.corebl.model.ERCMailRecipient object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _ERCMailMessage.MAIL_RECIPIENTS_KEY);
  }

  public void deleteAllMailRecipientsRelationships() {
    Enumeration<er.corebl.model.ERCMailRecipient> objects = mailRecipients().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteMailRecipientsRelationship(objects.nextElement());
    }
  }


}
