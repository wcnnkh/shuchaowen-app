package scw.app.user.controller;

import scw.app.user.pojo.UserSettings;
import scw.app.user.security.LoginRequired;
import scw.app.user.service.UserSettingsService;
import scw.beans.annotation.Autowired;
import scw.mvc.annotation.Controller;
import scw.mvc.annotation.ResultFactory;

@Controller(value = "/user/settings")
@LoginRequired
@ResultFactory
public class UserSettingsController {
	@Autowired
	private UserSettingsService userSettingsService;

	@Controller(value = "info")
	public UserSettings getUserSettings(long uid) {
		return userSettingsService.getUserSettings(uid);
	}

	@Controller(value = "update")
	public UserSettings saveOrUpdate(long uid, String settings) {
		return userSettingsService.saveOrUpdate(uid, settings);
	}
}
