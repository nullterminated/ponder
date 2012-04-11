package er.auth.migrations;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.migration.ERXMigrationDatabase;
import er.extensions.migration.ERXMigrationTable;
import er.extensions.migration.ERXModelVersion;

public class ERAuth0 extends ERXMigrationDatabase.Migration {
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
		ERXMigrationTable erAuthenticationRequestTable = database.newTableNamed("ERAuthenticationRequest");
		erAuthenticationRequestTable.newIntegerColumn("id", false);
		erAuthenticationRequestTable.newIpAddressColumn("inetAddress", true);
		erAuthenticationRequestTable.newTimestampColumn("requestDate", false);
		erAuthenticationRequestTable.newStringColumn("subtype", 50, false);
		erAuthenticationRequestTable.create();
	 	erAuthenticationRequestTable.setPrimaryKey("id");

		ERXMigrationTable erAuthenticationResponseTable = database.newTableNamed("ERAuthenticationResponse");
		erAuthenticationResponseTable.newFlagBooleanColumn("authenticationFailed", false);
		erAuthenticationResponseTable.newStringColumn("authenticationFailureType", 50, true);
		erAuthenticationResponseTable.newIntegerColumn("authenticationRequestID", false);
		erAuthenticationResponseTable.newIntegerColumn("id", false);
		erAuthenticationResponseTable.newStringColumn("subtype", 50, false);
		erAuthenticationResponseTable.newStringColumn("userID", 255, true);
		erAuthenticationResponseTable.create();
	 	erAuthenticationResponseTable.setPrimaryKey("id");

		ERXMigrationTable erTwoFactorAuthenticationRequestTable = database.existingTableNamed("ERAuthenticationRequest");
		erTwoFactorAuthenticationRequestTable.newStringColumn("username", 50, true);

		erAuthenticationResponseTable.addForeignKey("authenticationRequestID", "ERAuthenticationRequest", "id");
	}
}