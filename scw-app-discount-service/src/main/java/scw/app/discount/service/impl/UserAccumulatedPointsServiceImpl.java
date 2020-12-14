package scw.app.discount.service.impl;

import scw.app.discount.pojo.UserAccumulatedPoints;
import scw.app.discount.pojo.UserAccumulatedPointsLog;
import scw.app.discount.service.UserAccumulatedPointsService;
import scw.app.util.BaseServiceConfiguration;
import scw.beans.annotation.Service;
import scw.db.DB;
import scw.result.Result;
import scw.result.ResultFactory;

@Service
public class UserAccumulatedPointsServiceImpl extends BaseServiceConfiguration implements UserAccumulatedPointsService {

	public UserAccumulatedPointsServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
		db.createTable(UserAccumulatedPoints.class, false);
	}

	public UserAccumulatedPoints getUserAccumulatedPoints(long uid) {
		return db.getById(UserAccumulatedPoints.class, uid);
	}

	public String change(long uid, int change, String describe) {
		UserAccumulatedPoints userAccumulatedPoints = db.getById(UserAccumulatedPoints.class, uid);
		if (userAccumulatedPoints == null) {
			userAccumulatedPoints = new UserAccumulatedPoints();
			userAccumulatedPoints.setUid(uid);
			userAccumulatedPoints.setAccumulatedPoints(change);
			db.save(userAccumulatedPoints);
		} else {
			userAccumulatedPoints.setAccumulatedPoints(userAccumulatedPoints.getAccumulatedPoints() + change);
			if (!db.update(userAccumulatedPoints)) {
				return null;
			}
		}

		UserAccumulatedPointsLog log = new UserAccumulatedPointsLog();
		log.setUid(uid);
		log.setDescribe(describe);
		log.setAccumulatedPointsChange(change);
		db.save(log);
		return log.getId();
	}

	public Result cancelChange(String logId) {
		UserAccumulatedPointsLog log = db.getById(UserAccumulatedPointsLog.class, logId);
		if (log == null) {
			return resultFactory.error("修改记录不存在(" + logId + ")");
		}

		if (log.isCancel()) {
			return resultFactory.error("已经取消了");
		}

		UserAccumulatedPoints points = db.getById(UserAccumulatedPoints.class, log.getUid());
		if (points == null) {
			return resultFactory.error("用户不存在(" + log.getUid() + ")");
		}

		log.setCancel(true);
		db.update(log);
		points.setAccumulatedPoints(points.getAccumulatedPoints() - log.getAccumulatedPointsChange());
		db.update(points);
		return resultFactory.success();

	}

}
