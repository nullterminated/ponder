package er.auth.interfaces;

import com.webobjects.eocontrol.EOQualifier;

public interface CRUDClazz {
	public boolean canCreate(CRUDAuthorization auth);
	public boolean canQuery(CRUDAuthorization auth);
	public EOQualifier restrictingQualifier(CRUDAuthorization auth);
}
