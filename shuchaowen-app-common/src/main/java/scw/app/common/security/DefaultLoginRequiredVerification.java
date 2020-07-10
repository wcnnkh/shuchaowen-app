package scw.app.common.security;

import scw.beans.annotation.Autowired;
import scw.core.instance.annotation.Configuration;
import scw.mvc.HttpChannel;
import scw.mvc.action.Action;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.security.login.LoginService;
import scw.security.login.UserToken;
import scw.value.Value;

@Configuration(order = Integer.MIN_VALUE)
public class DefaultLoginRequiredVerification implements
		LoginRequiredVerification {
	private LoginService<Long> loginService;
	@Autowired
	private ResultFactory resultFactory;

	public DefaultLoginRequiredVerification(LoginService<Long> loginService) {
		this.loginService = loginService;
	}

	protected Value getToken(HttpChannel httpChannel) {
		return httpChannel.getValue("token");
	}

	protected Value getUid(HttpChannel httpChannel) {
		return httpChannel.getValue("uid");
	}

	public Result verification(Action action, HttpChannel httpChannel) {
		Value token = getToken(httpChannel);
		if (token.isEmpty()) {
			return resultFactory.authorizationFailure();
		}

		UserToken<Long> userToken = loginService.getUserToken(token
				.getAsString());
		if (userToken == null) {
			return resultFactory.authorizationFailure();
		}

		Value value = getUid(httpChannel);
		if (value.isEmpty()) {
			return resultFactory.success();
		}

		if (!userToken.getUid().equals(value.getAsLong())) {
			return resultFactory.authorizationFailure();
		}

		return resultFactory.success();
	}
}
