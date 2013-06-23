package er.corebl;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOApplication;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOEntityClassDescription;
import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eoaccess.EOJoin;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSLog;

import er.corebl.audittrail.ERCAuditTrailHandler;
import er.corebl.components.ERCMailableExceptionPage;
import er.corebl.mail.ERCMailState;
import er.corebl.model.ERCMailAddress;
import er.corebl.model.ERCMailMessage;
import er.corebl.model.ERCPreference;
import er.corebl.preferences.ERCoreUserInterface;
import er.corebl.preferences.ERCoreUserPreferences;
import er.directtoweb.ERDirectToWeb;
import er.extensions.ERXExtensions;
import er.extensions.ERXFrameworkPrincipal;
import er.extensions.appserver.ERXApplication;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXEOControlUtilities;
import er.extensions.foundation.ERXConfigurationManager;
import er.extensions.foundation.ERXProperties;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.foundation.ERXThreadStorage;
import er.extensions.foundation.ERXUtilities;
import er.javamail.ERJavaMail;

/**
 *
 * @property er.corebl.ERCoreBL.problemEmailDomain holds the email domain of the generated from email.
 * @property er.corebl.ERCoreBL.problemEmailRecipients holds the email messages of those who receive problem messages.
 *
 * @author nullterminated
 */
public class ERCoreBL extends ERXFrameworkPrincipal {
	private static final Logger log = Logger.getLogger(ERCoreBL.class);

	/** holds the shared instance reference */
	protected static volatile ERCoreBL sharedInstance;

	public final static Class<?> REQUIRES[] = new Class[] { ERXExtensions.class, ERDirectToWeb.class, ERJavaMail.class };

	public static final String ACTOR_KEY = "actor";
	public static final String PREFERENCES_RELATIONSHIP_NAME = "preferences";
	public static final String USER_RELATIONSHIP_NAME = "user";

	public static final String ProblemEmailDomainPropertyKey = "er.corebl.ERCoreBL.problemEmailDomain";

	public static final String ProblemEmailRecipientsPropertyKey = "er.corebl.ERCoreBL.problemEmailRecipients";

	/** caches the email addresses to send to in the case of problems */
	protected NSArray<String> _emailsForProblemRecipients;

	/** caches the problem email address domain */
	protected String _problemEmailDomain;

	/**
	 * Register the class as the framework principal
	 */
	static {
		setUpFrameworkPrincipalClass(ERCoreBL.class);
	}

	/**
	 * Get the shared instance of the ERCoreBusinessLogic.
	 *
	 * @return shared instance.
	 */
	public static ERCoreBL sharedInstance() {
		if (sharedInstance == null) {
			synchronized (ERCoreBL.class) {
				if (sharedInstance == null) {
					sharedInstance = ERXFrameworkPrincipal.sharedInstance(ERCoreBL.class);
				}
			}
		}
		return sharedInstance;
	}

	@Override
	public void finishInitialization() {
		ERCoreUserPreferences.INSTANCE.registerHandlers();

		// Initialize the audit trail handler
		ERCAuditTrailHandler.init();

		log.info("ERCoreBL finished initializing.");
	}

	public boolean shouldMailReportedExceptions() {
		return ERXProperties.booleanForKey("er.corebl.ERCoreBL.shouldMailReportedExceptions");
	}

	/**
	 * Get the array of email addresses to send emails about problems to.
	 *
	 * @return array of email addresses
	 */
	public NSArray<String> emailsForProblemRecipients() {
		if (_emailsForProblemRecipients == null) {
			_emailsForProblemRecipients = ERXProperties.arrayForKeyWithDefault(ProblemEmailRecipientsPropertyKey, NSArray.EmptyArray);
		}
		return _emailsForProblemRecipients;
	}

	/**
	 * Set the emails for problem recipients. Should be an array of email
	 * addresses to report exceptions to in production applications.
	 *
	 * @param a
	 *            array of email addresses
	 */
	public void setEmailsForProblemRecipients(NSArray<String> a) {
		_emailsForProblemRecipients = a;
	}

	/**
	 * Get the problem email domain. This is used for constructing the from
	 * address when reporting an exception. Should be of the form: foo.com.
	 *
	 * @return problem email address domain
	 */
	public String problemEmailDomain() {
		if (_problemEmailDomain == null) {
			_problemEmailDomain = System.getProperty(ProblemEmailDomainPropertyKey);
		}
		return _problemEmailDomain;
	}

	/**
	 * Set the problem email domain.
	 *
	 * @param value
	 *            to set problem domain to
	 */
	public void setProblemEmailDomain(String value) {
		_problemEmailDomain = value;
	}

	public void addPreferenceRelationshipToActorEntity(String entityName) {
		EOEntity entity = EOModelGroup.defaultGroup().entityNamed(entityName);
		if (entity != null && entity.primaryKeyAttributeNames().count() == 1) {
			addPreferenceRelationshipToActorEntity(entityName, (String) entity.primaryKeyAttributeNames().lastObject());
		} else {
			throw new IllegalArgumentException("Entity is not suitable: " + entityName);
		}
	}

	/**
	 * Register a run-time relationship called "preferences" on the actor
	 * entity of your business logic. The framework needs preferences
	 * relationship to access user preferences for a specific actor. Call this
	 * method when you initialize your business logic layer. (Check
	 * BTBusinessLogic class as an example.)
	 *
	 * @param entityName
	 *            String name for your actor entity
	 * @param attributeNameToJoin
	 *            String attribute name on the actor entity; used by the
	 *            relationship and typically it's the primary key.
	 */
	public void addPreferenceRelationshipToActorEntity(String entityName, String attributeNameToJoin) {
		EOEntity actor = EOModelGroup.defaultGroup().entityNamed(entityName);
		EOEntity preference = EOModelGroup.defaultGroup().entityNamed(ERCPreference.ENTITY_NAME);

		EOJoin preferencesJoin = new EOJoin(actor.attributeNamed(attributeNameToJoin),
				preference.attributeNamed(ERCPreference.USER_ID_KEY));
		EORelationship preferencesRelationship = new EORelationship();

		preferencesRelationship.setName(PREFERENCES_RELATIONSHIP_NAME);
		actor.addRelationship(preferencesRelationship);
		preferencesRelationship.addJoin(preferencesJoin);
		preferencesRelationship.setToMany(true);
		preferencesRelationship.setJoinSemantic(EORelationship.InnerJoin);
		preferencesRelationship.setDeleteRule(EOEntityClassDescription.DeleteRuleCascade);

		EOJoin userJoin = new EOJoin(preference.attributeNamed(ERCPreference.USER_ID_KEY),
				actor.attributeNamed(attributeNameToJoin));
		EORelationship userRelationship = new EORelationship();
		userRelationship.setName(USER_RELATIONSHIP_NAME);
		preference.addRelationship(userRelationship);
		userRelationship.addJoin(userJoin);
		userRelationship.setToMany(false);
		userRelationship.setJoinSemantic(EORelationship.InnerJoin);
	}

	/**
	 * Set the actor in the current thread storage.
	 *
	 * @param actor
	 *            current user for this thread
	 */
	public static void setActor(EOEnterpriseObject actor) {
		if (log.isDebugEnabled()) {
			log.debug("Setting actor to : " + actor);
		}
		if (actor != null) {
			ERXThreadStorage.takeValueForKey(actor, ACTOR_KEY);
		} else {
			ERXThreadStorage.removeValueForKey(ACTOR_KEY);
		}
	}

	/**
	 * Get the actor as a local instance in the given context.
	 *
	 * @param ec
	 *            editing context to pull a local copy of the actor into
	 * @return actor instance in the given editing context
	 */
	public static EOEnterpriseObject actor(EOEditingContext ec) {
		EOEnterpriseObject actor = actor();
		if (actor != null && actor.editingContext() != ec) {
			EOEditingContext actorEc = actor.editingContext();
			actorEc.lock();
			try {
				EOEnterpriseObject localActor = (EOEnterpriseObject) ERXEOControlUtilities.localInstanceOfObject(ec,
						actor);
				try {
					if (actor instanceof ERCoreUserInterface) {
						NSArray<ERCPreference> prefs = ((ERCoreUserInterface) actor).preferences();
						prefs = ERXEOControlUtilities.localInstancesOfObjects(ec, prefs);
						((ERCoreUserInterface) localActor).setPreferences(prefs);
					}
				} catch (RuntimeException ex) {
					log.error("Error while setting getting actor's preferences: " + ex, ex);
				}
				actor = localActor;
			} finally {
				actorEc.unlock();
			}
		}
		return actor;
	}

	/**
	 * Get the actor.
	 *
	 * @return current actor for the thread
	 */
	public static EOEnterpriseObject actor() {
		return (EOEnterpriseObject) ERXThreadStorage.valueForKey(ACTOR_KEY);
	}

	public synchronized void reportException(Throwable exception, NSDictionary<String, Object> extraInfo) {
		if (exception instanceof NSForwardException) {
			exception = ((NSForwardException) exception).originalException();
		}
		StringBuffer s = new StringBuffer();
		try {
			s.append(" **** Caught: " + exception + "\n");
			s.append(extraInfoString(extraInfo, 3));

			if (exception instanceof EOGeneralAdaptorException) {
				EOGeneralAdaptorException e = (EOGeneralAdaptorException) exception;
				if (e.userInfo() != null) {
					Object userInfo = e.userInfo();
					if (userInfo instanceof NSDictionary) {
						NSDictionary<String,Object> uid = (NSDictionary<String,Object>) userInfo;
						for (String key: uid.allKeys()) {
							Object value = uid.objectForKey(key);
							s.append(key + " = " + value + ";\n");
						}
					} else {
						s.append(e.userInfo().toString());
					}
				}
			} else {
				s.append(ERXUtilities.stackTrace(exception));
			}
			if (!WOApplication.application().isCachingEnabled() || !shouldMailReportedExceptions()) {
				log.error(s.toString());
			} else {
				// Usually the Mail appender is set to Threshold ERROR
				log.warn(s.toString());
				if (emailsForProblemRecipients().count() == 0 || problemEmailDomain() == null) {
					log.error("Unable to log problem due to misconfiguration: recipients: "
							+ emailsForProblemRecipients() + " email domain: " + problemEmailDomain());
				} else {
					ERCMailableExceptionPage standardExceptionPage =
							(ERCMailableExceptionPage) ERXApplication.instantiatePage(ERCMailableExceptionPage.class.getSimpleName());
					standardExceptionPage.setException(exception);
					standardExceptionPage.setActor(actor());
					standardExceptionPage.setExtraInfo(extraInfo);

					EOEditingContext ec = ERXEC.newEditingContext();
					ec.lock();
					try {
						String shortExceptionName;
						Throwable exceptionForTitle = exception;
						if (exception instanceof InvocationTargetException) {
							exceptionForTitle = ((InvocationTargetException) exception).getTargetException();
						}
						shortExceptionName = ERXStringUtilities.lastPropertyKeyInKeyPath(exceptionForTitle.getClass()
								.getName());

						String hostName = ERXConfigurationManager.defaultManager().hostName();
						
						String fromString = WOApplication.application().name() + "-" + hostName + "@" + problemEmailDomain();
						ERCMailAddress fromAddress = ERCMailAddress.clazz.addressForEmailString(ec, fromString);
						String subject = WOApplication.application().name() + ": " + shortExceptionName + ": " + exceptionForTitle.getMessage();
						String htmlContent = standardExceptionPage.generateResponse().contentString();
						NSArray<ERCMailAddress> toAddresses = ERCMailAddress.clazz.addressesForEmailStrings(ec, emailsForProblemRecipients());
						ERCMailMessage.clazz.composeMailMessage(ec, ERCMailState.READY_TO_BE_SENT, fromAddress, null, toAddresses, null, null,subject, htmlContent, null, null, null);
						ec.saveChanges();
					} finally {
						ec.unlock();
					}
					ec.dispose();
				}
			}
		} catch (Throwable u) {
			try {
				s.append("************ Caught exception " + u + " trying to report another one: " + exception + "\n");
				s.append("** Original exception\n");
				s.append(ERXUtilities.stackTrace(exception) + "\n");
				s.append("** Second exception\n");
				s.append(ERXUtilities.stackTrace(u) + "\n");
				NSLog.err.appendln(s.toString());
				log.error(s.toString());
			} catch (Throwable u2) {
			} // WE DON'T WANT ANYTHING TO GO WRONG IN HERE as it would cause
				// the app to instantly exit
		}
	}

	public synchronized String extraInfoString(NSDictionary<String, Object> extraInfo, int indent) {
		StringBuffer s = new StringBuffer();
		ERXStringUtilities.indent(s, indent);
		s.append("Extra Information: \n");
		ERXStringUtilities.indent(s, indent);
		s.append("    Actor = " + (actor() != null ? actor().toString() : "No Actor") + "\n");
		if (extraInfo != null && extraInfo.count() > 0) {
			for (String key : extraInfo.allKeys()) {
				Object value = extraInfo.objectForKey(key);
				if (value instanceof NSDictionary) {
					String valueStr = String.valueOf(value);
					StringBuffer valueIndent = new StringBuffer();
					valueIndent.append("\n         ");
					ERXStringUtilities.indent(valueIndent, indent);
					for (int i = 0; i < key.length(); i++) {
						valueIndent.append(" ");
					}
					value = valueStr.replaceAll("\n", valueIndent.toString());
				}
				ERXStringUtilities.indent(s, indent);
				s.append("    " + key + " = " + value + "\n");
			}
		}
		return s.toString();
	}

}
