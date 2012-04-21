package er.users.migrations;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.migration.ERXMigrationDatabase;
import er.extensions.migration.ERXMigrationTable;
import er.extensions.migration.ERXModelVersion;

public class ERUsers0 extends ERXMigrationDatabase.Migration {
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
		ERXMigrationTable erUserTable = database.newTableNamed("ERUser");
		erUserTable.newTimestampColumn("dateCreated", false);
		erUserTable.newStringColumn("emailAddress", 254, false);
		erUserTable.newIntegerColumn("id", false);
		erUserTable.newStringColumn("password", 128, false);
		erUserTable.newStringColumn("salt", 174, false);
		erUserTable.newStringColumn("username", 50, false);

		erUserTable.addUniqueIndex("username_idx", erUserTable.existingColumnNamed("username"));

		erUserTable.create();
	 	erUserTable.setPrimaryKey("id");

		ERXMigrationTable erCredentialTable = database.newTableNamed("ERCredential");
		erCredentialTable.newTimestampColumn("dateCreated", false);
		erCredentialTable.newIntegerColumn("id", false);
		erCredentialTable.newStringColumn("password", 128, false);
		erCredentialTable.newStringColumn("salt", 174, false);
		erCredentialTable.newIntegerColumn("userID", false);


		erCredentialTable.create();
	 	erCredentialTable.setPrimaryKey("id");

		erCredentialTable.addForeignKey("userID", "ERUser", "id");
	}
}