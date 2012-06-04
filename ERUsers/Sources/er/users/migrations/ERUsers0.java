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
		erUserTable.newStringColumn("activateUserTokenID", 50, true);
		erUserTable.newStringColumn("activationStatus", 50, false);
		erUserTable.newTimestampColumn("dateCreated", false);
		erUserTable.newStringColumn("emailAddress", 254, false);
		erUserTable.newIntegerColumn("id", false);
		erUserTable.newStringColumn("password", 128, false);
		erUserTable.newTimestampColumn("resetRequestDate", true);
		erUserTable.newStringColumn("resetToken", 50, true);
		erUserTable.newStringColumn("salt", 174, false);
		erUserTable.newStringColumn("username", 50, false);

		erUserTable.addUniqueIndex("username_idx", erUserTable.existingColumnNamed("username"));

		erUserTable.create();
	 	erUserTable.setPrimaryKey("id");

		ERXMigrationTable erChallengeQuestionTable = database.newTableNamed("ERChallengeQuestion");
		erChallengeQuestionTable.newIntegerColumn("id", false);
		erChallengeQuestionTable.newStringColumn("question", 255, false);


		erChallengeQuestionTable.create();
	 	erChallengeQuestionTable.setPrimaryKey("id");

		ERXMigrationTable erCredentialTable = database.newTableNamed("ERCredential");
		erCredentialTable.newTimestampColumn("dateCreated", false);
		erCredentialTable.newIntegerColumn("id", false);
		erCredentialTable.newStringColumn("password", 128, false);
		erCredentialTable.newStringColumn("salt", 174, false);
		erCredentialTable.newIntegerColumn("userID", false);


		erCredentialTable.create();
	 	erCredentialTable.setPrimaryKey("id");

		ERXMigrationTable erActivateUserTokenTable = database.newTableNamed("ERActivateUserToken");
		erActivateUserTokenTable.newStringColumn("token", 50, false);
		erActivateUserTokenTable.newIntegerColumn("userID", false);

		erActivateUserTokenTable.addUniqueIndex("ERActivateUserToken_userID_unique_idx", erActivateUserTokenTable.existingColumnNamed("userID"));

		erActivateUserTokenTable.create();
	 	erActivateUserTokenTable.setPrimaryKey("token");

		ERXMigrationTable erChallengeResponseTable = database.newTableNamed("ERChallengeResponse");
		erChallengeResponseTable.newStringColumn("answer", 128, false);
		erChallengeResponseTable.newIntegerColumn("challengeQuestionID", false);
		erChallengeResponseTable.newIntegerColumn("id", false);
		erChallengeResponseTable.newStringColumn("salt", 174, false);
		erChallengeResponseTable.newIntegerColumn("userID", false);

		erChallengeResponseTable.addUniqueIndex("challengeQuestionID_userID_idx", erChallengeResponseTable.existingColumnNamed("userID"), erChallengeResponseTable.existingColumnNamed("challengeQuestionID"));

		erChallengeResponseTable.create();
	 	erChallengeResponseTable.setPrimaryKey("id");

		erUserTable.addForeignKey("activateUserTokenID", "ERActivateUserToken", "token");
		erCredentialTable.addForeignKey("userID", "ERUser", "id");
		erActivateUserTokenTable.addForeignKey("userID", "ERUser", "id");
		erChallengeResponseTable.addForeignKey("challengeQuestionID", "ERChallengeQuestion", "id");
		erChallengeResponseTable.addForeignKey("userID", "ERUser", "id");
	}
}