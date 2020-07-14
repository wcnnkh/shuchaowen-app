package scw.app.user.service;

import java.util.List;

import scw.app.user.pojo.UserRelationship;
import scw.result.Result;

public interface UserRelationshipService {
	UserRelationship getUserRelationship(long uid, int type, long toUid);

	/**
	 * 创建关系
	 * 
	 * @param uid
	 * @param type
	 * @param toUid
	 * @param mutual
	 *            是否是相互的关系
	 * @return
	 */
	Result create(long uid, int type, long toUid, boolean mutual);

	/**
	 * 删除关系
	 * 
	 * @param uid
	 * @param type
	 * @param toUid
	 * @param mutual
	 *            是否是相互的
	 * @return
	 */
	Result delete(long uid, int type, long toUid, boolean mutual);

	List<UserRelationship> getUserRelationshipList(long uid, int type);
}
