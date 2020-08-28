package scw.app.vc.service.impl;

import java.util.HashMap;
import java.util.Map;

import scw.core.Assert;
import scw.data.TemporaryCache;
import scw.result.Result;
import scw.result.ResultFactory;

public class VerificationCodeManager {
	private Map<String, VerificationCodeService> serviceMap = new HashMap<String, VerificationCodeService>();
	private final ResultFactory resultFactory;

	public VerificationCodeManager(ResultFactory resultFactory) {
		this.resultFactory = resultFactory;
	}

	public void set(String group, VerificationCodeService service) {
		serviceMap.put(group, service);
	}

	public VerificationCodeService getService(String group) {
		return serviceMap.get(group);
	}

	public void set(String group, VerificationCodeSender sender, TemporaryCache temporaryCache) {
		DefaultVerificationCodeService service = new DefaultVerificationCodeService(resultFactory, sender,
				temporaryCache, group);
		set(group, service);
	}

	public Result send(String group, String user) {
		Assert.requiredArgument(resultFactory != null, "resultFactory");
		VerificationCodeService service = getService(group);
		if (service == null) {
			return resultFactory.error("not found service(" + group + ")");
		}

		return service.send(user);
	}

	public Result check(String group, String user, String code) {
		Assert.requiredArgument(resultFactory != null, "resultFactory");
		VerificationCodeService service = getService(group);
		if (service == null) {
			return resultFactory.error("not found service(" + group + ")");
		}

		return service.check(user, code);
	}
}
