// DO NOT EDIT.  Make changes to er.addressbookexample.model.ERABAddress.java instead.
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
public abstract class _ERABAddress extends er.datum.model.ERDatumObject {
  public static final String ENTITY_NAME = "ERABAddress";

  // Attributes
  public static final ERXKey<String> SUBTYPE = new ERXKey<String>("subtype");
  public static final String SUBTYPE_KEY = SUBTYPE.key();

  // Relationships
  public static final ERXKey<er.addressbookexample.model.ERABContact> CONTACT = new ERXKey<er.addressbookexample.model.ERABContact>("contact");
  public static final String CONTACT_KEY = CONTACT.key();
  public static final ERXKey<er.datum.model.ERDatum> OBJECT_DATA = new ERXKey<er.datum.model.ERDatum>("objectData");
  public static final String OBJECT_DATA_KEY = OBJECT_DATA.key();

  public static class _ERABAddressClazz<T extends er.addressbookexample.model.ERABAddress> extends er.datum.model.ERDatumObject.ERDatumObjectClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERABAddress.class);

  public er.addressbookexample.model.ERABAddress.ERABAddressClazz clazz() {
    return er.addressbookexample.model.ERABAddress.clazz;
  }
  
  public er.addressbookexample.model.ERABContact contact() {
    return (er.addressbookexample.model.ERABContact)storedValueForKey(_ERABAddress.CONTACT_KEY);
  }
  
  public void setContact(er.addressbookexample.model.ERABContact value) {
    takeStoredValueForKey(value, _ERABAddress.CONTACT_KEY);
  }

  public void setContactRelationship(er.addressbookexample.model.ERABContact value) {
    if (_ERABAddress.LOG.isDebugEnabled()) {
      _ERABAddress.LOG.debug("updating contact from " + contact() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setContact(value);
    }
    else if (value == null) {
    	er.addressbookexample.model.ERABContact oldValue = contact();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERABAddress.CONTACT_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERABAddress.CONTACT_KEY);
    }
  }

}
