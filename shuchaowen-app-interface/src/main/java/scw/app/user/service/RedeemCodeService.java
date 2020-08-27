package scw.app.user.service;

import java.util.List;

import scw.app.user.pojo.RedeemCode;
import scw.app.user.pojo.UserRedeemCodeLog;
import scw.result.DataResult;
import scw.result.Result;
import scw.util.Pagination;

public interface RedeemCodeService {
	RedeemCode getRedeemCode(int type, String code);

	DataResult<RedeemCode> create(RedeemCode redeemCode);

	DataResult<List<RedeemCode>> create(int type, int count, int usableCount, long usableBeginTime, long usableEndTime);

	Result use(long uid, int type, String code);

	Pagination<UserRedeemCodeLog> getUserRedeemCodeLogPagination(long uid, long beginTime, long endTime, int page,
			int limit);
}
