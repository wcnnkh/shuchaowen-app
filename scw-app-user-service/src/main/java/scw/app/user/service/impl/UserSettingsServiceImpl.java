package scw.app.user.service.impl;

import scw.app.user.pojo.UserSettings;
import scw.app.user.service.UserSettingsService;
import scw.app.util.BaseServiceConfiguration;
import scw.beans.annotation.Service;
import scw.context.result.ResultFactory;
import scw.db.DB;

@Service
public class UserSettingsServiceImpl extends BaseServiceConfiguration implements UserSettingsService{
	
	public UserSettingsServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
		db.createTable(UserSettings.class, false);
	}

	public UserSettings getUserSettings(long uid) {
		return db.getById(UserSettings.class, uid);
	}

	public UserSettings saveOrUpdate(long uid, String settings) {
		UserSettings userSettings = getUserSettings(uid);
		if(userSettings == null){
			userSettings = new UserSettings();
			userSettings.setUid(uid);
		}
		
		userSettings.setSettings(settings);
		userSettings.setLastUpdateTime(System.currentTimeMillis());
		db.saveOrUpdate(userSettings);
		return userSettings;
	}

}
