package scw.app.user.service;

import scw.app.user.pojo.UserSettings;
import scw.app.user.security.LoginRequired;
import scw.mvc.annotation.Controller;
import scw.mvc.annotation.ResultFactory;

@Controller(value="user/settings")
@LoginRequired
@ResultFactory
public interface UserSettingsService {
	@Controller(value="info")
	UserSettings getUserSettings(long uid);
	
	@Controller(value="update")
	UserSettings saveOrUpdate(long uid, String settings);
}
