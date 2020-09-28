package scw.app.common.service;

import java.util.List;

import scw.app.common.pojo.UserMembershipCard;
import scw.app.enums.TimeCycle;

public interface UserMembershipCardService {
	UserMembershipCard getUserMembershipCard(String id);

	UserMembershipCard getCurrentUserMembershipCard(long uid);

	/**
	 * 获取用户所有的会员卡
	 * @param uid
	 * @param available 是否可用，如果为空就返回所有
	 * @return
	 */
	List<UserMembershipCard> getUserMembershipCardList(long uid, Boolean available);

	/**
	 * 添加会员卡
	 * 
	 * @param uid
	 * @param type
	 * @param timeCycle
	 *            会员卡时间周期
	 * @param timeCycleCount
	 *            几个周期
	 */
	void addUserMembershipCard(long uid, int type, TimeCycle timeCycle, int timeCycleCount);
}
