package er.users.delegates;

import org.apache.commons.lang.StringUtils;

import com.webobjects.appserver.WOComponent;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.ERD2WUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSValidation;

import er.directtoweb.ERD2WContainer;
import er.directtoweb.delegates.ERDBranchDelegate;
import er.directtoweb.pages.ERD2WInspectPage;
import er.extensions.eof.ERXQ;
import er.extensions.localization.ERXLocalizer;
import er.extensions.qualifiers.ERXKeyValueQualifier;
import er.extensions.validation.ERXValidationException;
import er.extensions.validation.ERXValidationFactory;
import er.users.model.ERChallengeResponse;
import er.users.model.ERUser;

public class ResetPasswordController extends ERDBranchDelegate {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

	public void _nextStep(WOComponent sender) {
		ERD2WInspectPage page = ERD2WUtilities.enclosingComponentOfClass(sender, ERD2WInspectPage.class);

		//Verify user
		ERUser user = (ERUser) object(sender);
		NSArray<ERChallengeResponse> responses = user.challengeResponses();
		ERXKeyValueQualifier q = ERXQ.isFalse(ERChallengeResponse.VERIFY_ANSWER_MATCHES);
		responses = q.filtered(responses);
		boolean verified = responses.isEmpty();
		if(!verified) {
			//Display error
			ERXValidationFactory factory = ERXValidationFactory.defaultFactory();
			ERXValidationException ex = factory.createException(responses.lastObject(), ERChallengeResponse.VERIFY_ANSWER_KEY, null, ERXValidationException.InvalidValueException);
			page.validationFailedWithException(ex, null, ex.key());
			return;
		}
		
		//Set user password to null
		user.setPassword(null);

		//Set next tab
		NSArray<ERD2WContainer> tabs = page.tabSectionsContents();
		ERD2WContainer tab = page.currentTab();
		int index = tabs.indexOf(tab);
		if(index + 1 < tabs.count()) {
			ERD2WContainer next = tabs.objectAtIndex(index + 1);
			page.setCurrentTab(next);
			
			/*
			 * CHECKME not sure why it happens, but if I do this,
			 * deserialization fails on the following page.
			 */
			//User is verified. Log in.
			//EOEnterpriseObject eo = object(sender);
			//ERStageManager.INSTANCE.setActor(eo);
		}
	}
	
	public WOComponent _save(WOComponent sender) {
		ERD2WInspectPage page = ERD2WUtilities.enclosingComponentOfClass(sender, ERD2WInspectPage.class);
		D2WContext c = d2wContext(sender);
		ERUser user = (ERUser) object(sender);
		EOEditingContext ec = user.editingContext();
		
		if(ec == null) {
			page.setErrorMessage(ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("ERD2WInspect.alreadyAborted", c));
			page.clearValidationFailed();
			return null;
		}
		
		if(page.errorMessages().isEmpty() && StringUtils.isBlank(page.errorMessage()) && ec.hasChanges()) {
			try {
				ec.saveChanges();
				return page.nextPage(false);
			} catch(NSValidation.ValidationException e) {
				page.setErrorMessage(ERXLocalizer.currentLocalizer().localizedTemplateStringForKeyWithObject("CouldNotSave", e));
				page.validationFailedWithException(e, e.object(), "saveChangesExceptionKey");
			}
		}
		return null;
	}
	
    public WOComponent _cancelEdit(WOComponent sender) {
		EOEnterpriseObject eo = object(sender);
		ERD2WInspectPage page = ERD2WUtilities.enclosingComponentOfClass(sender, ERD2WInspectPage.class);
		EOEditingContext ec = eo != null?eo.editingContext():null;
		if (ec != null && page.shouldRevertChanges()) {
            ec.revert();
        }
        return page.nextPage(false);
	}
}
