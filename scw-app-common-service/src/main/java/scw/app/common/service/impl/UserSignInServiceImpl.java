package scw.app.common.service.impl;

import java.util.Calendar;

import scw.app.common.pojo.UserSignIn;
import scw.app.common.pojo.UserSignInLogTable;
import scw.app.common.service.UserSignInService;
import scw.app.enums.ContinuityStatus;
import scw.app.enums.ContinuityStatus.ContinuityCycle;
import scw.app.util.BaseServiceConfiguration;
import scw.core.IteratorCallback;
import scw.core.instance.annotation.Configuration;
import scw.db.DB;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.sql.SimpleSql;
import scw.sql.Sql;
import scw.util.CalendarUtils;
import scw.util.KeyValuePair;
import scw.util.Pagination;

@Configuration(order=Integer.MIN_VALUE)
public class UserSignInServiceImpl extends BaseServiceConfiguration implements UserSignInService {

	public UserSignInServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
		db.createTable(UserSignInLogTable.class, false);
		db.createTable(UserSignIn.class, false);
	}

	protected KeyValuePair<Integer, UserSignInLogTable> getContinuousSignInCount(long uid, int type, int initCount,
			long lastSignInTime) {
		Sql sql = new SimpleSql("select * from user_sign_in_log_table where uid=? type=? order by signInTime desc", uid,
				type);
		IteratorUserSignInLogTable iterator = new IteratorUserSignInLogTable(initCount, lastSignInTime);
		db.iterator(sql, UserSignInLogTable.class, iterator);
		if (iterator.getCount() == 0) {
			return null;
		}

		return new KeyValuePair<Integer, UserSignInLogTable>(iterator.getCount(), iterator.getLog());
	}

	public Result signIn(long uid, int type, long signInTime, long currentTimeMillis) {
		Calendar time = CalendarUtils.getDayBeginCalendar(signInTime);// 签到开始时间
		UserSignInLogTable log = new UserSignInLogTable();
		log.setUid(uid);
		log.setType(type);
		log.setSignInTime(time.getTimeInMillis());
		log.setCreateTime(currentTimeMillis);

		UserSignIn signIn = getUserSignIn(uid, type);
		if (signIn != null) {
			if (log.isSupplementary()) {
				if (db.getById(UserSignInLogTable.class, uid, type, log.getSignInTime()) != null) {
					return resultFactory.error("已经签到过了，无法进行补签操作");
				}

				// 补签 重新计算连续签到次数
				KeyValuePair<Integer, UserSignInLogTable> keyValuePair = getContinuousSignInCount(uid, type, 0,
						log.getCreateTime());
				if (keyValuePair == null) {
					signIn.setCount(0);
					signIn.setLastSignInTime(0);
				} else {
					signIn.setCount(keyValuePair.getKey());
					signIn.setLastSignInTime(log.getSignInTime());
				}
			} else {
				ContinuityStatus status = ContinuityStatus.getContinuityStatus(signIn.getLastSignInTime(),
						time.getTimeInMillis(), ContinuityCycle.DAY);
				if (status == ContinuityStatus.SAME) {// 同一天
					return resultFactory.error("已经签到过了");
				}

				if (status == ContinuityStatus.CONTINUITY) {// 连续的
					signIn.setCount(signIn.getCount() + 1);
				} else {// 不连续的
					signIn.setCount(1);
				}
				signIn.setLastSignInTime(log.getSignInTime());
			}
			db.update(signIn);
		} else {
			signIn = new UserSignIn();
			signIn.setUid(uid);
			signIn.setType(type);
			if (log.isSupplementary()) {// 补签
				// 补签 重新计算连续签到次数
				KeyValuePair<Integer, UserSignInLogTable> keyValuePair = getContinuousSignInCount(uid, type, 0,
						log.getCreateTime());
				if (keyValuePair == null) {
					signIn.setCount(0);
					signIn.setLastSignInTime(0);
				} else {
					signIn.setCount(keyValuePair.getKey());
					signIn.setLastSignInTime(log.getSignInTime());
				}
			} else {
				signIn.setCount(1);
				signIn.setLastSignInTime(log.getSignInTime());
			}
			db.save(signIn);
		}
		db.save(log);
		return resultFactory.success();
	}

	public UserSignIn getUserSignIn(long uid, int type) {
		return db.getById(UserSignIn.class, uid, type);
	}

	public Pagination<UserSignInLogTable> getPagination(long uid, int type, long beginTime, long endTime, int page,
			int limit) {
		Sql sql = new SimpleSql(
				"select * from user_sign_in_log_table where uid=? and type=? and signInTime>=? and signInTime <=?", uid,
				type, beginTime, endTime);
		return db.select(UserSignInLogTable.class, page, limit, sql);
	}

	private static class IteratorUserSignInLogTable implements IteratorCallback<UserSignInLogTable> {
		private int count;
		private long lastSignInTime;
		private UserSignInLogTable log;

		public IteratorUserSignInLogTable(int count, long lastSignInTime) {
			this.count = count;
			this.lastSignInTime = lastSignInTime;
		}

		public boolean iteratorCallback(UserSignInLogTable value) {
			if (ContinuityStatus.getContinuityStatus(value.getSignInTime(), lastSignInTime,
					ContinuityCycle.DAY) == ContinuityStatus.CONTINUITY) {
				this.log = value;
				count++;
				return true;
			}
			return false;
		}

		public int getCount() {
			return count;
		}

		public UserSignInLogTable getLog() {
			return log;
		}
	}
}
