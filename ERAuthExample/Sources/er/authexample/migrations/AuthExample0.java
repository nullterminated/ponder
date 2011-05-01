package er.authexample.migrations;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.migration.ERXMigrationDatabase;
import er.extensions.migration.ERXMigrationTable;
import er.extensions.migration.ERXModelVersion;

public class AuthExample0 extends ERXMigrationDatabase.Migration {
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
		ERXMigrationTable credentialTable = database.newTableNamed("Credential");
		credentialTable.newTimestampColumn("dateCreated", false);
		credentialTable.newIntegerColumn("id", false);
		credentialTable.newStringColumn("password", 128, false);
		credentialTable.newIntegerColumn("userID", false);
		credentialTable.create();
		credentialTable.setPrimaryKey("id");

		ERXMigrationTable userTable = database.newTableNamed("User");
		userTable.newTimestampColumn("dateCreated", false);
		userTable.newStringColumn("emailAddress", 255, false);
		userTable.newIntegerColumn("id", false);
		userTable.newStringColumn("password", 128, false);
		userTable.newStringColumn("username", 50, false);
		userTable.create();
		userTable.setPrimaryKey("id");
		userTable.addUniqueIndex("username_idx", userTable.existingColumnNamed("username"));

		credentialTable.addForeignKey("userID", "User", "id");
	}
}