package scw.app.vc.service.impl;

import java.io.Serializable;

import scw.core.Assert;
import scw.core.utils.XTime;
import scw.data.TemporaryCache;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.util.RandomUtils;

public class DefaultVerificationCodeService implements VerificationCodeService {
	private static final int ONE_DAY = (int) (XTime.ONE_DAY / 1000);
	private TemporaryCache temporaryCache;
	private int everyDayMaxSize = 10;// 每天发送限制
	private int maxTimeInterval = 30;// 两次发送时间限制(秒)
	private int maxActiveTime = 300;// 验证码有效时间(秒)

	private final VerificationCodeSender sender;
	// 验证码长度
	private int codeLength = 6;
	protected ResultFactory resultFactory;
	private final String prefix;

	public DefaultVerificationCodeService(ResultFactory resultFactory, VerificationCodeSender sender, TemporaryCache temporaryCache, String prefix) {
		this.sender = sender;
		this.temporaryCache = temporaryCache;
		this.prefix = prefix;
		this.resultFactory = resultFactory;
	}

	public int getCodeLength() {
		return codeLength;
	}

	public void setCodeLength(int codeLength) {
		this.codeLength = codeLength;
	}

	public TemporaryCache getTemporaryCache() {
		return temporaryCache;
	}

	public void setTemporaryCache(TemporaryCache temporaryCache) {
		this.temporaryCache = temporaryCache;
	}

	public int getEveryDayMaxSize() {
		return everyDayMaxSize;
	}

	public void setEveryDayMaxSize(int everyDayMaxSize) {
		this.everyDayMaxSize = everyDayMaxSize;
	}

	public int getMaxTimeInterval() {
		return maxTimeInterval;
	}

	public void setMaxTimeInterval(int maxTimeInterval) {
		this.maxTimeInterval = maxTimeInterval;
	}

	public int getMaxActiveTime() {
		return maxActiveTime;
	}

	public void setMaxActiveTime(int maxActiveTime) {
		this.maxActiveTime = maxActiveTime;
	}

	protected String getCacheKey(String user) {
		return getClass().getName() + "&" + prefix + "&" + user;
	}

	public Result send(String user) {
		Assert.requiredArgument(resultFactory != null, "resultFactory");

		String key = getCacheKey(user);
		VerificationCodeInfo info = temporaryCache.getAndTouch(key, ONE_DAY);
		if (info == null) {
			info = new VerificationCodeInfo();
		}

		long lastSendTime = info.getLastSendTime();
		if (getMaxTimeInterval() > 0 && (System.currentTimeMillis() - lastSendTime) < getMaxTimeInterval() * 1000L) {
			return resultFactory.error("发送过于频繁");
		}

		int count = info.getCount();
		if (getEveryDayMaxSize() > 0 && (count > getEveryDayMaxSize())) {
			return resultFactory.error("今日发送次数过多");
		}

		String code = RandomUtils.getNumCode(getCodeLength());
		Result result = sender.send(user, code);
		if (result.isError()) {
			return result;
		}

		if (System.currentTimeMillis() - info.getLastSendTime() > XTime.ONE_DAY) {
			info.setCount(1);
		} else {
			info.setCount(info.getCount() + 1);
		}

		info.setCode(code);
		info.setLastSendTime(System.currentTimeMillis());
		temporaryCache.set(key, ONE_DAY, info);
		return result;
	}

	public Result check(String user, String code) {
		if (code == null || user == null) {
			return resultFactory.parameterError();
		}

		String key = getCacheKey(user);
		VerificationCodeInfo info = temporaryCache.get(key);
		if (info == null) {
			return resultFactory.error("验证码错误(not sent)");
		}

		if (getMaxActiveTime() > 0
				&& (System.currentTimeMillis() - info.getLastSendTime()) > getMaxActiveTime() * 1000L) {
			return resultFactory.error("验证码错误(Expired)");// 验证码已过期
		}

		if (!code.equals(info.getCode())) {
			return resultFactory.error("验证码错误");
		}

		info.setCode(null);
		temporaryCache.set(key, ONE_DAY, info);
		return resultFactory.success();
	}

	public static final class VerificationCodeInfo implements Serializable {
		private static final long serialVersionUID = 1L;
		private long lastSendTime;
		private String code;
		private int count;

		public long getLastSendTime() {
			return lastSendTime;
		}

		public void setLastSendTime(long lastSendTime) {
			this.lastSendTime = lastSendTime;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}
	}
}
