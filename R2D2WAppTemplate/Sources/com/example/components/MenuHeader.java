package com.example.components;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORedirect;
import com.webobjects.directtoweb.D2W;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.directtoweb.EditPageInterface;
import com.webobjects.directtoweb.ErrorPageInterface;
import com.webobjects.directtoweb.InspectPageInterface;
import com.webobjects.directtoweb.QueryPageInterface;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;

import er.directtoweb.pages.ERD2WPage;
import er.extensions.eof.ERXEC;
import er.extensions.localization.ERXLocalizer;

public class MenuHeader extends WOComponent {
    private String _manipulatedEntityName;
	private String entityNameInList;

    public MenuHeader(WOContext aContext) {
        super(aContext);
    }

    public String manipulatedEntityName() {
        if (_manipulatedEntityName == null) {
            WOComponent currentPage = context().page();
            _manipulatedEntityName = D2W.entityNameFromPage(currentPage);
        }
        return _manipulatedEntityName;
    }

    public void setManipulatedEntityName(String newValue) {
        _manipulatedEntityName = newValue;
    }

    public NSArray<?> visibleEntityNames() {
        return D2W.factory().visibleEntityNames(session());
    }

    public WOComponent findEntityAction() {
        QueryPageInterface newQueryPage = D2W.factory().queryPageForEntityNamed(_manipulatedEntityName, session());
        return (WOComponent) newQueryPage;
    }

    public WOComponent newObjectAction() {
        WOComponent nextPage = null;
        try {
            EditPageInterface epi = D2W.factory().editPageForNewObjectWithEntityNamed(_manipulatedEntityName, session());
            epi.setNextPage(context().page());
            nextPage = (WOComponent) epi;
        } catch (IllegalArgumentException e) {
            ErrorPageInterface epf = D2W.factory().errorPage(session());
            epf.setMessage(e.toString());
            epf.setNextPage(context().page());
            nextPage = (WOComponent) epf;
        }
        return nextPage;
    }

    public WOComponent logout() {
        WOComponent redirectPage = pageWithName(WORedirect.class.getName());
        ((WORedirect) redirectPage).setUrl(D2W.factory().homeHrefInContext(context()));
        session().terminate();
        return redirectPage;
    }

	public WOActionResults inspectCurrentUser() {
        WOComponent result = null;
		try {
			EOEnterpriseObject user = (EOEnterpriseObject)session().objectForKey("user");
			InspectPageInterface ipi = D2W.factory().inspectPageForEntityNamed(user.classDescription().entityName(), session());
			ipi.setObject(user);
			ipi.setNextPage(context().page());
			result = (WOComponent) ipi;
		} catch (IllegalArgumentException e) {
            ErrorPageInterface epf = D2W.factory().errorPage(session());
            epf.setMessage(e.toString());
            epf.setNextPage(context().page());
            result = (WOComponent) epf;
        }
		return result;
	}

	public WOActionResults editCurrentUser() {
        WOComponent results = null;
        try {
			EOEnterpriseObject user = (EOEnterpriseObject)session().objectForKey("user");
            EditPageInterface epi = D2W.factory().editPageForEntityNamed(user.classDescription().entityName(), session());
            epi.setObject(user);
            epi.setNextPage(context().page());
            results = (WOComponent) epi;
        } catch (IllegalArgumentException e) {
            ErrorPageInterface epf = D2W.factory().errorPage(session());
            epf.setMessage(e.toString());
            epf.setNextPage(context().page());
            results = (WOComponent) epf;
        }
        return results;
	}

	public WOActionResults loginAction() {
		ERD2WPage page = (ERD2WPage)D2W.factory().pageForConfigurationNamed("UserLogin", session());
		D2WContext c = page.d2wContext();
		page.setNextPage(context().page());
		if(c.task().equals("error")) {
			String message = ERXLocalizer.currentLocalizer().localizedStringForKey("R2D2W.noUserEntityMessage");
			page.takeValueForKey(message, "message");
		}
		return page;
	}

	public WOActionResults createUserAction() {
		ERD2WPage page = (ERD2WPage)D2W.factory().pageForConfigurationNamed("UserCreation", session());
		D2WContext c = page.d2wContext();
		page.setNextPage(context().page());
		if(c.task().equals("error")) {
			String message = ERXLocalizer.currentLocalizer().localizedStringForKey("R2D2W.noUserEntityMessage");
			page.takeValueForKey(message, "message");
		} else {
			EOEntity entity = c.entity();
			EOEditingContext ec = ERXEC.newEditingContext();
			EOEnterpriseObject eo = EOUtilities.createAndInsertInstance(ec, entity.name());
			page.setObject(eo);
		}
		return page;
	}

	public WOActionResults passwordReminderAction() {
		ERD2WPage page = (ERD2WPage)D2W.factory().pageForConfigurationNamed("UserReset", session());
		D2WContext c = page.d2wContext();
		page.setNextPage(context().page());
		if(c.task().equals("error")) {
			String message = ERXLocalizer.currentLocalizer().localizedStringForKey("R2D2W.noUserEntityMessage");
			page.takeValueForKey(message, "message");
		}
		return page;
	}

	public WOActionResults updateLanguageAction() {
		return context().page();
	}

	public Boolean hasMultipleLanguages() {
		return (ERXLocalizer.availableLanguages().count() > 1);
	}

	/**
	 * @return the entityNameInList
	 */
	public String entityNameInList() {
		return entityNameInList;
	}

	/**
	 * @param entityNameInList the entityNameInList to set
	 */
	public void setEntityNameInList(String entityNameInList) {
		this.entityNameInList = entityNameInList;
	}

	public String displayStringForEntity() {
		String result = entityNameInList();
		ERXLocalizer loc = ERXLocalizer.currentLocalizer();
		String locKey = "Entity.name." + result;
		if(loc.valueForKey(locKey) != null) {
			result = loc.localizedStringForKey(locKey);
		}
		return result;
	}

	public WOActionResults queryAnyAction() {
		return D2W.factory().defaultPage(session());
	}

	public String queryAnyString() {
		return ERXLocalizer.currentLocalizer().localizedStringForKey("R2D2W.queryAllButtonLabel");
	}

	private static final NSArray<String> availableTimeZones = new NSArray<String>(new String[] {
			 "US/Hawaii", "US/Alaska", "US/Pacific", "US/Mountain", "US/Central", "US/Eastern", "GMT", "Asia/Tokyo"
	});

	/**
	 * @return the availableTimeZones
	 */
	public NSArray<String> availableTimeZones() {
		return availableTimeZones;
	}

}
