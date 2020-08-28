package scw.app.discount.service;

import java.util.List;

import scw.app.discount.pojo.UserVoucher;
import scw.result.Result;
import scw.tcc.annotation.Tcc;
import scw.tcc.annotation.TccStage;

public interface UserVoucherService {
	UserVoucher getUserVoucher(long uid, long voucherId);
	
	List<UserVoucher> getUserVoucherList(long uid);

	@Tcc(cancel="cancelChange")
	String change(long uid, long voucherId, int count, int group, String msg);
	
	@TccStage
	Result cancelChange(String logId);
}
