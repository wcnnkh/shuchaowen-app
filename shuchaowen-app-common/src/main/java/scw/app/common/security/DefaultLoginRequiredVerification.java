package scw.app.common.security;

import scw.beans.annotation.Autowired;
import scw.core.GlobalPropertyFactory;
import scw.core.instance.annotation.Configuration;
import scw.http.HttpCookie;
import scw.http.HttpUtils;
import scw.mvc.HttpChannel;
import scw.mvc.action.Action;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.security.login.LoginService;
import scw.security.login.UserToken;
import scw.value.StringValue;
import scw.value.Value;

@Configuration(order = Integer.MIN_VALUE)
public class DefaultLoginRequiredVerification implements
		LoginRequiredVerification {
	private static final String TOKEN_NAME = GlobalPropertyFactory
			.getInstance().getValue("login.verification.token.name",
					String.class, "token");

	private LoginService<Long> loginService;
	@Autowired
	private ResultFactory resultFactory;

	public DefaultLoginRequiredVerification(LoginService<Long> loginService) {
		this.loginService = loginService;
	}

	protected Value getToken(HttpChannel httpChannel) {
		Value value = httpChannel.getValue(TOKEN_NAME);
		if (value == null || value.isEmpty()) {
			String token = httpChannel.getRequest().getHeaders()
					.getFirst(TOKEN_NAME);
			if (token == null) {
				HttpCookie httpCookie = HttpUtils.getCookie(
						httpChannel.getRequest(), TOKEN_NAME);
				if (httpCookie != null) {
					token = httpCookie.getValue();
				}
			}

			if (token != null) {
				value = new StringValue(token);
			}
		}
		return value;
	}

	protected Value getUid(HttpChannel httpChannel) {
		return httpChannel.getValue("uid");
	}

	public Result verification(Action action, HttpChannel httpChannel) {
		Value token = getToken(httpChannel);
		if (token == null || token.isEmpty()) {
			return resultFactory.authorizationFailure();
		}

		UserToken<Long> userToken = loginService.getUserToken(token
				.getAsString());
		if (userToken == null) {
			return resultFactory.authorizationFailure();
		}

		Value value = getUid(httpChannel);
		if (value == null || value.isEmpty()) {
			return resultFactory.success();
		}

		if (!userToken.getUid().equals(value.getAsLong())) {
			return resultFactory.authorizationFailure();
		}

		return resultFactory.success();
	}
}
