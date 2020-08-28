package scw.app.common.service.impl;

import java.util.List;

import scw.app.common.pojo.UserRelationship;
import scw.app.common.service.UserRelationshipService;
import scw.app.util.BaseServiceConfiguration;
import scw.core.instance.annotation.Configuration;
import scw.db.DB;
import scw.result.Result;
import scw.result.ResultFactory;

@Configuration(order = Integer.MIN_VALUE)
public class UserRelationshipServiceImpl extends BaseServiceConfiguration implements UserRelationshipService {

	public UserRelationshipServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
		db.createTable(UserRelationship.class, false);
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
