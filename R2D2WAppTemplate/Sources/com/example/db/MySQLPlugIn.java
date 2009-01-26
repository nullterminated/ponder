package com.example.db;

import com.webobjects.eoaccess.EOAdaptor;
import com.webobjects.eoaccess.synchronization.EOSchemaSynchronizationFactory;
import com.webobjects.jdbcadaptor.JDBCAdaptor;

public class MySQLPlugIn extends com.webobjects.jdbcadaptor.MySQLPlugIn {
	
	public MySQLPlugIn(JDBCAdaptor adaptor) {
		super(adaptor);
	}
	
    public EOSchemaSynchronizationFactory createSchemaSynchronizationFactory() {
        return new MySQLSynchronizationFactory(_adaptor);
    }

	
	public class MySQLSynchronizationFactory extends com.webobjects.jdbcadaptor.MySQLPlugIn.MySQLSynchronizationFactory {
		public MySQLSynchronizationFactory(EOAdaptor adaptor) {
			super(adaptor);
		}
		
        public String _alterPhraseInsertionClausePrefixAtIndex(int columnIndex) {
            return "ADD";
        }

	}
}
