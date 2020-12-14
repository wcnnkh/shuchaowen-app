package scw.app.discount.service.impl;

import java.util.List;

import scw.app.discount.pojo.UserVoucher;
import scw.app.discount.pojo.UserVoucherLog;
import scw.app.discount.service.UserVoucherService;
import scw.app.util.BaseServiceConfiguration;
import scw.beans.annotation.Service;
import scw.db.DB;
import scw.result.Result;
import scw.result.ResultFactory;

@Service
public class UserVoucherServiceImpl extends BaseServiceConfiguration implements UserVoucherService {

	public UserVoucherServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
		db.createTable(UserVoucher.class, false);
	}

	public UserVoucher getUserVoucher(long uid, int voucherId) {
		return db.getById(UserVoucher.class, uid, voucherId);
	}

	public List<UserVoucher> getUserVoucherList(long uid) {
		return db.getByIdList(UserVoucher.class, uid);
	}

	public String change(long uid, int voucherId, int count, String msg) {
		UserVoucher userVoucher = getUserVoucher(uid, voucherId);
		if (userVoucher == null) {
			if (count < 0) {
				return null;
			}
			userVoucher = new UserVoucher();
			userVoucher.setUid(uid);
			userVoucher.setVoucherId(voucherId);
			userVoucher.setCount(count);
			db.save(userVoucher);
		} else {
			userVoucher.setCount(userVoucher.getCount() - count);
			if (userVoucher.getCount() < 0) {
				return null;
			}

			if (!db.update(userVoucher)) {
				return null;
			}
		}

		UserVoucherLog log = new UserVoucherLog();
		log.setUid(uid);
		log.setVoucherId(voucherId);
		log.setCount(count);
		db.save(log);
		return log.getId();
	}

	public Result cancelChange(String logId) {
		UserVoucherLog log = db.getById(UserVoucherLog.class, logId);
		if (log == null) {
			return resultFactory.error("日志不存在(" + logId + ")");
		}

		if (log.isCancel()) {
			return resultFactory.error("已经取消了(" + logId + ")");
		}

		UserVoucher userVoucher = getUserVoucher(log.getUid(), log.getVoucherId());
		if (userVoucher == null) {
			return resultFactory.error("不存在的券(" + logId + ")");
		}

		userVoucher.setCount(userVoucher.getCount() - log.getCount());
		if (!db.update(userVoucher)) {
			return resultFactory.error("取消失败(" + logId + ")");
		}

		return resultFactory.success();
	}

}
