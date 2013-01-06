package er.awsplugin.migrations;

import java.sql.Types;

import com.webobjects.eocontrol.EOEditingContext;

import er.extensions.migration.ERXMigrationColumn;
import er.extensions.migration.ERXMigrationDatabase;

public class AWSPlugin1 extends ERXMigrationDatabase.Migration {

	@Override
	public void downgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
		// TODO Auto-generated method stub
	}

	@Override
	public void upgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
		ERXMigrationColumn diagnosticCode = database.existingColumnNamed("SESNotification", "diagnosticCode");
		diagnosticCode.setWidthType(Types.VARCHAR, 1000, null);
	}

}
