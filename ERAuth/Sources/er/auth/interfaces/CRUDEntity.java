package er.auth.interfaces;


public interface CRUDEntity {
	public boolean canDelete(CRUDAuthorization auth);
	public boolean canRead(CRUDAuthorization auth);
	public boolean canReadProperty(CRUDAuthorization auth, String propertyKey);
	public boolean canUpdate(CRUDAuthorization auth);
	public boolean canUpdateProperty(CRUDAuthorization auth, String propertyKey);
}
