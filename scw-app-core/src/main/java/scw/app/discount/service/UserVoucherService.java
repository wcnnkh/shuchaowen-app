package scw.app.discount.service;

import java.util.List;

import scw.app.discount.pojo.UserVoucher;
import scw.result.Result;
import scw.tcc.annotation.Tcc;
import scw.tcc.annotation.TccStage;
import scw.tcc.annotation.TryResult;

public interface UserVoucherService {
	UserVoucher getUserVoucher(long uid, int voucherId);
	
	List<UserVoucher> getUserVoucherList(long uid);

	@Tcc(cancel="cancelChange")
	String change(long uid, int voucherId, int count, String msg);
	
	@TccStage
	Result cancelChange(@TryResult String logId);
}
