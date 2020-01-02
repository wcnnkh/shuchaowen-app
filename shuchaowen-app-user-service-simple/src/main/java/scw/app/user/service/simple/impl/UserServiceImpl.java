package scw.app.user.service.simple.impl;

import scw.app.user.pojo.UnionId;
import scw.app.user.pojo.User;
import scw.app.user.pojo.enums.UnionIdType;
import scw.app.user.service.UserService;
import scw.db.DB;

public class UserServiceImpl extends BaseServiceImpl implements UserService {

	public UserServiceImpl(DB db) {
		super(db);
	}

	public User getUser(long uid) {
		return db.getById(User.class, uid);
	}

	public User getUser(UnionIdType unionIdType, String unionId) {
		UnionId union = getUnionId(unionIdType, unionId);
		return union == null ? null : getUser(union.getUid());
	}

	public User getUser(String unionIdType, String unionId) {
		UnionId union = getUnionId(unionIdType, unionId);
		return union == null ? null : getUser(union.getUid());
	}

	public UnionId getUnionId(UnionIdType unionIdType, String unionId) {
		return db.getById(UnionId.class, unionIdType, unionId);
	}

	public UnionId getUnionId(String unionIdType, String unionId) {
		return db.getById(UnionId.class, unionIdType, unionId);
	}

}
