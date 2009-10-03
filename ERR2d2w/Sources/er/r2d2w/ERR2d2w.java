package er.r2d2w;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOApplication;
import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOEntityClassDescription;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.eocontrol.EOClassDescription;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSSelector;

import er.directtoweb.ERDirectToWeb;
import er.extensions.ERXExtensions;
import er.extensions.ERXFrameworkPrincipal;
import er.extensions.eof.ERXConstant;
import er.extensions.eof.ERXModelGroup;
import er.extensions.foundation.ERXProperties;
import er.extensions.foundation.ERXRetainer;
import er.r2d2w.delegates.PreferenceHandlerDelegate;

public class ERR2d2w extends ERXFrameworkPrincipal {

	public static final String DERIVED_COUNT = "DerivedCount";
    public static final Logger log = Logger.getLogger(ERR2d2w.class);
    public final static Class<?>[] REQUIRES = new Class[] {ERXExtensions.class, ERDirectToWeb.class};

    protected static ERR2d2w sharedInstance;

    // Registers the class as the framework principal
    static {
    	log.debug("Static Initializer for ERR2d2w");
    	setUpFrameworkPrincipalClass (ERR2d2w.class);
    }

    public static ERR2d2w sharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = sharedInstance(ERR2d2w.class);
        }
        return sharedInstance;
    }
    
	@Override
    public void finishInitialization() {
        // Initialized shared data
		if(printLocalizableKeys() || createsDerivedCounts()) {
			NSNotificationCenter.defaultCenter().addObserver(this,
	    	        new NSSelector<Void>("applicationDidFinishLaunching",
	    	                ERXConstant.NotificationClassArray),
	    	                WOApplication.ApplicationDidFinishLaunchingNotification,
	    					null);
		}
    }

    /**
     * Registers notification handlers for user preference notifications.
     */
    private static void registerHandlers() {
        log.debug("Registering preference handlers");
        Object handler = null;
        String className = ERXProperties.stringForKey("er.corebusinesslogic.ERCoreUserPreferences.handlerClassName");
    	if(className != null) {
    		try {
    			handler = Class.forName(className).newInstance();
    		} catch (Exception e) {
    			throw NSForwardException._runtimeExceptionForThrowable(e);
    		}
    	}
        if(handler == null) {
        	handler = new PreferenceHandlerDelegate();
        }
        ERXRetainer.retain(handler);        
    }

    /**
     * Checks the system property <code>er.directtoweb.ERDirectToWeb.createsDerivedCounts</code>.
     */
    public static boolean createsDerivedCounts() {
        return ERXProperties.booleanForKeyWithDefault("er.r2d2w.ERR2d2w.createsDerivedCounts", false);
    }

    /**
     * Checks the system property <code>er.directtoweb.ERDirectToWeb.printLocalizableKeys</code>.
     */
    public static boolean printLocalizableKeys() {
        return ERXProperties.booleanForKeyWithDefault("er.r2d2w.ERR2d2w.printLocalizableKeys", false);
    }
    
    public static NSArray<String> modelsToIgnore() {
    	return ERXProperties.arrayForKeyWithDefault("er.r2d2w.ERR2d2w.ignoreModels", NSArray.EmptyArray);
    }
    
    public static void applicationDidFinishLaunching(NSNotification n) {
    	registerHandlers();
    	
    	NSArray<EOModel> models = EOModelGroup.defaultGroup().models();
    	for(EOModel model: models) {
    		if(modelsToIgnore().contains(model.name())) {
    			continue;
    		}
    		
	    	if(printLocalizableKeys()) {
	    		printLocalizableStringsKeys(model);
	    	}
	    	
	    	if(createsDerivedCounts()) {
	    		createDerivedCountAttributes(model);
	    	}
    	}
    }
    
    public static void printLocalizableStringsKeys(EOModel model) {
    	
		log.debug("Localizable keys for model: " + model.name());
		NSMutableArray<String> propKeys = new NSMutableArray<String>();
		StringBuilder requiredKeys = new StringBuilder("/* Required keys for Entity and Attribute localization */\n\n");
		StringBuilder recommendedKeys = new StringBuilder("/* Recommended keys for improved accessibility */\n\n");
		StringBuilder optionalKeys = new StringBuilder("/* Optional keys for input instructions */\n\n");
		StringBuilder pageTitleKeys = new StringBuilder("/* Page Title Keys */\n\n");

		// Build key strings
		for(EOEntity entity: model.entities()) {
			
			EOClassDescription ecd = entity.classDescriptionForInstances();
			
			// Entity keys
			requiredKeys.append("\"Entity.name.").append(entity.name()).append("\" = \"\";\n");
			recommendedKeys.append("\"Entity.name.").append(entity.name()).append(".tableCaption\" = \"\";\n");
			recommendedKeys.append("\"Entity.name.").append(entity.name()).append(".tableSummary\" = \"\";\n");
			optionalKeys.append("/* Input instructions for entity: ").append(entity.name()).append(" */\n");
			
			pageTitleKeys.append("\"PageTitle.Create").append(entity.name()).append("\" = \"\";\n");
			pageTitleKeys.append("\"PageTitle.ConfirmDelete").append(entity.name()).append("\" = \"\";\n");
			pageTitleKeys.append("\"PageTitle.Edit").append(entity.name()).append("\" = \"\";\n");
			pageTitleKeys.append("\"PageTitle.Inspect").append(entity.name()).append("\" = \"\";\n");
			pageTitleKeys.append("\"PageTitle.List").append(entity.name()).append("\" = \"\";\n");
			pageTitleKeys.append("\"PageTitle.Query").append(entity.name()).append("\" = \"\";\n");
			
			// Attribute keys
			NSMutableArray<String> attrKeys = new NSMutableArray<String>();
			for(String attrName: ecd.attributeKeys()){
				EOAttribute attr = entity.attributeNamed(attrName);
				if(attr.userInfo() != null && attr.userInfo().containsKey(ERXModelGroup.LANGUAGES_KEY)) {
					attrName = attrName.substring(0, attrName.lastIndexOf("_"));
					if(attrKeys.contains(attrName)) { continue; }
					attrKeys.add(attrName);
				}
				if(propKeys.contains(attrName)) {
					requiredKeys.append("/* Name collision: Be advised, PropertyKey.").append(attrName).append(" is shared by multiple entities! */\n");
					recommendedKeys.append("/* Name collision: Be advised, PropertyKey.").append(attrName).append(".attributeAbbr is shared by multiple entities! */\n");
					optionalKeys.append("/* Name collision: Be advised, PropertyKey.").append(attrName).append(".inputInfo is shared by multiple entities! */\n");
				} else {
					propKeys.add(attrName);
					requiredKeys.append("\"PropertyKey.").append(attrName).append("\" = \"\";\n");
					recommendedKeys.append("\"PropertyKey.").append(attrName).append(".attributeAbbr\" = \"\";\n");
					optionalKeys.append("\"PropertyKey.").append(attrName).append(".inputInfo\" = \"\";\n");
				}
			}
			
			// Relationship keys
			for(String toOne: ecd.toOneRelationshipKeys()) {
				if(propKeys.contains(toOne)) {
					requiredKeys.append("/* Name collision: Be advised, PropertyKey.").append(toOne).append(" is shared by multiple entities! */\n");
					recommendedKeys.append("/* Name collision: Be advised, PropertyKey.").append(toOne).append(".attributeAbbr is shared by multiple entities! */\n");
					optionalKeys.append("/* Name collision: Be advised, PropertyKey.").append(toOne).append(".inputInfo is shared by multiple entities! */\n");
				} else {
					propKeys.add(toOne);
					requiredKeys.append("\"PropertyKey.").append(toOne).append("\" = \"\";\n");
					recommendedKeys.append("\"PropertyKey.").append(toOne).append(".attributeAbbr\" = \"\";\n");
					optionalKeys.append("\"PropertyKey.").append(toOne).append(".inputInfo\" = \"\";\n");						
				}
			}
			for(String toMany: ecd.toManyRelationshipKeys()) {
				if(propKeys.contains(toMany)) {
					requiredKeys.append("/* Name collision: Be advised, PropertyKey.").append(toMany).append(" is shared by multiple entities! */\n");
					recommendedKeys.append("/* Name collision: Be advised, PropertyKey.").append(toMany).append(".attributeAbbr is shared by multiple entities! */\n");
					optionalKeys.append("/* Name collision: Be advised, PropertyKey.").append(toMany).append(".inputInfo is shared by multiple entities! */\n");
				} else {
					propKeys.add(toMany);
					requiredKeys.append("\"PropertyKey.").append(toMany).append("\" = \"\";\n");
					recommendedKeys.append("\"PropertyKey.").append(toMany).append(".attributeAbbr\" = \"\";\n");
					optionalKeys.append("\"PropertyKey.").append(toMany).append(".inputInfo\" = \"\";\n");
				}
			}
			
			// Just spacing to make things easier to read
			requiredKeys.append("\n");
			recommendedKeys.append("\n");
			optionalKeys.append("\n");
			pageTitleKeys.append("\n");
		}
		
		// Print strings to console
		StringBuilder message = new StringBuilder("Localizable keys for model: ").append(model.name()).append("\n{\n");
		message.append(requiredKeys.toString()).append("\n");
		message.append(recommendedKeys.toString()).append("\n");
		message.append(optionalKeys.toString()).append("\n");
		message.append(pageTitleKeys.toString()).append("}\n");
		log.debug(message.toString());
	}
    
	public static void createDerivedCountAttributes(EOModel model) {
		log.debug("Model added: Begin adding derived counts");
		
		// Get all to-many relationships
		for(EOEntity entity: model.entities()) {
			
			//Compound keys aren't supported
			if(!entity.hasSimplePrimaryKey()) {continue;}

			//Must use ERPrototypes
			EOAttribute intProto = entity.model().prototypeAttributeNamed("intNumber");
			if(intProto == null){continue;}

			EOEntityClassDescription ecd = new EOEntityClassDescription(entity);
			NSArray<String> relationships = ecd.toManyRelationshipKeys();
			if(!(relationships.count() > 0)){continue;}
			
			//Create derived attribute to fetch relationship counts
			for(int k = 0; k < relationships.count(); k++) {
				EORelationship rel = entity.relationshipNamed(relationships.objectAtIndex(k).toString());
				
				// Entities must share the same model
				if(!rel.destinationEntity().model().equals(entity.model())){continue;}
				
				//CHECKME can I do this with flattened many-to-manys??
				// Relationship cannot be flattened
				if(rel.isFlattened()) {continue;}
				
				// Finally, build derived attribute
				EOAttribute att = new EOAttribute();
				
				// Set att name
				StringBuilder attName = new StringBuilder();
				attName.append(rel.name()).append(DERIVED_COUNT);
				att.setName(attName.toString());
				log.debug("Building derived attribute for entity named " + entity.name() + ": " + attName.toString());
				
				// Set proto attributes individually to avoid column name NPE
				att.setValueType(intProto.valueType());
				att.setExternalType(intProto.externalType());
				att.setClassName(intProto.className());
				
				// Other important settings
				att.setAllowsNull(true);
				att.setReadOnly(true);
				
				// Add to entity BEFORE setting definition
				entity.addAttribute(att);
				
				// Create and set definition
				StringBuilder attDef = new StringBuilder();
				attDef.append("(SELECT COUNT(*) FROM ");
				attDef.append(rel.destinationEntity().externalName());
				attDef.append(" WHERE ");
				attDef.append(rel.destinationEntity().externalName());
				attDef.append(".");
				attDef.append(rel.destinationAttributes().objectAtIndex(0).columnName());
				attDef.append(" = ");
				attDef.append(entity.primaryKeyAttributes().objectAtIndex(0).columnName());
				attDef.append(")");
				att.setDefinition(attDef.toString());
			}
		}
	}

}
