package com.example.model;



import com.webobjects.eoaccess.EODatabaseContext;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSDictionary;

import er.extensions.eof.ERXEOControlUtilities;
import er.extensions.eof.ERXGenericRecord;
import er.extensions.localization.ERXLocalizer;

public class GenericRecord extends ERXGenericRecord {
	private static final String entityNameKeyPath = "Entity.name.";
	private static final String displayNameKey = "displayNameForEntity";
	private static final String newEODescriptionKey = "R2D2W.newEODescription";
	
	public String userPresentableDescription() {
		String result = null;
		if(isNewObject()) {
			String entityKey = entityNameKeyPath + entityName();
			ERXLocalizer loc = ERXLocalizer.currentLocalizer();
			String localizedEntityName = loc.localizedStringForKey(entityKey);
			if (localizedEntityName == null) {
				localizedEntityName = classDescription().displayNameForKey(entityName());
			}
			NSDictionary<String, String> dict = new NSDictionary<String, String>(localizedEntityName, displayNameKey);
			result = loc.localizedTemplateStringForKeyWithObject(newEODescriptionKey, dict);
		} else {
			result = super.userPresentableDescription();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public EOQualifier qualifierForRelationshipWithKey(String aKey) {
		this.willRead();

		if ((aKey != null))
		{
			String anEntityName = this.entityName();
			EOEntity anEntity = EOModelGroup.defaultGroup().entityNamed(anEntityName);
			EORelationship aRelationship = anEntity.relationshipNamed(aKey);

			if (aRelationship != null)
			{
				EOEditingContext anEditingContext = this.editingContext();
				EOGlobalID aGlobalID = anEditingContext.globalIDForObject(this);
				String aModelName = anEntity.model().name();
				EODatabaseContext aDatabaseContext = EOUtilities.databaseContextForModelNamed(anEditingContext,
				        aModelName);
				aDatabaseContext.lock();
				NSDictionary aRow = aDatabaseContext.snapshotForGlobalID(aGlobalID);
				aDatabaseContext.unlock();
				EOQualifier aQualifier = aRelationship.qualifierWithSourceRow(aRow);

				return aQualifier;
			}
		}

		return null;
	}

	/**
	 * Return count for the given relationship.
	 */
	public Number countForRelationship(String key)
	{
		EOQualifier qual = qualifierForRelationshipWithKey(key);
		if (qual != null)
		{
			String anEntityName = this.entityName();
			EOEntity anEntity = EOModelGroup.defaultGroup().entityNamed(anEntityName);
			EORelationship aRelationship = anEntity.relationshipNamed(key);
			return ERXEOControlUtilities.objectCountWithQualifier(this.editingContext(), aRelationship
			        .destinationEntity().name(), qual);
		}
		return null;

	}
}
