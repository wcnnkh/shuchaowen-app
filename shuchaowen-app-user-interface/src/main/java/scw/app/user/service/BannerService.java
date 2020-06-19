package scw.app.user.service;

import java.util.List;

import scw.app.user.pojo.Banner;
import scw.app.user.pojo.enums.BannerModel;
import scw.result.DataResult;
import scw.result.Result;

public interface BannerService {
	List<Banner> getList(long uid);

	DataResult<Banner> add(BannerModel banner);

	Result update(Banner banner);
}
