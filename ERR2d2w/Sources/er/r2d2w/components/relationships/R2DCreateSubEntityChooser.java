package er.r2d2w.components.relationships;

import org.apache.commons.lang.StringUtils;

import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.ERD2WUtilities;
import com.webobjects.directtoweb.EditRelationshipPageInterface;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOClassDescription;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;

import er.directtoweb.components.ERDCustomComponent;
import er.extensions.appserver.ERXWOContext;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXGenericRecord;
import er.extensions.foundation.ERXValueUtilities;

public class R2DCreateSubEntityChooser extends ERDCustomComponent {
	private EOEntity subEntity;
	private EOEntity selectedEntity;
	private String labelID;
	private D2WContext ctx;
	private NSArray<EOEntity> createSubEntities;
	
	public R2DCreateSubEntityChooser(WOContext context) {
		super(context);
	}
	
	private D2WContext ctx() {
		if(ctx == null) {
			ctx = new D2WContext(session());
		}
		return ctx;
	}
	
	@SuppressWarnings("unchecked")
	public NSArray<EOEntity> createSubEntities() {
		if(createSubEntities == null) {
			createSubEntities = (NSArray<EOEntity>) d2wContext().valueForKey("createSubEntities");
		}
		return createSubEntities;
	}
	
	/**
	 * @return the subEntity
	 */
	public EOEntity subEntity() {
		return subEntity;
	}

	/**
	 * @param subEntity the subEntity to set
	 */
	public void setSubEntity(EOEntity subEntity) {
		this.subEntity = subEntity;
	}

	/**
	 * @return the selectedEntity
	 */
	public EOEntity selectedEntity() {
		return selectedEntity;
	}

	/**
	 * @param selectedEntity the selectedEntity to set
	 */
	public void setSelectedEntity(EOEntity selectedEntity) {
		this.selectedEntity = selectedEntity;
	}

	/**
	 * @return the labelID
	 */
	public String labelID() {
		if(labelID == null) {
			labelID = ERXWOContext.safeIdentifierName(context(), true);
		}
		return labelID;
	}

	public void createAction() {
		EditRelationshipPageInterface erpi = ERD2WUtilities.enclosingComponentOfClass(this, EditRelationshipPageInterface.class);
		if(erpi != null && selectedEntity() != null) {
			EOEnterpriseObject eo = (EOEnterpriseObject)NSKeyValueCoding.Utility.valueForKey(erpi, "masterObject");
			String relationshipKey = (String)NSKeyValueCoding.Utility.valueForKey(erpi, "relationshipKey");
			if(!ERXValueUtilities.isNull(eo) && !StringUtils.isBlank(relationshipKey)) {
				EOEditingContext nestedEC = ERXEC.newEditingContext(eo.editingContext());
				EOClassDescription relatedObjectClassDescription = selectedEntity().classDescriptionForInstances();
				EOEnterpriseObject relatedObject = (EOEnterpriseObject)EOUtilities.createAndInsertInstance(nestedEC, relatedObjectClassDescription.entityName());
				EOEnterpriseObject localObj = EOUtilities.localInstanceOfObject(relatedObject.editingContext(), eo);
				if (localObj instanceof ERXGenericRecord) {
					((ERXGenericRecord)localObj).setValidatedWhenNested(false);
				}
				localObj.addObjectToBothSidesOfRelationshipWithKey(relatedObject, relationshipKey);
				NSKeyValueCoding.Utility.takeValueForKey(erpi, relatedObject, "objectInRelationship");
				NSKeyValueCoding.Utility.takeValueForKey(erpi, "create", "inlineTaskSafely");
				ctx().setEntity(selectedEntity());
				String config = (String)ctx().valueForKey("editEmbeddedConfigurationName");
				NSKeyValueCoding.Utility.takeValueForKey(erpi, config, "inspectConfigurationName");
			}
		}
	}

	public String displayNameForEntity() {
		ctx().setEntity(subEntity());
		return (String)ctx().valueForKey("displayNameForEntity");
	}
}