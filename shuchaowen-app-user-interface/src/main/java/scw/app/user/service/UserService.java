package scw.app.user.service;

import scw.app.user.enums.UnionIdType;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.UnionId;
import scw.app.user.pojo.User;
import scw.http.HttpMethod;
import scw.mvc.annotation.Controller;
import scw.result.DataResult;
import scw.result.Result;
import scw.security.login.UserToken;

@Controller(value = "user", methods = { HttpMethod.POST, HttpMethod.GET })
public interface UserService {
	User getUser(long uid);

	UnionId getUnionId(UnionIdType unionIdType, String unionId);

	User getUser(UnionIdType unionIdType, String unionId);

	DataResult<User> register(UnionIdType unionIdType, String unionId, String password,
			UserAttributeModel userAttributeModel);

	/**
	 * 注册
	 * 
	 * @param unionIdType
	 * @param unionId
	 * @param password
	 * @return
	 */
	DataResult<User> register(long uid, UnionIdType unionIdType, String unionId, String password,
			UserAttributeModel userAttributeModel);

	DataResult<User> bind(long uid, UnionIdType unionIdType, String unionId);

	Result bind(long uid, UnionIdType unionIdType, String unionId, String code);

	Result register(UnionIdType unionIdType, String unionId, String password, String code,
			UserAttributeModel userAttributeModel);

	/**
	 * 以验证码方式注册
	 * 
	 * @param unionIdType
	 * @param unionId
	 * @param password
	 * @param code
	 * @return
	 */
	Result register(long uid, UnionIdType unionIdType, String unionId, String password, String code,
			UserAttributeModel userAttributeModel);

	/**
	 * 修改用户其他属性
	 * 
	 * @param uid
	 * @param userAttributeModel
	 * @return
	 */
	Result updateUserAttribute(long uid, UserAttributeModel userAttributeModel);

	@Controller(value = "login")
	DataResult<UserToken<Long>> login(UnionIdType unionIdType, String unionId, String code);
	
	@Controller(value="pwd_login")
	DataResult<UserToken<Long>> pwdLogin(UnionIdType unionIdType, String unionId, String password);
}
