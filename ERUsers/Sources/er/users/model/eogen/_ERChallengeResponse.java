// DO NOT EDIT.  Make changes to er.users.model.ERChallengeResponse.java instead.
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
public abstract class _ERChallengeResponse extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERChallengeResponse";

  // Attributes
  public static final ERXKey<String> ANSWER = new ERXKey<String>("answer");
  public static final String ANSWER_KEY = ANSWER.key();
  public static final ERXKey<String> SALT = new ERXKey<String>("salt");
  public static final String SALT_KEY = SALT.key();

  // Relationships
  public static final ERXKey<er.users.model.ERChallengeQuestion> CHALLENGE_QUESTION = new ERXKey<er.users.model.ERChallengeQuestion>("challengeQuestion");
  public static final String CHALLENGE_QUESTION_KEY = CHALLENGE_QUESTION.key();
  public static final ERXKey<er.users.model.ERUser> USER = new ERXKey<er.users.model.ERUser>("user");
  public static final String USER_KEY = USER.key();

  public static class _ERChallengeResponseClazz<T extends er.users.model.ERChallengeResponse> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERChallengeResponse.class);

  public er.users.model.ERChallengeResponse.ERChallengeResponseClazz clazz() {
    return er.users.model.ERChallengeResponse.clazz;
  }
  
  public String answer() {
    return (String) storedValueForKey(_ERChallengeResponse.ANSWER_KEY);
  }

  public void setAnswer(String value) {
    if (_ERChallengeResponse.LOG.isDebugEnabled()) {
    	_ERChallengeResponse.LOG.debug( "updating answer from " + answer() + " to " + value);
    }
    takeStoredValueForKey(value, _ERChallengeResponse.ANSWER_KEY);
  }

  public String salt() {
    return (String) storedValueForKey(_ERChallengeResponse.SALT_KEY);
  }

  public void setSalt(String value) {
    if (_ERChallengeResponse.LOG.isDebugEnabled()) {
    	_ERChallengeResponse.LOG.debug( "updating salt from " + salt() + " to " + value);
    }
    takeStoredValueForKey(value, _ERChallengeResponse.SALT_KEY);
  }

  public er.users.model.ERChallengeQuestion challengeQuestion() {
    return (er.users.model.ERChallengeQuestion)storedValueForKey(_ERChallengeResponse.CHALLENGE_QUESTION_KEY);
  }
  
  public void setChallengeQuestion(er.users.model.ERChallengeQuestion value) {
    takeStoredValueForKey(value, _ERChallengeResponse.CHALLENGE_QUESTION_KEY);
  }

  public void setChallengeQuestionRelationship(er.users.model.ERChallengeQuestion value) {
    if (_ERChallengeResponse.LOG.isDebugEnabled()) {
      _ERChallengeResponse.LOG.debug("updating challengeQuestion from " + challengeQuestion() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setChallengeQuestion(value);
    }
    else if (value == null) {
    	er.users.model.ERChallengeQuestion oldValue = challengeQuestion();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERChallengeResponse.CHALLENGE_QUESTION_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERChallengeResponse.CHALLENGE_QUESTION_KEY);
    }
  }
  public er.users.model.ERUser user() {
    return (er.users.model.ERUser)storedValueForKey(_ERChallengeResponse.USER_KEY);
  }
  
  public void setUser(er.users.model.ERUser value) {
    takeStoredValueForKey(value, _ERChallengeResponse.USER_KEY);
  }

  public void setUserRelationship(er.users.model.ERUser value) {
    if (_ERChallengeResponse.LOG.isDebugEnabled()) {
      _ERChallengeResponse.LOG.debug("updating user from " + user() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setUser(value);
    }
    else if (value == null) {
    	er.users.model.ERUser oldValue = user();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _ERChallengeResponse.USER_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _ERChallengeResponse.USER_KEY);
    }
  }

}
