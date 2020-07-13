package scw.app.user.service.impl;

import scw.app.common.BaseServiceImpl;
import scw.app.user.enums.UnionIdType;
import scw.app.user.enums.VerificationCodeType;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.UnionId;
import scw.app.user.pojo.User;
import scw.app.user.service.UserService;
import scw.app.user.service.VerificationCodeService;
import scw.beans.annotation.Autowired;
import scw.core.Constants;
import scw.core.instance.annotation.Configuration;
import scw.core.utils.StringUtils;
import scw.db.DB;
import scw.lang.NotSupportedException;
import scw.mapper.Copy;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.security.SignatureUtils;
import scw.security.login.LoginService;
import scw.security.login.UserToken;

@Configuration(order = Integer.MIN_VALUE)
public class UserServiceImpl extends BaseServiceImpl implements UserService {

	@Autowired(required = false)
	private VerificationCodeService verificationCodeService;
	private LoginService<Long> loginService;

	public UserServiceImpl(DB db, ResultFactory resultFactory, LoginService<Long> loginService) {
		super(db, resultFactory);
		this.loginService = loginService;
	}

	public User getUser(long uid) {
		return db.getById(User.class, uid);
	}

	public User getUser(UnionIdType unionIdType, String unionId) {
		UnionId union = getUnionId(unionIdType, unionId);
		return union == null ? null : getUser(union.getUid());
	}

	public UnionId getUnionId(UnionIdType unionIdType, String unionId) {
		return db.getById(UnionId.class, unionIdType, unionId);
	}

	public DataResult<User> register(UnionIdType unionIdType, String unionId, String password,
			UserAttributeModel userAttributeModel) {
		return register(0, unionIdType, unionId, password, userAttributeModel);
	}

	public Result register(UnionIdType unionIdType, String unionId, String password, String code,
			UserAttributeModel userAttributeModel) {
		return register(0, unionIdType, unionId, password, code, userAttributeModel);
	}

	public DataResult<User> bind(long uid, UnionIdType unionIdType, String unionId) {
		if (unionIdType == null || StringUtils.isEmpty(unionId)) {
			return resultFactory.error("参数错误");
		}

		UnionId uuid = getUnionId(unionIdType, unionId);
		//如果账号已经存在且绑定者不是自己那就说明账号已经被别人绑定了
		if (uuid != null && uuid.getUid() != uid) {
			return resultFactory.error("账号已存在");
		}

		User user = getUser(uid);
		if (user == null) {
			return resultFactory.error("用户不存在(1)");
		}

		user.putUnionId(unionIdType, unionId);
		db.update(user);

		uuid = new UnionId();
		uuid.setUnionId(unionId);
		uuid.setUid(user.getUid());
		db.save(uid);
		return resultFactory.success(user);
	}

	public Result bind(long uid, UnionIdType unionIdType, String unionId, String code) {
		if (verificationCodeService == null) {
			throw new NotSupportedException("不支持验证码绑定");
		}

		Result result = verificationCodeService.checkVerificationCode(unionId, unionIdType, VerificationCodeType.BIND);
		if (result.isError()) {
			return result;
		}

		return bind(uid, unionIdType, unionId).result();
	}

	public Result updateUserAttribute(long uid, UserAttributeModel userAttributeModel) {
		User user = getUser(uid);
		if (user == null) {
			return resultFactory.error("用户不存在");
		}

		if (userAttributeModel != null) {
			if (userAttributeModel.getBirthday() != null) {
				user.setBirthday(userAttributeModel.getBirthday());
			}

			if (userAttributeModel.getSex() != null) {
				user.setSex(userAttributeModel.getSex());
			}

			if (userAttributeModel.getAge() != 0 && userAttributeModel.getAge() != user.getAge()) {
				user.setAge(userAttributeModel.getAge());
			}
			db.save(userAttributeModel);
		}

		return resultFactory.success();
	}

	public DataResult<User> register(long uid, UnionIdType unionIdType, String unionId, String password,
			UserAttributeModel userAttributeModel) {
		if (unionIdType == null || StringUtils.isEmpty(unionId)) {
			return resultFactory.error("参数错误");
		}

		User user = getUser(uid);
		if (user != null) {
			return resultFactory.error("已经注册过了");
		}

		UnionId uuid = getUnionId(unionIdType, unionId);
		if (uuid != null) {
			return resultFactory.error("账号已存在");
		}

		user = new User();
		user.setUid(uid);
		user.setCts(System.currentTimeMillis());
		if (StringUtils.isNotEmpty(password)) {
			user.setPassword(SignatureUtils.md5(password, Constants.DEFAULT_CHARSET_NAME));
		}

		if (userAttributeModel != null) {
			Copy.copy(user, userAttributeModel);
		}

		user.putUnionId(unionIdType, unionId);
		db.save(user);

		uuid = new UnionId();
		uuid.setUnionIdType(unionIdType);
		uuid.setUnionId(unionId);
		uuid.setUid(user.getUid());
		db.save(uuid);
		return resultFactory.success(user);
	}

	public Result register(long uid, UnionIdType unionIdType, String unionId, String password, String code,
			UserAttributeModel userAttributeModel) {
		if (verificationCodeService == null) {
			throw new NotSupportedException("不支持验证码注册");
		}

		Result result = verificationCodeService.checkVerificationCode(unionId, unionIdType,
				VerificationCodeType.REGISTER);
		if (result.isError()) {
			return result;
		}

		return register(uid, unionIdType, unionId, password, userAttributeModel).result();
	}

	public DataResult<UserToken<Long>> login(UnionIdType unionIdType, String unionId, String code) {
		if (verificationCodeService == null) {
			throw new NotSupportedException("不支持验证码登录");
		}

		UnionId uid = getUnionId(unionIdType, unionId);
		if (uid == null) {
			return resultFactory.error("用户不存在");
		}

		Result result = verificationCodeService.checkVerificationCode(unionId, unionIdType, VerificationCodeType.LOGIN);
		if (result.isError()) {
			return result.dataResult();
		}

		UserToken<Long> userToken = loginService.login(uid.getUid());
		return resultFactory.success(userToken);
	}

	public DataResult<UserToken<Long>> pwdLogin(UnionIdType unionIdType, String unionId, String password) {
		User user = getUser(unionIdType, unionId);
		if (user == null) {
			return resultFactory.error("账号不存在");
		}

		if (StringUtils.isEmpty(user.getPassword()) || StringUtils.isEmpty(password)) {
			return resultFactory.error("账号或密码错误(1)");
		}

		if (!user.getPassword().equals(SignatureUtils.md5(password, Constants.DEFAULT_CHARSET_NAME))) {
			return resultFactory.error("账号或密码错误(2)");
		}

		return resultFactory.success(loginService.login(user.getUid()));
	}
}
