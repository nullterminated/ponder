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

		ERXMigrationTable erEntityPermissionTable = database.newTableNamed("EREntityPermission");
		erEntityPermissionTable.newFlagBooleanColumn("allowCreate", false);
		erEntityPermissionTable.newFlagBooleanColumn("allowDelete", false);
		erEntityPermissionTable.newFlagBooleanColumn("allowQuery", false);
		erEntityPermissionTable.newFlagBooleanColumn("allowRead", false);
		erEntityPermissionTable.newFlagBooleanColumn("allowUpdate", false);
		erEntityPermissionTable.newIntegerColumn("id", false);
		erEntityPermissionTable.newStringColumn("nameForEntity", 255, false);
		erEntityPermissionTable.newIntegerColumn("roleID", false);

		erEntityPermissionTable.addUniqueIndex("unique_entity_role_idx", erEntityPermissionTable.existingColumnNamed("roleID"), erEntityPermissionTable.existingColumnNamed("nameForEntity"));

		erEntityPermissionTable.create();
	 	erEntityPermissionTable.setPrimaryKey("id");

		ERXMigrationTable erPropertyPermissionTable = database.newTableNamed("ERPropertyPermission");
		erPropertyPermissionTable.newFlagBooleanColumn("allowRead", false);
		erPropertyPermissionTable.newFlagBooleanColumn("allowUpdate", false);
		erPropertyPermissionTable.newIntegerColumn("entityPermissionID", false);
		erPropertyPermissionTable.newIntegerColumn("id", false);
		erPropertyPermissionTable.newStringColumn("nameForProperty", 255, false);

		erPropertyPermissionTable.addUniqueIndex("unique_property_entity_idx", erPropertyPermissionTable.existingColumnNamed("nameForProperty"), erPropertyPermissionTable.existingColumnNamed("entityPermissionID"));

		erPropertyPermissionTable.create();
	 	erPropertyPermissionTable.setPrimaryKey("id");

		ERXMigrationTable erRoleTable = database.newTableNamed("ERRole");
		erRoleTable.newIntegerColumn("id", false);
		erRoleTable.newStringColumn("roleName", 50, false);

		erRoleTable.addUniqueIndex("unique_roleName_idx", erRoleTable.existingColumnNamed("roleName"));

		erRoleTable.create();
	 	erRoleTable.setPrimaryKey("id");

		ERXMigrationTable erTwoFactorAuthenticationRequestTable = database.existingTableNamed("ERAuthenticationRequest");
		erTwoFactorAuthenticationRequestTable.newStringColumn("username", 50, true);



		ERXMigrationTable erTwoFactorAuthenticationResponseTable = database.existingTableNamed("ERAuthenticationResponse");



		erAuthenticationResponseTable.addForeignKey("authenticationRequestID", "ERAuthenticationRequest", "id");
		erEntityPermissionTable.addForeignKey("roleID", "ERRole", "id");
		erPropertyPermissionTable.addForeignKey("entityPermissionID", "EREntityPermission", "id");
		erTwoFactorAuthenticationResponseTable.addForeignKey("authenticationRequestID", "ERAuthenticationRequest", "id");
	}
}