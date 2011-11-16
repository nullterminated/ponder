package er.persistentsessionstorage.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOSession;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSForwardException;

public class ERSessionInfo extends er.persistentsessionstorage.model.eogen._ERSessionInfo {
	private static final Logger log = Logger.getLogger(ERSessionInfo.class);

	public static final ERSessionInfoClazz<ERSessionInfo> clazz = new ERSessionInfoClazz<ERSessionInfo>();

	public static class ERSessionInfoClazz<T extends ERSessionInfo> extends
			er.persistentsessionstorage.model.eogen._ERSessionInfo._ERSessionInfoClazz<T> {
		/* more clazz methods here */
	}

	/**
	 * Initializes the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
	}

	public WOSession session() {
		return sessionFromArchivedData(sessionData());
	}

	public void archiveDataFromSession(WOSession session) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		NSData data = null;

		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(session);
			oos.flush();
			byte[] bytes = baos.toByteArray();
			data = new NSData(bytes);
		} catch (IOException e) {
			throw NSForwardException._runtimeExceptionForThrowable(e);
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					log.warn(e);
				}
			}
		}

		setSessionData(data);
	}

	public WOSession sessionFromArchivedData(NSData data) {
		Object object = null;
		byte[] bytes = data.bytes();
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
			object = ois.readObject();
		} catch (IOException e) {
			log.warn("Failed to deserialize session", e);
		} catch (ClassNotFoundException e) {
			log.warn("Failed to deserialize session", e);
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					log.warn(e);
				}
			}
		}
		return (WOSession) object;
	}
}
