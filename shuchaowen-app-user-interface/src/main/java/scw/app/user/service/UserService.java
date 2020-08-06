package scw.app.user.service;

import scw.app.user.enums.OpenidType;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.User;
import scw.core.GlobalPropertyFactory;
import scw.http.HttpMethod;
import scw.mvc.annotation.Controller;
import scw.result.DataResult;
import scw.result.Result;
import scw.security.login.UserToken;
import scw.util.Pagination;

@Controller(value = "user", methods = { HttpMethod.POST, HttpMethod.GET })
public interface UserService {
	public static String DEFAULT_ADMIN_NAME = GlobalPropertyFactory.getInstance().getValue("scw.admin.username",
			String.class, "admin");
	public static String DEFAULT_PASSWORD = GlobalPropertyFactory.getInstance().getValue("scw.admin.password",
			String.class, "123456");
	public static String DEFAULT_NICKNAME = GlobalPropertyFactory.getInstance().getValue("scw.admin.nickname",
			String.class, "超级管理员");

	Pagination<User> getPagination(int permissionGroupId, String username, String nickname, int page, int limit);

	User getUser(long uid);

	User getUserByUsername(String username);

	User getUserByPhone(String phone);

	User getUserByOpenid(OpenidType type, String openid);

	Result checkPassword(long uid, String password);

	DataResult<User> registerByUsername(String username, String password, UserAttributeModel userAttributeModel);

	DataResult<User> registerByPhone(String phone, String password, UserAttributeModel userAttributeModel);

	DataResult<User> registerByOpenid(OpenidType type, String openid, UserAttributeModel userAttributeModel);

	DataResult<User> bindPhone(long uid, String phone);

	DataResult<User> bindOpenid(long uid, OpenidType type, String openid, UserAttributeModel userAttributeModel);

	Result updateUserAttribute(long uid, UserAttributeModel userAttributeModel);

	UserToken<Long> login(long uid);

	boolean cancelLogin(String token);
}
