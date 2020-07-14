package scw.app.user.service.impl;

import java.util.List;

import scw.app.common.BaseServiceImpl;
import scw.app.user.pojo.UserRelationship;
import scw.app.user.service.UserRelationshipService;
import scw.db.DB;
import scw.result.Result;
import scw.result.ResultFactory;

public class UserRelationshipServiceImpl extends BaseServiceImpl implements UserRelationshipService {

	public UserRelationshipServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
	}

	public Result create(long uid, int type, long toUid, boolean mutual) {
		UserRelationship userRelationship = getUserRelationship(uid, type, toUid);
		if (userRelationship != null) {
			return resultFactory.error("已经存在相互关系了(1)");
		}

		if (mutual && getUserRelationship(toUid, type, uid) != null) {
			return resultFactory.error("已经存在相互关系了(2)");
		}

		userRelationship = new UserRelationship();
		userRelationship.setUid(uid);
		userRelationship.setToUid(toUid);
		userRelationship.setType(type);
		userRelationship.setCts(System.currentTimeMillis());
		db.save(userRelationship);

		if (mutual) {
			userRelationship.setUid(toUid);
			userRelationship.setToUid(uid);
			db.save(userRelationship);
		}
		return resultFactory.success();
	}

	public Result delete(long uid, int type, long toUid, boolean mutual) {
		UserRelationship userRelationship = getUserRelationship(uid, type, toUid);
		if (userRelationship == null) {
			return resultFactory.error("不存在相互关系(1)");
		}

		if (mutual && getUserRelationship(toUid, type, uid) == null) {
			return resultFactory.error("不存在相互关系(2)");
		}

		db.deleteById(UserRelationship.class, uid, type, toUid);

		if (mutual) {
			db.deleteById(UserRelationship.class, toUid, type, uid);
		}

		return resultFactory.success();
	}

	public List<UserRelationship> getUserRelationshipList(long uid, int type) {
		return db.getByIdList(UserRelationship.class, uid, type);
	}

	public UserRelationship getUserRelationship(long uid, int type, long toUid) {
		return db.getById(UserRelationship.class, uid, type, toUid);
	}

}
