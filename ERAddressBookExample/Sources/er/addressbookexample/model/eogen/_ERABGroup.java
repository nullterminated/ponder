// DO NOT EDIT.  Make changes to er.addressbookexample.model.ERABGroup.java instead.
package er.addressbookexample.model.eogen;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;


@SuppressWarnings("all")
public abstract class _ERABGroup extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERABGroup";

  // Attributes

  // Relationships
  public static final ERXKey<er.addressbookexample.model.ERABContact> CONTACTS = new ERXKey<er.addressbookexample.model.ERABContact>("contacts");
  public static final String CONTACTS_KEY = CONTACTS.key();

  public static class _ERABGroupClazz<T extends er.addressbookexample.model.ERABGroup> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERABGroup.class);

  public er.addressbookexample.model.ERABGroup.ERABGroupClazz clazz() {
    return er.addressbookexample.model.ERABGroup.clazz;
  }
  
  public NSArray<er.addressbookexample.model.ERABContact> contacts() {
    return (NSArray<er.addressbookexample.model.ERABContact>)storedValueForKey(_ERABGroup.CONTACTS_KEY);
  }

  public NSArray<er.addressbookexample.model.ERABContact> contacts(EOQualifier qualifier) {
    return contacts(qualifier, null);
  }

  public NSArray<er.addressbookexample.model.ERABContact> contacts(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    NSArray<er.addressbookexample.model.ERABContact> results;
      results = contacts();
      if (qualifier != null) {
        results = (NSArray<er.addressbookexample.model.ERABContact>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<er.addressbookexample.model.ERABContact>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToContacts(er.addressbookexample.model.ERABContact object) {
    includeObjectIntoPropertyWithKey(object, _ERABGroup.CONTACTS_KEY);
  }

  public void removeFromContacts(er.addressbookexample.model.ERABContact object) {
    excludeObjectFromPropertyWithKey(object, _ERABGroup.CONTACTS_KEY);
  }

  public void addToContactsRelationship(er.addressbookexample.model.ERABContact object) {
    if (_ERABGroup.LOG.isDebugEnabled()) {
      _ERABGroup.LOG.debug("adding " + object + " to contacts relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToContacts(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _ERABGroup.CONTACTS_KEY);
    }
  }

  public void removeFromContactsRelationship(er.addressbookexample.model.ERABContact object) {
    if (_ERABGroup.LOG.isDebugEnabled()) {
      _ERABGroup.LOG.debug("removing " + object + " from contacts relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromContacts(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _ERABGroup.CONTACTS_KEY);
    }
  }

  public er.addressbookexample.model.ERABContact createContactsRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( er.addressbookexample.model.ERABContact.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _ERABGroup.CONTACTS_KEY);
    return (er.addressbookexample.model.ERABContact) eo;
  }

  public void deleteContactsRelationship(er.addressbookexample.model.ERABContact object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _ERABGroup.CONTACTS_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllContactsRelationships() {
    Enumeration<er.addressbookexample.model.ERABContact> objects = contacts().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteContactsRelationship(objects.nextElement());
    }
  }


}
