package scw.app.common.service;

import java.util.List;

import scw.app.common.pojo.RedeemCode;
import scw.app.common.pojo.UserRedeemCodeLog;
import scw.context.result.DataResult;
import scw.context.result.Result;
import scw.util.Pagination;

public interface RedeemCodeService {
	RedeemCode getRedeemCode(int type, String code);

	DataResult<RedeemCode> create(RedeemCode redeemCode);

	DataResult<List<RedeemCode>> create(int type, int count, int usableCount, long usableBeginTime, long usableEndTime);

	Result use(long uid, int type, String code);

	Pagination<UserRedeemCodeLog> getUserRedeemCodeLogPagination(long uid, long beginTime, long endTime, int page,
			int limit);
}
