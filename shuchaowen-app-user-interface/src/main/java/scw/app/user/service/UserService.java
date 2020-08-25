package scw.app.user.service;

import scw.app.user.enums.OpenidType;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.User;
import scw.http.HttpMethod;
import scw.mvc.annotation.Controller;
import scw.result.DataResult;
import scw.result.Result;
import scw.util.Pagination;

@Controller(value = "user", methods = { HttpMethod.POST, HttpMethod.GET })
public interface UserService {
	Pagination<User> getPagination(int permissionGroupId, String username, String nickname, int page, int limit);

	User getUser(long uid);
	
	/**
	 * 是否是超级管理员
	 * @param uid
	 * @return
	 */
	boolean isSuperAdmin(long uid);

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
}
