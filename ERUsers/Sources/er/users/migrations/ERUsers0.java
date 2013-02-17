package er.users.migrations;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.migration.ERXMigrationDatabase;
import er.extensions.migration.ERXMigrationTable;
import er.extensions.migration.ERXModelVersion;

public class ERUsers0 extends ERXMigrationDatabase.Migration {
	@Override
	public NSArray<ERXModelVersion> modelDependencies() {
		return new NSArray<ERXModelVersion>(new ERXModelVersion("ERAuth", 0));
	}
  
	@Override
	public void downgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
		// DO NOTHING
	}

	@Override
	public void upgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
		ERXMigrationTable erChallengeQuestionTable = database.newTableNamed("ERChallengeQuestion");
		erChallengeQuestionTable.newIntegerColumn("id", false);
		erChallengeQuestionTable.newStringColumn("question", 255, false);


		erChallengeQuestionTable.create();
	 	erChallengeQuestionTable.setPrimaryKey("id");

		ERXMigrationTable erChallengeResponseTable = database.newTableNamed("ERChallengeResponse");
		erChallengeResponseTable.newStringColumn("answer", 60, false);
		erChallengeResponseTable.newIntegerColumn("challengeQuestionID", false);
		erChallengeResponseTable.newIntegerColumn("id", false);
		erChallengeResponseTable.newIntegerColumn("userID", false);

		erChallengeResponseTable.addUniqueIndex("challengeQuestionID_userID_idx", erChallengeResponseTable.existingColumnNamed("challengeQuestionID"), erChallengeResponseTable.existingColumnNamed("userID"));

		erChallengeResponseTable.create();
	 	erChallengeResponseTable.setPrimaryKey("id");

		ERXMigrationTable erCredentialTable = database.newTableNamed("ERCredential");
		erCredentialTable.newTimestampColumn("dateCreated", false);
		erCredentialTable.newIntegerColumn("id", false);
		erCredentialTable.newStringColumn("password", 60, false);
		erCredentialTable.newIntegerColumn("userID", false);


		erCredentialTable.create();
	 	erCredentialTable.setPrimaryKey("id");

		ERXMigrationTable erUserTable = database.newTableNamed("ERUser");
		erUserTable.newStringColumn("activateUserToken", 50, true);
		erUserTable.newStringColumn("activationStatus", 50, false);
		erUserTable.newTimestampColumn("dateCreated", false);
		erUserTable.newIntegerColumn("id", false);
		erUserTable.newBigIntegerColumn("mailAddressID", false);
		erUserTable.newStringColumn("password", 60, false);
		erUserTable.newTimestampColumn("resetRequestDate", true);
		erUserTable.newStringColumn("resetToken", 50, true);
		erUserTable.newStringColumn("subtype", 50, false);
		erUserTable.newStringColumn("username", 50, false);

		erUserTable.addUniqueIndex("ERUser_username_unique_idx", erUserTable.existingColumnNamed("username"));

		erUserTable.create();
	 	erUserTable.setPrimaryKey("id");

		ERXMigrationTable erUserRoleTable = database.newTableNamed("ERUserRole");
		erUserRoleTable.newIntegerColumn("erRoleId", false);
		erUserRoleTable.newIntegerColumn("erUserId", false);


		erUserRoleTable.create();
	 	erUserRoleTable.setPrimaryKey("erUserId", "erRoleId");

		erChallengeResponseTable.addForeignKey("challengeQuestionID", "ERChallengeQuestion", "id");
		erChallengeResponseTable.addForeignKey("userID", "ERUser", "id");
		erCredentialTable.addForeignKey("userID", "ERUser", "id");
		erUserTable.addForeignKey("mailAddressID", "ERCMailAddress", "id");
		erUserRoleTable.addForeignKey("erRoleId", "ERRole", "id");
		erUserRoleTable.addForeignKey("erUserId", "ERUser", "id");
	}
}