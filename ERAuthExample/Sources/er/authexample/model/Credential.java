package er.authexample.model;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.webobjects.eocontrol.EOEditingContext;

public class Credential extends er.authexample.model.base._Credential {
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(Credential.class);

    public static final CredentialClazz<Credential> clazz = new CredentialClazz<Credential>();
    public static class CredentialClazz<T extends Credential> extends er.authexample.model.base._Credential._CredentialClazz<T> {
        /* more clazz methods here */
    }

    /**
     * Initializes the EO. This is called when an EO is created, not when it is 
     * inserted into an EC.
     */
    public void init(EOEditingContext ec) {
        super.init(ec);
        setDateCreated(new DateTime());
    }

}
