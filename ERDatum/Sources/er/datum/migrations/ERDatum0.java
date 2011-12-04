package er.datum.migrations;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.migration.ERXMigrationDatabase;
import er.extensions.migration.ERXMigrationTable;
import er.extensions.migration.ERXModelVersion;

public class ERDatum0 extends ERXMigrationDatabase.Migration {
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
		ERXMigrationTable erDatumObjectTable = database.newTableNamed("ERDatumObject");
		erDatumObjectTable.newBigIntegerColumn("id", false);
		erDatumObjectTable.newStringColumn("subtype", 50, false);


		erDatumObjectTable.create();
	 	erDatumObjectTable.setPrimaryKey("id");

		ERXMigrationTable erDatumTable = database.newTableNamed("ERDatum");
		erDatumTable.newBigIntegerColumn("datumObjectID", false);
		erDatumTable.newBigIntegerColumn("id", false);
		erDatumTable.newStringColumn("subtype", 50, false);
		erDatumTable.newStringColumn("type", 50, false);


		erDatumTable.create();
	 	erDatumTable.setPrimaryKey("id");

		erDatumTable.addForeignKey("datumObjectID", "ERDatumObject", "id");
	}
}