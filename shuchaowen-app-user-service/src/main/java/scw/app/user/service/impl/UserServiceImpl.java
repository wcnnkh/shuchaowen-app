package scw.app.user.service.impl;

import java.util.Collections;

import scw.app.user.pojo.UnionId;
import scw.app.user.pojo.User;
import scw.app.user.pojo.enums.UnionIdType;
import scw.app.user.service.UserService;
import scw.core.utils.StringUtils;
import scw.db.DB;
import scw.result.DataResult;
import scw.result.ResultFactory;

public class UserServiceImpl extends BaseServiceImpl implements UserService {

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

	public DataResult<User> register(UnionIdType unionIdType, String unionId, String password) {
		if (unionIdType == null || StringUtils.isEmpty(unionId)) {
			return resultFactory.error("参数错误");
		}

		UnionId uid = getUnionId(unionIdType, unionId);
		if (uid != null) {
			return resultFactory.error("账号已存在");
		}

		User user = new User();
		user.setPassword(password);
		user.setUnionIdMap(Collections.singletonMap(unionIdType, unionId));
		user.setCts(System.currentTimeMillis());
		db.save(user);

		uid = new UnionId();
		uid.setUnionId(unionId);
		uid.setUid(user.getUid());
		db.save(uid);
		return resultFactory.success(user);
	}

}
