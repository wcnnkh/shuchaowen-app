package scw.app.common.service;

import scw.app.common.model.UserSignInLog;
import scw.app.common.pojo.UserSignIn;
import scw.result.Result;
import scw.util.Pagination;

/**
 * 签到
 * @author shuchaowen
 *
 */
public interface UserSignInService {
	/**
	 * 签到/补签
	 * @param uid
	 * @param type 签到类型
	 * @param time 签到时间(可能是补签)
	 * @param currentTimeMillis 当前时间 {@see System.currentTimeMillis()}
	 * @return
	 */
	Result signIn(long uid, int type, long time, long currentTimeMillis);
	
	/**
	 * 获取签到信息
	 * @param uid
	 * @param type
	 * @return
	 */
	UserSignIn getUserSignIn(long uid, int type);
	
	/**
	 * 获取用户签到详情
	 * @param uid
	 * @param type
	 * @param beginTime
	 * @param endTime
	 * @param page
	 * @param limit
	 * @return
	 */
	Pagination<? extends UserSignInLog> getPagination(long uid, int type, long beginTime, long endTime, int page, int limit);
}
