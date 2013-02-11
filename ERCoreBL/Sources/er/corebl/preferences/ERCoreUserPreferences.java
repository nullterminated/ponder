package er.corebl.preferences;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

import com.webobjects.directtoweb.D2WModel;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOKeyValueArchiver;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSPropertyListSerialization;
import com.webobjects.foundation.NSSelector;

import er.corebl.ERCoreBL;
import er.corebl.model.ERCPreference;
import er.extensions.batching.ERXBatchNavigationBar;
import er.extensions.components.ERXSortOrder;
import er.extensions.eof.ERXConstant;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXEOControlUtilities;
import er.extensions.foundation.ERXProperties;
import er.extensions.foundation.ERXRetainer;

public enum ERCoreUserPreferences implements NSKeyValueCoding {
	INSTANCE;

	/** Notification that is posted when preferences change */
	public final static String PreferenceDidChangeNotification = "PreferenceChangedNotification";

	private static final Logger log = Logger.getLogger(ERCoreUserPreferences.class);

	/** EOEncoding key */
	private static final String VALUE = "_V";

	public static class _UserPreferenceHandler {
		public _UserPreferenceHandler() {
			NSNotificationCenter.defaultCenter().addObserver(this,
					new NSSelector<Void>("handleBatchSizeChange", ERXConstant.NotificationClassArray),
					ERXBatchNavigationBar.BatchSizeChanged, null);
			NSNotificationCenter.defaultCenter().addObserver(this,
					new NSSelector<Void>("handleSortOrderingChange", ERXConstant.NotificationClassArray),
					ERXSortOrder.SortOrderingChanged, null);
		}

		public void handleBatchSizeChange(NSNotification n) {
			handleChange("batchSize", n);
		}

		public void handleSortOrderingChange(NSNotification n) {
			handleChange("sortOrdering", n);
		}

		public void handleChange(String prefName, NSNotification n) {
			if (ERCoreBL.actor() != null) {
				NSKeyValueCoding context = (NSKeyValueCoding) n.userInfo().objectForKey("d2wContext");
				if (context != null && context.valueForKey(D2WModel.DynamicPageKey) != null) {
					INSTANCE.takeValueForKey(n.object(),
							prefName + "." + (String) context.valueForKey(D2WModel.DynamicPageKey));
				}
			}
		}
	}

	/**
	 * Registers notification handlers for user preference notifications. These
	 * are mainly used within the context of D2W pages.
	 */
	public void registerHandlers() {
		log.debug("Registering preference handlers");
		Object handler = null;
		String handlerClassName = ERXProperties
				.stringForKey("er.corebusinesslogic.ERCoreUserPreferences.handlerClassName");
		if (handlerClassName != null) {
			try {
				handler = Class.forName(handlerClassName).newInstance();
			} catch (Exception e) {
				throw NSForwardException._runtimeExceptionForThrowable(e);
			}
		}
		if (handler == null) {
			handler = new _UserPreferenceHandler();
		}
		ERXRetainer.retain(handler);
	}

	// FIXME -- unarchiving - archiving probably could use optimization
	@Override
	public void takeValueForKey(Object value, String key) {
		/*
		 * we first make sure there is no cruft left !! locking is turned off on
		 * the value attribute of UserPreference so that if a user opens two
		 * sessions they don't get locking failures this is OK for display style
		 * prefs (how many items, how they are sorted) but might not be for more
		 * behavior-style prefs!!
		 */
		EOEditingContext ec = ERXEC.newEditingContext();
		ec.lock();
		try {
			ERCPreference pref = preferenceRecordForKey(key, ec);
			ERCoreUserInterface actor = (ERCoreUserInterface) ERCoreBL.actor(ec);
			if (pref != null) {
				if (value != null) {
					String encodedValue = encodedValue(value);
					if (ObjectUtils.notEqual(encodedValue, pref.prefValue())) {
						if (log.isDebugEnabled()) {
							log.debug("Updating preference " + actor + ": " + key + "=" + encodedValue);
						}
						pref.setPrefValue(encodedValue);
					}
				} else {
					if (log.isDebugEnabled()) {
						log.debug("Removing preference " + actor + ": " + key);
					}
					ec.deleteObject(pref);
				}
			} else if (value != null) {
				pref = ERCPreference.clazz.createAndInsertObject(ec);
				actor.newPreference(pref);
				// done this way to not force you to sub-class our User entity
				Object pk = ERXEOControlUtilities.primaryKeyObjectForObject((EOEnterpriseObject) actor);
				pref.takeValueForKey(pk, ERCPreference.USER_ID_KEY);
				pref.setPrefKey(key);
				String encodedValue = encodedValue(value);
				pref.setPrefValue(encodedValue);
				if (log.isDebugEnabled()) {
					log.debug("Creating preference " + actor + ": " + key + " - " + value + " -- " + encodedValue);
				}
			}
			if (ec.hasChanges()) {
				ec.saveChanges();
			}
		} catch (RuntimeException ex) {
			log.error("Error while setting preference " + key, ex);
		} finally {
			ec.unlock();
		}
		ec.dispose();
		NSNotificationCenter.defaultCenter().postNotification(PreferenceDidChangeNotification,
				new NSDictionary<String, Object>(value, key));
	}

	@Override
	public Object valueForKey(String key) {
		Object result = null;
		EOEditingContext ec = ERXEC.newEditingContext();
		ec.lock();
		try {
			ERCPreference pref = preferenceRecordForKey(key, ec);
			if (pref != null) {
				String encodedValue = pref.prefValue();
				if (encodedValue != null) {
					result = decodedValue(encodedValue);
				}
			}
		} catch (RuntimeException ex) {
			log.error("Error while getting preference " + key, ex);
		} finally {
			ec.unlock();
		}
		ec.dispose();
		if (log.isDebugEnabled()) {
			log.debug("Prefs vfk " + key + " = " + result);
		}
		return result;
	}

	private String encodedValue(Object value) {
		EOKeyValueArchiver archiver = new EOKeyValueArchiver();
		archiver.encodeObject(value, VALUE);
		String encodedValue = NSPropertyListSerialization.stringFromPropertyList(archiver.dictionary());
		return encodedValue;
	}

	private Object decodedValue(String encodedValue) {
		@SuppressWarnings("rawtypes")
		NSDictionary d = (NSDictionary) NSPropertyListSerialization.propertyListFromString(encodedValue);
		EOKeyValueUnarchiver u = new EOKeyValueUnarchiver(d);
		return u.decodeObjectForKey(VALUE);
	}

	private NSArray<ERCPreference> preferences(EOEditingContext ec) {
		ERCoreUserInterface user = (ERCoreUserInterface) ERCoreBL.actor(ec);
		NSArray<ERCPreference> prefs = NSArray.emptyArray();
		if (user != null) {
			prefs = user.preferences();
		}
		return prefs;
	}

	private ERCPreference preferenceRecordForKey(String key, EOEditingContext ec) {
		ERCPreference result = null;
		if (key != null) {
			if (log.isDebugEnabled()) {
				log.debug("Preference value for Key = " + key);
			}
			NSArray<ERCPreference> prefs = preferences(ec);
			result = ERCPreference.PREF_KEY.eq(key).filtered(prefs).lastObject();
		}
		return result;
	}
}
