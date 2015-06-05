package er.corebl.migrations;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.jdbc.ERXSQLHelper.ColumnIndex;
import er.extensions.migration.ERXMigrationDatabase;
import er.extensions.migration.ERXMigrationIndex;
import er.extensions.migration.ERXMigrationTable;
import er.extensions.migration.ERXModelVersion;

public class ERCoreBL3 extends ERXMigrationDatabase.Migration {
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
		ERXMigrationTable ercMailOpenTable = database.newTableNamed("ERCMailOpen");
		ercMailOpenTable.newTimestampColumn("dateOpened", ALLOWS_NULL);
		ercMailOpenTable.newIntegerColumn("id", NOT_NULL);
		ercMailOpenTable.newBigIntegerColumn("mailMessageID", NOT_NULL);
		ercMailOpenTable.newIntegerColumn("userAgentID", ALLOWS_NULL);
		ercMailOpenTable.create();
	 	ercMailOpenTable.setPrimaryKey("id");

		ERXMigrationTable ercUserAgentTable = database.newTableNamed("ERCUserAgent");
		ercUserAgentTable.newBlobColumn("contentHash", NOT_NULL);
		ercUserAgentTable.newStringColumn("contentString", 10000000, NOT_NULL);
		ercUserAgentTable.newIntegerColumn("id", NOT_NULL);
		ercUserAgentTable.create();
	 	ercUserAgentTable.setPrimaryKey("id");
		ercUserAgentTable.addIndex(new ERXMigrationIndex(
			"contentHash_idx", true 
			,new ColumnIndex("contentHash")
		));

		ercMailOpenTable.addForeignKey("mailMessageID", "ERCMailMessage", "id");
		ercMailOpenTable.addForeignKey("userAgentID", "ERCUserAgent", "id");
	}
}