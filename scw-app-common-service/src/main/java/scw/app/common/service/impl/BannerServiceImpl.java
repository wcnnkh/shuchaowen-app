package scw.app.common.service.impl;

import java.util.List;

import scw.app.common.model.BannerModel;
import scw.app.common.pojo.Banner;
import scw.app.common.service.BannerService;
import scw.app.util.BaseServiceConfiguration;
import scw.db.DB;
import scw.mapper.Copy;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.sql.WhereSql;

public class BannerServiceImpl extends BaseServiceConfiguration implements BannerService {

	public BannerServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
	}

	public List<Banner> getList(int type, boolean hidden, long beginTime, long endTime) {
		WhereSql whereSql = new WhereSql();
		whereSql.and("type=?", type);
		whereSql.and("hidden=?", hidden);
		if (beginTime > 0) {
			whereSql.and("(beginTime=0 or beginTime>=?)", beginTime);
		}

		if (endTime > 0) {
			whereSql.and("(endTime=0 or endTime>?)", endTime);
		}
		return db.select(Banner.class, whereSql.assembleSql("select * from banner", null));
	}

	public DataResult<Banner> add(BannerModel bannerModel) {
		Banner banner = new Banner();
		Copy.copy(banner, bannerModel);
		db.save(banner);
		return resultFactory.success(banner);
	}

	public Result update(Banner banner) {
		db.update(banner);
		return resultFactory.success();
	}

}
