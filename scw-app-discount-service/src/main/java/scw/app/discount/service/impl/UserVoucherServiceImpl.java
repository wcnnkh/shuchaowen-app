package scw.app.discount.service.impl;

import java.util.List;

import scw.app.discount.pojo.UserVoucher;
import scw.app.discount.pojo.UserVoucherLog;
import scw.app.discount.service.UserVoucherService;
import scw.app.util.BaseServiceImpl;
import scw.core.instance.annotation.Configuration;
import scw.data.generator.SequenceId;
import scw.data.generator.SequenceIdGenerator;
import scw.db.DB;
import scw.result.Result;
import scw.result.ResultFactory;

@Configuration(order=Integer.MIN_VALUE)
public class UserVoucherServiceImpl extends BaseServiceImpl implements UserVoucherService {
	protected final SequenceIdGenerator sequenceIdGenerator;

	public UserVoucherServiceImpl(DB db, ResultFactory resultFactory, SequenceIdGenerator sequenceIdGenerator) {
		super(db, resultFactory);
		this.sequenceIdGenerator = sequenceIdGenerator;
		db.createTable(UserVoucher.class, false);
	}

	public UserVoucher getUserVoucher(long uid, long voucherId) {
		return db.getById(UserVoucher.class, uid, voucherId);
	}

	public List<UserVoucher> getUserVoucherList(long uid) {
		return db.getByIdList(UserVoucher.class, uid);
	}

	public String change(long uid, long voucherId, int count, int group, String msg) {
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
		SequenceId sequenceId = sequenceIdGenerator.next();
		log.setId(sequenceId.getId());
		log.setCts(sequenceId.getTimestamp());
		log.setUid(uid);
		log.setVoucherId(voucherId);
		log.setCount(count);
		log.setGroup(group);
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
