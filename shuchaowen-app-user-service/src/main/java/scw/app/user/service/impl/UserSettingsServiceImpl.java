package scw.app.user.service.impl;

import scw.app.common.BaseServiceImpl;
import scw.app.user.pojo.UserSettings;
import scw.app.user.service.UserSettingsService;
import scw.core.instance.annotation.Configuration;
import scw.db.DB;
import scw.result.ResultFactory;

@Configuration(order=Integer.MIN_VALUE)
public class UserSettingsServiceImpl extends BaseServiceImpl implements UserSettingsService{
	
	public UserSettingsServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
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
