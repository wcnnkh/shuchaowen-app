package scw.app.common.service;

import scw.app.common.pojo.UserEveryDayTouch;

/**
 * 对于每天进行的事情次数标记
 * @author shuchaowen
 *
 */
public interface UserEveryDayTouchService {
	UserEveryDayTouch getUserEveryDayTouch(long uid, int type);

	boolean canTouch(long uid, int type);
	
	boolean canTouch(long uid, int type, int maxCount);
	
	boolean touch(long uid, int type);
	
	boolean touch(long uid, int type, int maxCount);
}
