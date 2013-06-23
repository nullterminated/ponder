package er.corebl.migrations;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.foundation.ERXProperties;
import er.extensions.migration.ERXMigrationDatabase;
import er.extensions.migration.ERXMigrationTable;
import er.extensions.migration.ERXModelVersion;

public class ERCoreBL0 extends ERXMigrationDatabase.Migration {
	
	public ERCoreBL0() {
		super(ERXProperties.arrayForKey("ERCoreBL0.languages"));
	}
	
	@Override
	public NSArray<ERXModelVersion> modelDependencies() {
		return new NSArray<ERXModelVersion>(new ERXModelVersion("ERAttachment", 0));
	}

	@Override
	public void downgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
		// DO NOTHING
	}

	@Override
	public void upgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
		ERXMigrationTable ercMailMessageTable = database.newTableNamed("ERCMailMessage");
		ercMailMessageTable.newTimestampColumn("dateRead", true);
		ercMailMessageTable.newTimestampColumn("dateSent", true);
		ercMailMessageTable.newStringColumn("exceptionReason", 1000, true);
		ercMailMessageTable.newBigIntegerColumn("fromAddressID", false);
		ercMailMessageTable.newBigIntegerColumn("htmlClobID", true);
		ercMailMessageTable.newBigIntegerColumn("id", false);
		ercMailMessageTable.newIntegerColumn("mailCategoryID", true);
		ercMailMessageTable.newBigIntegerColumn("plainClobID", true);
		ercMailMessageTable.newBigIntegerColumn("replyToAddressID", true);
		ercMailMessageTable.newStringColumn("state", 50, false);
		ercMailMessageTable.newStringColumn("subject", 255, false);
		ercMailMessageTable.newStringColumn("uuid", 50, false);
		ercMailMessageTable.newStringColumn("xMailer", 255, true);

		ercMailMessageTable.addUniqueIndex("ERCMailMessage_uuid_idx", ercMailMessageTable.existingColumnNamed("uuid"));

		ercMailMessageTable.create();
	 	ercMailMessageTable.setPrimaryKey("id");

		ERXMigrationTable ercMailCategoryTable = database.newTableNamed("ERCMailCategory");
		ercMailCategoryTable.newLocalizedStringColumns("detail", 1000, false);
		ercMailCategoryTable.newIntegerColumn("id", false);
		ercMailCategoryTable.newLocalizedStringColumns("name", 50, false);


		ercMailCategoryTable.create();
	 	ercMailCategoryTable.setPrimaryKey("id");

		ERXMigrationTable ercMailAttachmentTable = database.newTableNamed("ERCMailAttachment");
		ercMailAttachmentTable.newIntegerColumn("attachmentID", true);
		ercMailAttachmentTable.newStringColumn("filePath", 1000, true);
		ercMailAttachmentTable.newBigIntegerColumn("id", false);
		ercMailAttachmentTable.newFlagBooleanColumn("isInline", false);
		ercMailAttachmentTable.newBigIntegerColumn("mailMessageID", false);
		ercMailAttachmentTable.newStringColumn("token", 50, false);

		ercMailAttachmentTable.addUniqueIndex("attachmentID_mailMessageID_idx", ercMailAttachmentTable.existingColumnNamed("mailMessageID"), ercMailAttachmentTable.existingColumnNamed("attachmentID"));
		ercMailAttachmentTable.addUniqueIndex("mailMessageID_token_idx", ercMailAttachmentTable.existingColumnNamed("mailMessageID"), ercMailAttachmentTable.existingColumnNamed("token"));

		ercMailAttachmentTable.create();
	 	ercMailAttachmentTable.setPrimaryKey("id");

		ERXMigrationTable ercAuditTrailEntryTable = database.newTableNamed("ERCAuditTrailEntry");
		ercAuditTrailEntryTable.newTimestampColumn("created", false);
		ercAuditTrailEntryTable.newBigIntegerColumn("id", false);
		ercAuditTrailEntryTable.newStringColumn("keyPath", 100, true);
		ercAuditTrailEntryTable.newBigIntegerColumn("newClobID", true);
		ercAuditTrailEntryTable.newBigIntegerColumn("oldClobID", true);
		ercAuditTrailEntryTable.newBigIntegerColumn("trailID", false);
		ercAuditTrailEntryTable.newStringColumn("type", 50, false);
		ercAuditTrailEntryTable.newStringColumn("userGlobalID", 255, true);


		ercAuditTrailEntryTable.create();
	 	ercAuditTrailEntryTable.setPrimaryKey("id");

		ERXMigrationTable ercMailRecipientTable = database.newTableNamed("ERCMailRecipient");
		ercMailRecipientTable.newBigIntegerColumn("id", false);
		ercMailRecipientTable.newBigIntegerColumn("mailAddressID", false);
		ercMailRecipientTable.newBigIntegerColumn("mailMessageID", false);
		ercMailRecipientTable.newStringColumn("recipientType", 3, false);

		ercMailRecipientTable.addUniqueIndex("mailAddressID_mailMessageID_idx", ercMailRecipientTable.existingColumnNamed("mailAddressID"), ercMailRecipientTable.existingColumnNamed("mailMessageID"));

		ercMailRecipientTable.create();
	 	ercMailRecipientTable.setPrimaryKey("id");

		ERXMigrationTable ercMailAddressTable = database.newTableNamed("ERCMailAddress");
		ercMailAddressTable.newStringColumn("emailAddress", 254, false);
		ercMailAddressTable.newBigIntegerColumn("id", false);
		ercMailAddressTable.newFlagBooleanColumn("isActive", false);

		ercMailAddressTable.addUniqueIndex("ERCMailAddress_emailAddress_unique_idx", ercMailAddressTable.existingColumnNamed("emailAddress"));

		ercMailAddressTable.create();
	 	ercMailAddressTable.setPrimaryKey("id");

		ERXMigrationTable ercAuditClobTable = database.newTableNamed("ERCAuditClob");
		ercAuditClobTable.newBigIntegerColumn("id", false);
		ercAuditClobTable.newLargeStringColumn("valuesString", false);


		ercAuditClobTable.create();
	 	ercAuditClobTable.setPrimaryKey("id");

		ERXMigrationTable ercMailClobTable = database.newTableNamed("ERCMailClob");
		ercMailClobTable.newBigIntegerColumn("id", false);
		ercMailClobTable.newLargeStringColumn("message", false);


		ercMailClobTable.create();
	 	ercMailClobTable.setPrimaryKey("id");

		ERXMigrationTable ercAuditTrailTable = database.newTableNamed("ERCAuditTrail");
		ercAuditTrailTable.newStringColumn("gid", 255, false);
		ercAuditTrailTable.newBigIntegerColumn("id", false);
		ercAuditTrailTable.newFlagBooleanColumn("isDeleted", false);

		ercAuditTrailTable.addUniqueIndex("ERCAuditTrail_gid_idx", ercAuditTrailTable.existingColumnNamed("gid"));

		ercAuditTrailTable.create();
	 	ercAuditTrailTable.setPrimaryKey("id");

		ERXMigrationTable ercMailAddressCategoryPreferenceTable = database.newTableNamed("ERCMailAddressCategoryPreference");
		ercMailAddressCategoryPreferenceTable.newBigIntegerColumn("ercMailAddressId", false);
		ercMailAddressCategoryPreferenceTable.newIntegerColumn("ercMailCategoryId", false);


		ercMailAddressCategoryPreferenceTable.create();
	 	ercMailAddressCategoryPreferenceTable.setPrimaryKey("ercMailAddressId", "ercMailCategoryId");

		ERXMigrationTable ercPreferenceTable = database.newTableNamed("ERCPreference");
		ercPreferenceTable.newBigIntegerColumn("id", false);
		ercPreferenceTable.newStringColumn("prefKey", 100, false);
		ercPreferenceTable.newLargeStringColumn("prefValue", false);
		ercPreferenceTable.newIntegerColumn("userID", false);

		ercPreferenceTable.addUniqueIndex("prefKey_userID_idx", ercPreferenceTable.existingColumnNamed("prefKey"), ercPreferenceTable.existingColumnNamed("userID"));

		ercPreferenceTable.create();
	 	ercPreferenceTable.setPrimaryKey("id");

		ercMailMessageTable.addForeignKey("fromAddressID", "ERCMailAddress", "id");
		ercMailMessageTable.addForeignKey("htmlClobID", "ERCMailClob", "id");
		ercMailMessageTable.addForeignKey("mailCategoryID", "ERCMailCategory", "id");
		ercMailMessageTable.addForeignKey("plainClobID", "ERCMailClob", "id");
		ercMailMessageTable.addForeignKey("replyToAddressID", "ERCMailAddress", "id");
		ercMailAttachmentTable.addForeignKey("attachmentID", "ERAttachment", "id");
		ercMailAttachmentTable.addForeignKey("mailMessageID", "ERCMailMessage", "id");
		ercAuditTrailEntryTable.addForeignKey("newClobID", "ERCAuditClob", "id");
		ercAuditTrailEntryTable.addForeignKey("oldClobID", "ERCAuditClob", "id");
		ercAuditTrailEntryTable.addForeignKey("trailID", "ERCAuditTrail", "id");
		ercMailRecipientTable.addForeignKey("mailAddressID", "ERCMailAddress", "id");
		ercMailRecipientTable.addForeignKey("mailMessageID", "ERCMailMessage", "id");
		ercMailAddressCategoryPreferenceTable.addForeignKey("ercMailAddressId", "ERCMailAddress", "id");
		ercMailAddressCategoryPreferenceTable.addForeignKey("ercMailCategoryId", "ERCMailCategory", "id");
	}
}
