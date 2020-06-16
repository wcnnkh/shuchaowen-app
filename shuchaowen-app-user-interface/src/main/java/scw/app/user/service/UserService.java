package scw.app.user.service;

import scw.app.user.pojo.UnionId;
import scw.app.user.pojo.User;
import scw.app.user.pojo.enums.UnionIdType;
import scw.event.method.annotation.PublishEvent;
import scw.http.HttpMethod;
import scw.mvc.annotation.Controller;
import scw.result.DataResult;
import scw.result.Result;

@Controller(value = "user", methods = HttpMethod.POST)
public interface UserService {
	/**
	 * 注册成功事件
	 */
	public static final String REGISTER_EVENT_NAME = "scw,user.register.success";

	/**
	 * 绑定成功事件
	 */
	public static final String BIND_EVENT_NAME = "scw.user.bind.success";

	User getUser(long uid);

	UnionId getUnionId(UnionIdType unionIdType, String unionId);

	User getUser(UnionIdType unionIdType, String unionId);

	/**
	 * 注册
	 * 
	 * @param unionIdType
	 * @param unionId
	 * @param password
	 * @return
	 */
	@PublishEvent(REGISTER_EVENT_NAME)
	DataResult<User> register(UnionIdType unionIdType, String unionId, String password);

	@PublishEvent(BIND_EVENT_NAME)
	DataResult<User> bind(long uid, UnionIdType unionIdType, String unionId);

	@Controller(value = "bind")
	Result bind(long uid, UnionIdType unionIdType, String unionId, String code);

	/**
	 * 以验证码方式注册
	 * 
	 * @param unionIdType
	 * @param unionId
	 * @param password
	 * @param code
	 * @return
	 */
	@Controller(value = "register")
	Result register(UnionIdType unionIdType, String unionId, String password, String code);
}
