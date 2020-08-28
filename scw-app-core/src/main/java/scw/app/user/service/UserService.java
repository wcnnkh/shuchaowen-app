package scw.app.user.service;

import java.util.Collection;

import scw.app.user.enums.OpenidType;
import scw.app.user.model.AdminUserModel;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.User;
import scw.http.HttpMethod;
import scw.mvc.annotation.Controller;
import scw.result.DataResult;
import scw.result.Result;
import scw.util.Pagination;

@Controller(value = "user", methods = { HttpMethod.POST, HttpMethod.GET })
public interface UserService {
	/**
	 * 搜索permissionGroupId>0的用户
	 * @param permissionGroupIds
	 * @param search
	 * @param page
	 * @param limit
	 * @return
	 */
	Pagination<User> search(Collection<Integer> permissionGroupIds, String search, int page, int limit);

	User getUser(long uid);

	/**
	 * 是否是超级管理员
	 * 
	 * @param uid
	 * @return
	 */
	boolean isSuperAdmin(long uid);

	User getUserByUsername(String username);

	User getUserByPhone(String phone);

	User getUserByOpenid(OpenidType type, String openid);

	Result checkPassword(long uid, String password);

	Result updatePassword(long uid, String password);

	DataResult<User> createOrUpdateAdminUser(long uid, AdminUserModel adminUserModel);

	DataResult<User> registerByUsername(String username, String password, UserAttributeModel userAttributeModel);

	DataResult<User> registerByPhone(String phone, String password, UserAttributeModel userAttributeModel);

	DataResult<User> registerByOpenid(OpenidType type, String openid, UserAttributeModel userAttributeModel);

	DataResult<User> bindPhone(long uid, String phone);

	DataResult<User> bindOpenid(long uid, OpenidType type, String openid, UserAttributeModel userAttributeModel);

	Result updateUserAttribute(long uid, UserAttributeModel userAttributeModel);
}
