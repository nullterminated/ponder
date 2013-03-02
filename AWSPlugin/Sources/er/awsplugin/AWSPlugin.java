package er.awsplugin;

import org.apache.log4j.Logger;

import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOJoin;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EOProperty;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.eocontrol.EOClassDescription;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;

import er.awsplugin.model.SESBounceNotification;
import er.awsplugin.model.SESComplaintNotification;
import er.corebl.ERCoreBL;
import er.corebl.model.ERCMailAddress;
import er.extensions.ERXExtensions;
import er.extensions.ERXFrameworkPrincipal;
import er.extensions.eof.ERXModelGroup;
import er.extensions.foundation.ERXSelectorUtilities;

public class AWSPlugin extends ERXFrameworkPrincipal {

    public static final Logger log = Logger.getLogger(AWSPlugin.class);
    public final static Class<?>[] REQUIRES = new Class[] {ERXExtensions.class, ERCoreBL.class};

    protected static volatile AWSPlugin sharedInstance;

    // Registers the class as the framework principal
    static {
    	log.debug("Static Initializer for AWSPlugin");
    	setUpFrameworkPrincipalClass (AWSPlugin.class);
    }

    public static AWSPlugin sharedInstance() {
        if (sharedInstance == null) {
        	synchronized (AWSPlugin.class) {
        		if(sharedInstance == null) {
        			sharedInstance = sharedInstance(AWSPlugin.class);
        		}
        	}
        }
        return sharedInstance;
    }
    
    @Override
    public void finishInitialization() {
    	/*
    	 * Dynamically create reverse relationships for notifications
    	 * to prevent tying ERCoreBL to AWSPlugin
    	 */
    	NSNotificationCenter.defaultCenter().addObserver(
    			this, 
    			ERXSelectorUtilities.notificationSelector("modelGroupAdded"), 
    			ERXModelGroup.ModelGroupAddedNotification, 
    			null);
    }
    
    public void modelGroupAdded(NSNotification n) {
    	EOModelGroup group = (EOModelGroup) n.object();
    	if(group.modelNamed("ERCoreBL") != null && group.modelNamed("AWSPlugin") != null) {
        	createRelationship("complaints", ERCMailAddress.ENTITY_NAME, "id", SESComplaintNotification.ENTITY_NAME, "mailAddressID", true, EOClassDescription.DeleteRuleDeny, false, true, false);
        	createRelationship("bounces", ERCMailAddress.ENTITY_NAME, "id", SESBounceNotification.ENTITY_NAME, "mailAddressID", true, EOClassDescription.DeleteRuleDeny, false, true, false);
    	}
    }
    
    //TODO remove this method and use the one in ERXEOAccessUtilities when the fix hits master.
	/**
	 * Programatically adds a toOne or toMany relationship to an entity at runtime.
	 * 
	 * @param relationshipName name of the relationship to be created on the source entity
	 * @param sourceEntityName name of the source entity
	 * @param sourceAttributeName name of the attribute in the source entity to be used by the join
	 * @param destinationEntityName name of the destination entity
	 * @param destinationAttributeName name of the attribute in the destination entity to be used by the join
	 * @param toMany if true, the relationship will be toMany, otherwise it will be toOne
	 * @param deleteRule
	 *            EOClassDescription.DeleteRuleCascade ||
	 *            EOClassDescription.DeleteRuleDeny ||
	 *            EOClassDescription.DeleteRuleNoAction ||
	 *            EOClassDescription.DeleteRuleNullify
	 * @param isMandatory mandatory or not
	 * @param isClassProperty class property or not
	 * @param shouldPropagatePrimaryKey propagate primary key or not
	 * @return the newly created relationship
	 * 
	 * @author th
	 */
	private static EORelationship createRelationship(String relationshipName, String sourceEntityName, String sourceAttributeName, String destinationEntityName, String destinationAttributeName, boolean toMany, int deleteRule, boolean isMandatory, boolean isClassProperty, boolean shouldPropagatePrimaryKey) {
		EOEntity sourceEntity = EOModelGroup.defaultGroup().entityNamed(sourceEntityName);
		if(sourceEntity.isAbstractEntity())
			log.warn("If you programatically add relationships to an abstract entity, make sure you also add it to child entities");
		EOEntity destinationEntity = EOModelGroup.defaultGroup().entityNamed(destinationEntityName);
		EOAttribute sourceAttribute = sourceEntity.attributeNamed(sourceAttributeName);
		EOAttribute destinationAttribute = destinationEntity.attributeNamed(destinationAttributeName);
		EOJoin join = new EOJoin(sourceAttribute, destinationAttribute);
		EORelationship relationship = new EORelationship();

		relationship.setName(relationshipName);
		sourceEntity.addRelationship(relationship);
		relationship.addJoin(join);
		relationship.setToMany(toMany);
		relationship.setJoinSemantic(EORelationship.InnerJoin);
		relationship.setDeleteRule(deleteRule);
		relationship.setIsMandatory(isMandatory);
		relationship.setPropagatesPrimaryKey(shouldPropagatePrimaryKey);
		NSMutableArray<EOProperty> classProperties = sourceEntity.classProperties().mutableClone();
		if (isClassProperty && !classProperties.containsObject(relationship)) {
			classProperties.addObject(relationship);
			sourceEntity.setClassProperties(classProperties);
		} else if (!isClassProperty && classProperties.containsObject(relationship)) {
			classProperties.removeObject(relationship);
			sourceEntity.setClassProperties(classProperties);
		}
		if(log.isDebugEnabled())
			log.debug(relationship);

		return relationship;
	}

}
