package scw.app.user.service;

import scw.app.user.pojo.UserSettings;

public interface UserSettingsService {
	UserSettings getUserSettings(long uid);

	UserSettings saveOrUpdate(long uid, String settings);
}
