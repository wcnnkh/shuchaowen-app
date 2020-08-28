package scw.app.common.model;

public interface UserSignInLog {
	long getUid();
	
	/**
	 * 签到时间
	 * @return
	 */
	long getSignInTime();
	
	/**
	 * 如果是补签那么就是补签时的时间
	 * @return
	 */
	long getCreateTime();
	
	/**
	 * 是否是补签
	 * @return
	 */
	boolean isSupplementary();
}
