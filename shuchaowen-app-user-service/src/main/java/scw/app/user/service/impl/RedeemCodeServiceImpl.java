package scw.app.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import scw.app.common.BaseServiceImpl;
import scw.app.common.service.RedeemCodeGenerator;
import scw.app.user.pojo.RedeemCode;
import scw.app.user.pojo.UserRedeemCodeLog;
import scw.app.user.service.RedeemCodeService;
import scw.core.instance.annotation.Configuration;
import scw.db.DB;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.sql.WhereSql;
import scw.util.Pagination;

@Configuration(order=Integer.MIN_VALUE)
public class RedeemCodeServiceImpl extends BaseServiceImpl implements RedeemCodeService {
	private RedeemCodeGenerator redeemCodeGenerator;

	public RedeemCodeServiceImpl(DB db, ResultFactory resultFactory, RedeemCodeGenerator redeemCodeGenerator) {
		super(db, resultFactory);
		this.redeemCodeGenerator = redeemCodeGenerator;
	}

	public RedeemCode getRedeemCode(int type, String code) {
		return db.getById(RedeemCode.class, type, code);
	}

	public DataResult<RedeemCode> create(RedeemCode redeemCode) {
		if (getRedeemCode(redeemCode.getType(), redeemCode.getCode()) != null) {
			return resultFactory.error("兑换码已经存在");
		}

		db.save(redeemCode);
		return resultFactory.success(redeemCode);
	}

	public DataResult<List<RedeemCode>> create(int type, int count, int usableCount, long usableBeginTime,
			long usableEndTime) {
		List<RedeemCode> list = new ArrayList<RedeemCode>(count);
		for (int i = 0; i < count; i++) {
			RedeemCode code = new RedeemCode();
			code.setType(type);
			code.setCode(redeemCodeGenerator.generator(type));
			code.setUsableBeginTime(usableBeginTime);
			code.setUsableCount(usableCount);
			code.setUsableEndTime(usableEndTime);
			db.save(code);
			list.add(code);
		}
		return resultFactory.success(list);
	}

	public Result use(long uid, int type, String code) {
		RedeemCode redeemCode = getRedeemCode(type, code);
		if (redeemCode == null) {
			return resultFactory.error("无效的兑换码(1)");
		}

		if (redeemCode.getUsableCount() == 0) {
			return resultFactory.error("兑换码已无效(1)");
		}

		if (redeemCode.getUsableBeginTime() != 0 && System.currentTimeMillis() < redeemCode.getUsableBeginTime()) {
			return resultFactory.error("兑换时间未开始");
		}

		if (redeemCode.getUsableEndTime() != 0 && System.currentTimeMillis() < redeemCode.getUsableEndTime()) {
			return resultFactory.error("兑换码已过期");
		}

		UserRedeemCodeLog log = db.getById(UserRedeemCodeLog.class, uid, type, code);
		if (log != null) {
			return resultFactory.error("无效的兑换码(2)");
		}

		redeemCode.setUsableCount(redeemCode.getUsableCount() - 1);
		if (!db.update(redeemCode)) {
			return resultFactory.error("兑换码已无效(2)");
		}

		log = new UserRedeemCodeLog();
		log.setUid(uid);
		log.setType(type);
		log.setCode(code);
		log.setCts(System.currentTimeMillis());
		db.save(log);
		return resultFactory.success();
	}

	public Pagination<UserRedeemCodeLog> getUserRedeemCodeLogPagination(long uid, long beginTime, long endTime,
			int page, int limit) {
		WhereSql whereSql = new WhereSql();
		whereSql.and("uid=?", uid);
		if (beginTime > 0) {
			whereSql.and("cts >=?", beginTime);
		}

		if (endTime > 0) {
			whereSql.and("cts <=?", endTime);
		}

		return db.select(UserRedeemCodeLog.class, page, limit,
				whereSql.assembleSql("select * from user_redeem_code_log", "order by cts desc"));
	}

}
