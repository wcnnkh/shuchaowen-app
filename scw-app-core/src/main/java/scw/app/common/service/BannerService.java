package scw.app.common.service;

import java.util.List;

import scw.app.common.model.BannerModel;
import scw.app.common.pojo.Banner;
import scw.result.DataResult;
import scw.result.Result;

public interface BannerService {
	List<Banner> getList(int type, boolean hidden, long beginTime, long endTime);

	DataResult<Banner> add(BannerModel bannerModel);

	Result update(Banner banner);
}
