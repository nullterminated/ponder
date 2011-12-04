package er.addressbookexample.migrations;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.migration.ERXMigrationDatabase;
import er.extensions.migration.ERXMigrationTable;
import er.extensions.migration.ERXModelVersion;

public class ERAddressBookExample0 extends ERXMigrationDatabase.Migration {
	@Override
	public NSArray<ERXModelVersion> modelDependencies() {
		return new NSArray<ERXModelVersion>(new ERXModelVersion("ERDatum", 0));
	}

	@Override
	public void downgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
		// DO NOTHING
	}

	@Override
	public void upgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
		ERXMigrationTable erabDateDatumTable = database.newTableNamed("ERABDateDatum");
		erabDateDatumTable.newBigIntegerColumn("id", false);
		erabDateDatumTable.newDateColumn("value", false);

		erabDateDatumTable.create();
		erabDateDatumTable.setPrimaryKey("id");

		ERXMigrationTable erabAddressTable = database.newTableNamed("ERABAddress");
		erabAddressTable.newBigIntegerColumn("contactID", false);
		erabAddressTable.newBigIntegerColumn("id", false);

		erabAddressTable.create();
		erabAddressTable.setPrimaryKey("id");

		ERXMigrationTable erabContactTable = database.newTableNamed("ERABContact");
		erabContactTable.newBigIntegerColumn("id", false);

		erabContactTable.create();
		erabContactTable.setPrimaryKey("id");

		ERXMigrationTable erabContactGroupTable = database.newTableNamed("ERABContactGroup");
		erabContactGroupTable.newBigIntegerColumn("erabContactId", false);
		erabContactGroupTable.newIntegerColumn("erabGroupId", false);

		erabContactGroupTable.create();
		erabContactGroupTable.setPrimaryKey("erabContactId", "erabGroupId");

		ERXMigrationTable erabPhoneDatumTable = database.newTableNamed("ERABPhoneDatum");
		erabPhoneDatumTable.newBigIntegerColumn("id", false);
		erabPhoneDatumTable.newStringColumn("value", 50, false);

		erabPhoneDatumTable.create();
		erabPhoneDatumTable.setPrimaryKey("id");

		ERXMigrationTable erabEmailDatumTable = database.newTableNamed("ERABEmailDatum");
		erabEmailDatumTable.newBigIntegerColumn("id", false);
		erabEmailDatumTable.newStringColumn("value", 254, false);

		erabEmailDatumTable.create();
		erabEmailDatumTable.setPrimaryKey("id");

		ERXMigrationTable erabGroupTable = database.newTableNamed("ERABGroup");
		erabGroupTable.newIntegerColumn("id", false);

		erabGroupTable.create();
		erabGroupTable.setPrimaryKey("id");

		ERXMigrationTable erabStringDatumTable = database.newTableNamed("ERABStringDatum");
		erabStringDatumTable.newBigIntegerColumn("id", false);
		erabStringDatumTable.newStringColumn("value", 10000000, false);

		erabStringDatumTable.create();
		erabStringDatumTable.setPrimaryKey("id");

		erabDateDatumTable.addForeignKey("id", "ERDatum", "id");
		erabAddressTable.addForeignKey("contactID", "ERABContact", "id");
		erabAddressTable.addForeignKey("id", "ERDatumObject", "id");
		erabContactTable.addForeignKey("id", "ERDatumObject", "id");
		erabContactGroupTable.addForeignKey("erabContactId", "ERABContact", "id");
		erabContactGroupTable.addForeignKey("erabGroupId", "ERABGroup", "id");
		erabPhoneDatumTable.addForeignKey("id", "ERDatum", "id");
		erabEmailDatumTable.addForeignKey("id", "ERDatum", "id");
		erabStringDatumTable.addForeignKey("id", "ERDatum", "id");
	}
}