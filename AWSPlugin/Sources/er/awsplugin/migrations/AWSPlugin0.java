package er.awsplugin.migrations;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.migration.ERXMigrationDatabase;
import er.extensions.migration.ERXMigrationTable;
import er.extensions.migration.ERXModelVersion;

public class AWSPlugin0 extends ERXMigrationDatabase.Migration {
	@Override
	public NSArray<ERXModelVersion> modelDependencies() {
		return new NSArray<ERXModelVersion>(new ERXModelVersion("ERCoreBL", 2));
	}
  
	@Override
	public void downgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
		// DO NOTHING
	}

	@Override
	public void upgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
		ERXMigrationTable sesNotificationTable = database.newTableNamed("SESNotification");
		sesNotificationTable.newStringColumn("awsFeedbackID", 60, false);
		sesNotificationTable.newStringColumn("awsMessageID", 60, false);
		sesNotificationTable.newIntegerColumn("id", false);
		sesNotificationTable.newBigIntegerColumn("mailAddressID", false);
		sesNotificationTable.newBigIntegerColumn("mailMessageID", true);
		sesNotificationTable.newTimestampColumn("mailTimestamp", false);
		sesNotificationTable.newTimestampColumn("notificationTimestamp", false);
		sesNotificationTable.newStringColumn("notificationType", 10, false);
		sesNotificationTable.newBigIntegerColumn("sourceAddressID", false);

		sesNotificationTable.addUniqueIndex("awsMessageID_idx", sesNotificationTable.existingColumnNamed("awsMessageID"));

		sesNotificationTable.create();
	 	sesNotificationTable.setPrimaryKey("id");

		ERXMigrationTable sesBounceNotificationTable = database.existingTableNamed("SESNotification");
		sesBounceNotificationTable.newStringColumn("action", 16, true);
		sesBounceNotificationTable.newStringColumn("bounceSubType", 50, true);
		sesBounceNotificationTable.newStringColumn("bounceType", 10, true);
		sesBounceNotificationTable.newStringColumn("diagnosticCode", 255, true);
		sesBounceNotificationTable.newStringColumn("reportingMTA", 255, true);
		sesBounceNotificationTable.newStringColumn("status", 16, true);



		ERXMigrationTable sesComplaintNotificationTable = database.existingTableNamed("SESNotification");
		sesComplaintNotificationTable.newTimestampColumn("arrivalDate", true);
		sesComplaintNotificationTable.newStringColumn("complaintFeedbackType", 50, true);
		sesComplaintNotificationTable.newStringColumn("userAgent", 255, true);



		sesNotificationTable.addForeignKey("mailAddressID", "ERCMailAddress", "id");
		sesNotificationTable.addForeignKey("mailMessageID", "ERCMailMessage", "id");
		sesNotificationTable.addForeignKey("sourceAddressID", "ERCMailAddress", "id");
	}
}