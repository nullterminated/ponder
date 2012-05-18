// DO NOT EDIT.  Make changes to er.corebl.model.ERCMailAddress.java instead.
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
public abstract class _ERCMailAddress extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERCMailAddress";

  // Attributes
  public static final ERXKey<String> EMAIL_ADDRESS = new ERXKey<String>("emailAddress");
  public static final String EMAIL_ADDRESS_KEY = EMAIL_ADDRESS.key();
  public static final ERXKey<Boolean> IS_ACTIVE = new ERXKey<Boolean>("isActive");
  public static final String IS_ACTIVE_KEY = IS_ACTIVE.key();

  // Relationships
  public static final ERXKey<er.corebl.model.ERCMailCategory> OPT_IN_CATEGORIES = new ERXKey<er.corebl.model.ERCMailCategory>("optInCategories");
  public static final String OPT_IN_CATEGORIES_KEY = OPT_IN_CATEGORIES.key();

  public static class _ERCMailAddressClazz<T extends er.corebl.model.ERCMailAddress> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERCMailAddress.class);

  public er.corebl.model.ERCMailAddress.ERCMailAddressClazz clazz() {
    return er.corebl.model.ERCMailAddress.clazz;
  }
  
  public String emailAddress() {
    return (String) storedValueForKey(_ERCMailAddress.EMAIL_ADDRESS_KEY);
  }

  public void setEmailAddress(String value) {
    if (_ERCMailAddress.LOG.isDebugEnabled()) {
    	_ERCMailAddress.LOG.debug( "updating emailAddress from " + emailAddress() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailAddress.EMAIL_ADDRESS_KEY);
  }

  public Boolean isActive() {
    return (Boolean) storedValueForKey(_ERCMailAddress.IS_ACTIVE_KEY);
  }

  public void setIsActive(Boolean value) {
    if (_ERCMailAddress.LOG.isDebugEnabled()) {
    	_ERCMailAddress.LOG.debug( "updating isActive from " + isActive() + " to " + value);
    }
    takeStoredValueForKey(value, _ERCMailAddress.IS_ACTIVE_KEY);
  }

  public NSArray<er.corebl.model.ERCMailCategory> optInCategories() {
    return (NSArray<er.corebl.model.ERCMailCategory>)storedValueForKey(_ERCMailAddress.OPT_IN_CATEGORIES_KEY);
  }

  public NSArray<er.corebl.model.ERCMailCategory> optInCategories(EOQualifier qualifier) {
    return optInCategories(qualifier, null);
  }

  public NSArray<er.corebl.model.ERCMailCategory> optInCategories(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    NSArray<er.corebl.model.ERCMailCategory> results;
      results = optInCategories();
      if (qualifier != null) {
        results = (NSArray<er.corebl.model.ERCMailCategory>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<er.corebl.model.ERCMailCategory>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToOptInCategories(er.corebl.model.ERCMailCategory object) {
    includeObjectIntoPropertyWithKey(object, _ERCMailAddress.OPT_IN_CATEGORIES_KEY);
  }

  public void removeFromOptInCategories(er.corebl.model.ERCMailCategory object) {
    excludeObjectFromPropertyWithKey(object, _ERCMailAddress.OPT_IN_CATEGORIES_KEY);
  }

  public void addToOptInCategoriesRelationship(er.corebl.model.ERCMailCategory object) {
    if (_ERCMailAddress.LOG.isDebugEnabled()) {
      _ERCMailAddress.LOG.debug("adding " + object + " to optInCategories relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToOptInCategories(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _ERCMailAddress.OPT_IN_CATEGORIES_KEY);
    }
  }

  public void removeFromOptInCategoriesRelationship(er.corebl.model.ERCMailCategory object) {
    if (_ERCMailAddress.LOG.isDebugEnabled()) {
      _ERCMailAddress.LOG.debug("removing " + object + " from optInCategories relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromOptInCategories(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _ERCMailAddress.OPT_IN_CATEGORIES_KEY);
    }
  }

  public er.corebl.model.ERCMailCategory createOptInCategoriesRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( er.corebl.model.ERCMailCategory.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _ERCMailAddress.OPT_IN_CATEGORIES_KEY);
    return (er.corebl.model.ERCMailCategory) eo;
  }

  public void deleteOptInCategoriesRelationship(er.corebl.model.ERCMailCategory object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _ERCMailAddress.OPT_IN_CATEGORIES_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllOptInCategoriesRelationships() {
    Enumeration<er.corebl.model.ERCMailCategory> objects = optInCategories().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteOptInCategoriesRelationship(objects.nextElement());
    }
  }


}
