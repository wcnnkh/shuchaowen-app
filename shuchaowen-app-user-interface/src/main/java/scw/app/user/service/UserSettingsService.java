package scw.app.user.service;

import scw.app.common.annotation.LoginUse;
import scw.app.user.pojo.UserSettings;
import scw.mvc.annotation.Controller;
import scw.mvc.annotation.ResultFactory;

@Controller(value="user/settings")
@LoginUse
@ResultFactory
public interface UserSettingsService {
	@Controller(value="info")
	UserSettings getUserSettings(long uid);
	
	@Controller(value="update")
	UserSettings saveOrUpdate(long uid, String settings);
}
