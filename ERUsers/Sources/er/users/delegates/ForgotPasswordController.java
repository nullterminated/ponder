package er.users.delegates;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.webobjects.appserver.WOComponent;
import com.webobjects.directtoweb.ERD2WUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;

import er.directtoweb.delegates.ERDBranchDelegate;
import er.directtoweb.pages.ERD2WQueryPage;
import er.extensions.eof.ERXEC;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;
import er.users.model.ERUser;

public class ForgotPasswordController extends ERDBranchDelegate {
	/**
	 * Do I need to update serialVersionUID? See section 5.6 <cite>Type Changes
	 * Affecting Serialization</cite> on page 51 of the <a
	 * href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object
	 * Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	public WOComponent resetPassword(WOComponent sender) {
		ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
		ERD2WQueryPage page = ERD2WUtilities.enclosingComponentOfClass(sender, ERD2WQueryPage.class);
		EOEditingContext ec = ERXEC.newEditingContext();
		String username = (String) page.displayGroup().queryMatch().objectForKey(ERUser.USERNAME_KEY);
		if(username == null) {
			ERXValidationException ex = factory.createCustomException(null, ERUser.USERNAME_KEY, username, "NullUsernameException");
			page.validationFailedWithException(ex, username, ERUser.USERNAME_KEY);
			return null;
		}

		EOQualifier q = ERUser.USERNAME.eq(username);
		
		//Duplicate users by username should be impossible due to the unique index
		ERUser user = ERUser.clazz.objectsMatchingQualifier(ec, q).lastObject();
		if(user == null) {
			ERXValidationException ex = factory.createCustomException(null, ERUser.USERNAME_KEY, username, "UsernameNotFound");
			page.validationFailedWithException(ex, username, ERUser.USERNAME_KEY);
			return null;
		}
		
		if(user.resetRequestDate() == null) {
			//Set request date and token
			user.setResetRequestDate(new DateTime());
			user.setResetToken(UUID.randomUUID().toString());
			ec.saveChanges();
		}
		
		DateTime bypassDate = user.resetRequestDate().plus(outOfBandWaitingPeriod());
		if(bypassDate.compareTo(new DateTime()) > 0) {
			// TODO Send email
		} else {
			// TODO Go directly to challenge questions
		}
		return null;
	}
	
	/**
	 * Provided so that subclasses can provide a different waiting period.
	 * 
	 * @return the waiting period
	 */
	protected Duration outOfBandWaitingPeriod() {
		return Duration.standardDays(7);
	}
}
