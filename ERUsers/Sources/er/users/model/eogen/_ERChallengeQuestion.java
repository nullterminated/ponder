// DO NOT EDIT.  Make changes to er.users.model.ERChallengeQuestion.java instead.
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
public abstract class _ERChallengeQuestion extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "ERChallengeQuestion";

  // Attributes
  public static final ERXKey<String> QUESTION = new ERXKey<String>("question");
  public static final String QUESTION_KEY = QUESTION.key();

  // Relationships

  public static class _ERChallengeQuestionClazz<T extends er.users.model.ERChallengeQuestion> extends ERXGenericRecord.ERXGenericRecordClazz<T> {
    /* more clazz methods here */
  }

  private static final Logger LOG = Logger.getLogger(_ERChallengeQuestion.class);

  public er.users.model.ERChallengeQuestion.ERChallengeQuestionClazz clazz() {
    return er.users.model.ERChallengeQuestion.clazz;
  }
  
  public String question() {
    return (String) storedValueForKey(_ERChallengeQuestion.QUESTION_KEY);
  }

  public void setQuestion(String value) {
    if (_ERChallengeQuestion.LOG.isDebugEnabled()) {
    	_ERChallengeQuestion.LOG.debug( "updating question from " + question() + " to " + value);
    }
    takeStoredValueForKey(value, _ERChallengeQuestion.QUESTION_KEY);
  }


}
