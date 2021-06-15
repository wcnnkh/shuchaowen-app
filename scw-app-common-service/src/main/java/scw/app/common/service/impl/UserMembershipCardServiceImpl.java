package scw.app.common.service.impl;

import java.util.Calendar;
import java.util.List;

import scw.app.common.pojo.UserMembershipCard;
import scw.app.common.service.UserMembershipCardService;
import scw.app.enums.TimeCycle;
import scw.app.util.BaseServiceConfiguration;
import scw.beans.annotation.Service;
import scw.context.result.ResultFactory;
import scw.core.utils.StringUtils;
import scw.core.utils.XTime;
import scw.data.TemporaryStorage;
import scw.db.DB;
import scw.sql.SimpleSql;
import scw.sql.Sql;
import scw.util.CalendarUtils;

@Service
public class UserMembershipCardServiceImpl extends BaseServiceConfiguration implements UserMembershipCardService {
	private static final String CURRENT_MEMBERSHIP_CARD_CACHE_PREFIX = "user.membership_card.current.id:";
	private static final int EXP = (int) ((XTime.ONE_DAY * 7) / 1000);
	private TemporaryStorage temporaryCache;

	public UserMembershipCardServiceImpl(DB db, ResultFactory resultFactory, TemporaryStorage temporaryCache) {
		super(db, resultFactory);
		this.temporaryCache = temporaryCache;
		db.createTable(UserMembershipCard.class, false);
	}

	public UserMembershipCard getUserMembershipCard(String id) {
		return db.getById(UserMembershipCard.class, id);
	}

	protected UserMembershipCard getCurrentByDB(long uid) {
		long t = System.currentTimeMillis();
		Sql sql = new SimpleSql(
				"select * from user_membership_card where uid=? and beginTime>=? and endTime<? limit 0,1", uid, t, t);
		return db.queryFirst(UserMembershipCard.class, sql);
	}

	public UserMembershipCard getCurrentUserMembershipCard(long uid) {
		String cacheKey = CURRENT_MEMBERSHIP_CARD_CACHE_PREFIX + uid;
		String id = temporaryCache.getAndTouch(cacheKey, EXP);
		if (StringUtils.isEmpty(id)) {
			UserMembershipCard card = getCurrentByDB(uid);
			if (card == null) {
				return null;
			}

			temporaryCache.set(cacheKey, EXP, card.getId());
			return card;
		} else {
			UserMembershipCard card = getUserMembershipCard(id);
			if (card.isAvailable()) {
				return card;
			}

			card = getCurrentByDB(uid);
			if (card != null && card.isAvailable()) {
				temporaryCache.set(cacheKey, EXP, card.getId());
			}
			return card;
		}
	}

	public List<UserMembershipCard> getUserMembershipCardList(long uid, Boolean available) {
		Sql sql;
		if (available == null) {
			sql = new SimpleSql("select * from user_membership_card where uid=? order by cts desc", uid);
		} else if (available) {
			sql = new SimpleSql("select * from user_membership_card where uid=? and beginTime>=? order by cts desc",
					uid, System.currentTimeMillis());
		} else {
			sql = new SimpleSql("select * from user_membership_card where uid=? and endTime<? order by cts desc", uid,
					System.currentTimeMillis());
		}
		return db.query(UserMembershipCard.class, sql);
	}

	public void addUserMembershipCard(long uid, int type, TimeCycle timeCycle, int timeCycleCount) {
		if (timeCycleCount <= 0) {
			throw new IllegalArgumentException("时间周期不能小于等于0");
		}

		Sql sql = new SimpleSql("select * from user_membership_card where uid=? order by cts desc limit 0,1", uid);
		UserMembershipCard lastCard = db.queryFirst(UserMembershipCard.class, sql);
		UserMembershipCard card = new UserMembershipCard();
		card.setUid(uid);
		card.setCts(System.currentTimeMillis());
		card.setType(type);
		if (lastCard != null && lastCard.getEndTime() > card.getCts()) {
			card.setBeginTime(lastCard.getEndTime());
		} else {
			card.setBeginTime(CalendarUtils.getDayBeginCalendar(card.getCts()).getTimeInMillis());
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(card.getBeginTime());
		switch (timeCycle) {
		case DAY:
			calendar.add(Calendar.DAY_OF_YEAR, timeCycleCount);
			break;
		case MONTH:
			calendar.add(Calendar.MONTH, timeCycleCount);
			break;
		case YEAR:
			calendar.add(Calendar.YEAR, timeCycleCount);
		default:
			break;
		}

		card.setEndTime(calendar.getTimeInMillis());

		if (card.getBeginTime() >= card.getEndTime()) {
			throw new IllegalArgumentException("会员卡开始时间不能大于等于结束时间");
		}
		db.save(card);
	}

}
