package er.r2d2w.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EODataSource;

import er.directtoweb.components.ERD2WExtraDisplayPropertyKeysComponent;

public class R2D2WExtraDisplayPropertyKeys extends ERD2WExtraDisplayPropertyKeysComponent {
    private EODataSource dataSource;
	
	public R2D2WExtraDisplayPropertyKeys(WOContext context) {
        super(context);
    }

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(EODataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @return the dataSource
	 */
	public EODataSource dataSource() {
		return dataSource;
	}
}