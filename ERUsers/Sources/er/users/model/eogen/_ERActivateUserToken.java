// DO NOT EDIT.  Make changes to er.users.model.ERActivateUserToken.java instead.
package er.users.model.eogen;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;


@SuppressWarnings("all")
public abstract class _ERActivateUserToken extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERActivateUserToken";

  // Attributes

  // Relationships
  public static final ERXKey<er.users.model.ERUser> USER = new ERXKey<er.users.model.ERUser>("user");
  public static final String USER_KEY = USER.key();

  public static class _ERActivateUserTokenClazz<T extends er.users.model.ERActivateUserToken> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERActivateUserToken.class);

  public er.users.model.ERActivateUserToken.ERActivateUserTokenClazz clazz() {
    return er.users.model.ERActivateUserToken.clazz;
  }
  
  public er.users.model.ERUser user() {
    return (er.users.model.ERUser)storedValueForKey(_ERActivateUserToken.USER_KEY);
  }
  
  public void setUser(er.users.model.ERUser value) {
    takeStoredValueForKey(value, _ERActivateUserToken.USER_KEY);
  }

  public void setUserRelationship(er.users.model.ERUser value) {
    if (_ERActivateUserToken.LOG.isDebugEnabled()) {
      _ERActivateUserToken.LOG.debug("updating user from " + user() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setUser(value);
    }
    else if (value == null) {
    	er.users.model.ERUser oldValue = user();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERActivateUserToken.USER_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERActivateUserToken.USER_KEY);
    }
  }

}
