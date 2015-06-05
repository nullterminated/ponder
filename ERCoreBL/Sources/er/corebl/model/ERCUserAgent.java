package er.corebl.model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import com.webobjects.appserver.WOMessage;
import com.webobjects.appserver.WORequest;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSForwardException;

import er.extensions.eof.ERXEC;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;

public class ERCUserAgent extends er.corebl.model.eogen._ERCUserAgent {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ERCUserAgent.class);

	public static final ERCUserAgentClazz<ERCUserAgent> clazz = new ERCUserAgentClazz<ERCUserAgent>();

	public static class ERCUserAgentClazz<T extends ERCUserAgent> extends
			er.corebl.model.eogen._ERCUserAgent._ERCUserAgentClazz<T> {
		/* more clazz methods here */

		/**
		 * 
		 * @param ec
		 *            where the user agent EO is instantiated
		 * @param request
		 *            the request from which to extract user-agent
		 * @return a user agent EO or null if no user-agent header exists in the
		 *         request
		 */
		public ERCUserAgent userAgentFromRequest(EOEditingContext ec, WORequest request) {
			String userAgentString = request.headerForKey("user-agent");
			if (userAgentString == null) {
				return null;
			}
			/*
			 * CHECKME correct encoding? default header encoding for http
			 * request is ISO-8859-1? WOMessage default header encoding hard
			 * coded to UTF-8 Assuming no encoding issues for now...
			 */
			byte[] bytes;
			try {
				bytes = userAgentString.getBytes(WOMessage.defaultHeaderEncoding());
			} catch (UnsupportedEncodingException e) {
				throw NSForwardException._runtimeExceptionForThrowable(e);
			}

			MessageDigest md5;
			try {
				md5 = MessageDigest.getInstance("md5");
			} catch (NoSuchAlgorithmException e) {
				throw NSForwardException._runtimeExceptionForThrowable(e);
			}

			byte[] hash = md5.digest(bytes);
			NSData contentHash = new NSData(hash);

			ERCUserAgent userAgent = objectMatchingKeyAndValue(ec, CONTENT_HASH_KEY, contentHash);

			if (userAgent == null) {
				EOEditingContext tmpEc = ERXEC.newEditingContext();
				userAgent = createAndInsertObject(tmpEc);
				userAgent.setContentHash(contentHash);
				userAgent.setContentString(userAgentString);
				try {
					tmpEc.saveChanges();
				} catch (Exception e) {
					/*
					 * Race condition? Someone else may have saved the same hash
					 * before us. Retry fetch. If still null, rethrow exception
					 */
					userAgent = objectMatchingKeyAndValue(ec, CONTENT_HASH_KEY, contentHash);
					if (userAgent == null) {
						throw NSForwardException._runtimeExceptionForThrowable(e);
					}
				}
				userAgent = (ERCUserAgent) userAgent.localInstanceIn(ec);
			}

			return userAgent;
		}
	}

	/**
	 * Initializes the EO. This is called when an EO is created, not when it is
	 * inserted into an EC.
	 */
	public void init(EOEditingContext ec) {
		super.init(ec);
	}

	@Override
	public void validateForUpdate() throws ValidationException {
		// Only insert and delete are allowed
		ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
		ERXValidationException ex = factory.createCustomException(this, "ImmutableEOException");
		throw ex;
	}
}
