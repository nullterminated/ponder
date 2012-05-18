package er.corebl;

import org.apache.log4j.Logger;

import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOEntityClassDescription;
import com.webobjects.eoaccess.EOJoin;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;

import er.corebl.audittrail.ERCAuditTrailHandler;
import er.corebl.model.ERCPreference;
import er.corebl.preferences.ERCoreUserInterface;
import er.corebl.preferences.ERCoreUserPreferences;
import er.directtoweb.ERDirectToWeb;
import er.extensions.ERXExtensions;
import er.extensions.ERXFrameworkPrincipal;
import er.extensions.eof.ERXEOControlUtilities;
import er.extensions.foundation.ERXThreadStorage;
import er.javamail.ERJavaMail;

public class ERCoreBL extends ERXFrameworkPrincipal {
	private static final Logger log = Logger.getLogger(ERCoreBL.class);

	/** holds the shared instance reference */
	protected static volatile ERCoreBL sharedInstance;

	public final static Class<?> REQUIRES[] = new Class[] { ERXExtensions.class, ERDirectToWeb.class, ERJavaMail.class };

	public static final String ACTOR_KEY = "actor";
	public static final String PREFERENCES_RELATIONSHIP_NAME = "preferences";
	public static final String USER_RELATIONSHIP_NAME = "user";

	/**
	 * Registers the class as the framework principal
	 */
	static {
		setUpFrameworkPrincipalClass(ERCoreBL.class);
	}

	/**
	 * Gets the shared instance of the ERCoreBusinessLogic.
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
		
		//Just to trigger the static initializer
		ERCAuditTrailHandler.class.getName();

		log.info("ERCoreBL finished initializing.");
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
	 * Registers a run-time relationship called "preferences" on the actor
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
	 * Sets the actor in the current thread storage.
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
	 * Gets the actor as a local instance in the given context.
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
	 * Gets the actor.
	 * 
	 * @return current actor for the thread
	 */
	public static EOEnterpriseObject actor() {
		return (EOEnterpriseObject) ERXThreadStorage.valueForKey(ACTOR_KEY);
	}
}
