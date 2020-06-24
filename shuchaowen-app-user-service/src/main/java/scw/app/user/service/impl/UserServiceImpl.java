package scw.app.user.service.impl;

import scw.app.common.BaseServiceImpl;
import scw.app.user.enums.UnionIdType;
import scw.app.user.enums.VerificationCodeType;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.UnionId;
import scw.app.user.pojo.User;
import scw.app.user.service.UserService;
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

@Configuration(value = UserService.class)
public class UserServiceImpl extends BaseServiceImpl implements UserService {
	@Autowired(required = false)
	private VerificationCodeService verificationCodeService;

	public UserServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
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
			UserAttributeModel userAttribute) {
		if (unionIdType == null || StringUtils.isEmpty(unionId)) {
			return resultFactory.error("参数错误");
		}

		UnionId uuid = getUnionId(unionIdType, unionId);
		if (uuid != null) {
			return resultFactory.error("账号已存在");
		}

		User user = new User();
		user.setCts(System.currentTimeMillis());
		if (StringUtils.isNotEmpty(password)) {
			user.setPassword(SignatureUtils.md5(password, Constants.DEFAULT_CHARSET_NAME));
		}

		if (userAttribute != null) {
			Copy.copy(user, userAttribute);
		}

		user.putUnionId(unionIdType, unionId);
		db.save(user);

		uuid = new UnionId();
		uuid.setUnionId(unionId);
		uuid.setUid(user.getUid());
		db.save(uuid);
		return resultFactory.success(user);
	}

	public Result register(UnionIdType unionIdType, String unionId, String password, String code,
			UserAttributeModel userAttributeModel) {
		if (verificationCodeService == null) {
			throw new NotSupportedException("不支持验证码注册");
		}

		Result result = verificationCodeService.checkVerificationCode(unionId, unionIdType,
				VerificationCodeType.REGISTER);
		if (result.isError()) {
			return result;
		}

		return register(unionIdType, unionId, password, userAttributeModel).result();
	}

	public DataResult<User> bind(long uid, UnionIdType unionIdType, String unionId) {
		if (unionIdType == null || StringUtils.isEmpty(unionId)) {
			return resultFactory.error("参数错误");
		}

		UnionId uuid = getUnionId(unionIdType, unionId);
		if (uuid != null) {
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
}
