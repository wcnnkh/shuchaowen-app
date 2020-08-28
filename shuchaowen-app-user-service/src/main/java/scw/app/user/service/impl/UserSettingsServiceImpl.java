package scw.app.user.service.impl;

import scw.app.user.pojo.UserSettings;
import scw.app.user.service.UserSettingsService;
import scw.app.util.BaseServiceImpl;
import scw.core.instance.annotation.Configuration;
import scw.db.DB;
import scw.result.ResultFactory;

@Configuration(order=Integer.MIN_VALUE)
public class UserSettingsServiceImpl extends BaseServiceImpl implements UserSettingsService{
	
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
