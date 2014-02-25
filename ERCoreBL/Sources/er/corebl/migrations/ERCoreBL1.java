package er.corebl.migrations;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.migration.ERXMigrationDatabase;
import er.extensions.migration.ERXMigrationTable;
import er.extensions.migration.ERXModelVersion;

public class ERCoreBL1 extends ERXMigrationDatabase.Migration {
	@Override
	public NSArray<ERXModelVersion> modelDependencies() {
		return null;
	}

	@Override
	public void downgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
		// DO NOTHING
	}

	@Override
	public void upgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
		ERXMigrationTable ercMailAddressTable = database.existingTableNamed("ERCMailAddress");
		ercMailAddressTable.newTimestampColumn("dateLastSent", true);
		ercMailAddressTable.newStringColumn("stopReason", 50, true);
		ercMailAddressTable.newStringColumn("verificationState", 50, false, "UNVERIFIED");
		
		ercMailAddressTable.existingColumnNamed("isActive").delete();
		
		ERXMigrationTable ercMailMessageTable = database.existingTableNamed("ERCMailMessage");
		ercMailMessageTable.newStringColumn("messageID", 100, true);
		
		ercMailMessageTable.addUniqueIndex("ERCMailMessage_messageID_idx", "messageID");
		
		ERXMigrationTable ercMailNotificationTable = database.newTableNamed("ERCMailNotification");
		ercMailNotificationTable.newIntegerColumn("id", false);
		ercMailNotificationTable.newBigIntegerColumn("mailAddressID", false);
		ercMailNotificationTable.newBigIntegerColumn("mailMessageID", false);
		ercMailNotificationTable.newTimestampColumn("notificationTimestamp", false);
		ercMailNotificationTable.newStringColumn("notificationType", 50, false);


		ercMailNotificationTable.create();
	 	ercMailNotificationTable.setPrimaryKey("id");

		ercMailNotificationTable.addForeignKey("mailAddressID", "ERCMailAddress", "id");
		ercMailNotificationTable.addForeignKey("mailMessageID", "ERCMailMessage", "id");
	}
}
