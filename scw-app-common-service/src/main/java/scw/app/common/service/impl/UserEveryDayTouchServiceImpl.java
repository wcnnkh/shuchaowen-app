package scw.app.common.service.impl;

import scw.app.common.pojo.UserEveryDayTouch;
import scw.app.common.service.UserEveryDayTouchService;
import scw.app.util.BaseServiceConfiguration;
import scw.beans.annotation.Service;
import scw.context.result.ResultFactory;
import scw.db.DB;

@Service
public class UserEveryDayTouchServiceImpl extends BaseServiceConfiguration implements UserEveryDayTouchService {

	public UserEveryDayTouchServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
		db.createTable(UserEveryDayTouch.class, false);
	}

	public UserEveryDayTouch getUserEveryDayTouch(long uid, int type) {
		return db.getById(UserEveryDayTouch.class, uid, type);
	}

	public final boolean touch(long uid, int type) {
		return touch(uid, type, 1);
	}

	public boolean touch(long uid, int type, int maxCount) {
		UserEveryDayTouch touch = getUserEveryDayTouch(uid, type);
		if (touch == null) {
			touch = new UserEveryDayTouch();
			touch.setUid(uid);
			touch.setType(type);
			touch.setLastTouchTime(System.currentTimeMillis());
			touch.setCount(1);
			return db.save(touch);
		} else {
			if (touch.isCanTouch(maxCount)) {
				touch.setLastTouchTime(System.currentTimeMillis());
				touch.setCount(touch.getCount() + 1);
				return db.update(touch);
			}
			return false;
		}
	}

	public final boolean canTouch(long uid, int type) {
		return canTouch(uid, type, 1);
	}

	public boolean canTouch(long uid, int type, int maxCount) {
		UserEveryDayTouch touch = getUserEveryDayTouch(uid, type);
		if (touch == null) {
			return true;
		}

		return touch.isCanTouch(maxCount);
	}

}
