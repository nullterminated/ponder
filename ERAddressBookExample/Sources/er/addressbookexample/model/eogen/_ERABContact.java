// DO NOT EDIT.  Make changes to er.addressbookexample.model.ERABContact.java instead.
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
public abstract class _ERABContact extends er.datum.model.ERDatumObject {
  public static final String ENTITY_NAME = "ERABContact";

  // Attributes
  public static final ERXKey<String> SUBTYPE = new ERXKey<String>("subtype");
  public static final String SUBTYPE_KEY = SUBTYPE.key();

  // Relationships
  public static final ERXKey<er.addressbookexample.model.ERABAddress> ADDRESSES = new ERXKey<er.addressbookexample.model.ERABAddress>("addresses");
  public static final String ADDRESSES_KEY = ADDRESSES.key();
  public static final ERXKey<er.addressbookexample.model.ERABGroup> GROUPS = new ERXKey<er.addressbookexample.model.ERABGroup>("groups");
  public static final String GROUPS_KEY = GROUPS.key();
  public static final ERXKey<er.datum.model.ERDatum> OBJECT_DATA = new ERXKey<er.datum.model.ERDatum>("objectData");
  public static final String OBJECT_DATA_KEY = OBJECT_DATA.key();

  public static class _ERABContactClazz<T extends er.addressbookexample.model.ERABContact> extends er.datum.model.ERDatumObject.ERDatumObjectClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERABContact.class);

  public er.addressbookexample.model.ERABContact.ERABContactClazz clazz() {
    return er.addressbookexample.model.ERABContact.clazz;
  }
  
  public NSArray<er.addressbookexample.model.ERABAddress> addresses() {
    return (NSArray<er.addressbookexample.model.ERABAddress>)storedValueForKey(_ERABContact.ADDRESSES_KEY);
  }

  public NSArray<er.addressbookexample.model.ERABAddress> addresses(EOQualifier qualifier) {
    return addresses(qualifier, null, false);
  }

  public NSArray<er.addressbookexample.model.ERABAddress> addresses(EOQualifier qualifier, boolean fetch) {
    return addresses(qualifier, null, fetch);
  }

  public NSArray<er.addressbookexample.model.ERABAddress> addresses(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings, boolean fetch) {
    NSArray<er.addressbookexample.model.ERABAddress> results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(er.addressbookexample.model.ERABAddress.CONTACT_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray<EOQualifier> qualifiers = new NSMutableArray<EOQualifier>();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = er.addressbookexample.model.ERABAddress.clazz.objectsMatchingQualifier(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = addresses();
      if (qualifier != null) {
        results = (NSArray<er.addressbookexample.model.ERABAddress>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<er.addressbookexample.model.ERABAddress>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToAddresses(er.addressbookexample.model.ERABAddress object) {
    includeObjectIntoPropertyWithKey(object, _ERABContact.ADDRESSES_KEY);
  }

  public void removeFromAddresses(er.addressbookexample.model.ERABAddress object) {
    excludeObjectFromPropertyWithKey(object, _ERABContact.ADDRESSES_KEY);
  }

  public void addToAddressesRelationship(er.addressbookexample.model.ERABAddress object) {
    if (_ERABContact.LOG.isDebugEnabled()) {
      _ERABContact.LOG.debug("adding " + object + " to addresses relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToAddresses(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _ERABContact.ADDRESSES_KEY);
    }
  }

  public void removeFromAddressesRelationship(er.addressbookexample.model.ERABAddress object) {
    if (_ERABContact.LOG.isDebugEnabled()) {
      _ERABContact.LOG.debug("removing " + object + " from addresses relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromAddresses(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _ERABContact.ADDRESSES_KEY);
    }
  }

  public er.addressbookexample.model.ERABAddress createAddressesRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( er.addressbookexample.model.ERABAddress.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _ERABContact.ADDRESSES_KEY);
    return (er.addressbookexample.model.ERABAddress) eo;
  }

  public void deleteAddressesRelationship(er.addressbookexample.model.ERABAddress object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _ERABContact.ADDRESSES_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllAddressesRelationships() {
    Enumeration<er.addressbookexample.model.ERABAddress> objects = addresses().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteAddressesRelationship(objects.nextElement());
    }
  }

  public NSArray<er.addressbookexample.model.ERABGroup> groups() {
    return (NSArray<er.addressbookexample.model.ERABGroup>)storedValueForKey(_ERABContact.GROUPS_KEY);
  }

  public NSArray<er.addressbookexample.model.ERABGroup> groups(EOQualifier qualifier) {
    return groups(qualifier, null);
  }

  public NSArray<er.addressbookexample.model.ERABGroup> groups(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    NSArray<er.addressbookexample.model.ERABGroup> results;
      results = groups();
      if (qualifier != null) {
        results = (NSArray<er.addressbookexample.model.ERABGroup>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<er.addressbookexample.model.ERABGroup>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToGroups(er.addressbookexample.model.ERABGroup object) {
    includeObjectIntoPropertyWithKey(object, _ERABContact.GROUPS_KEY);
  }

  public void removeFromGroups(er.addressbookexample.model.ERABGroup object) {
    excludeObjectFromPropertyWithKey(object, _ERABContact.GROUPS_KEY);
  }

  public void addToGroupsRelationship(er.addressbookexample.model.ERABGroup object) {
    if (_ERABContact.LOG.isDebugEnabled()) {
      _ERABContact.LOG.debug("adding " + object + " to groups relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToGroups(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _ERABContact.GROUPS_KEY);
    }
  }

  public void removeFromGroupsRelationship(er.addressbookexample.model.ERABGroup object) {
    if (_ERABContact.LOG.isDebugEnabled()) {
      _ERABContact.LOG.debug("removing " + object + " from groups relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromGroups(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _ERABContact.GROUPS_KEY);
    }
  }

  public er.addressbookexample.model.ERABGroup createGroupsRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( er.addressbookexample.model.ERABGroup.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _ERABContact.GROUPS_KEY);
    return (er.addressbookexample.model.ERABGroup) eo;
  }

  public void deleteGroupsRelationship(er.addressbookexample.model.ERABGroup object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _ERABContact.GROUPS_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllGroupsRelationships() {
    Enumeration<er.addressbookexample.model.ERABGroup> objects = groups().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteGroupsRelationship(objects.nextElement());
    }
  }


}
