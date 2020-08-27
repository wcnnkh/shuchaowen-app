package scw.app.user.security;

import scw.core.instance.annotation.Configuration;
import scw.http.HttpCookie;
import scw.http.HttpUtils;
import scw.mvc.HttpChannel;
import scw.security.login.UserToken;
import scw.value.StringValue;
import scw.value.Value;

@Configuration(order = Integer.MIN_VALUE)
public class DefaultRequestUser implements RequestUser {
	private Long uid;
	private String token;

	public DefaultRequestUser(HttpChannel httpChannel) {
		Value uid = getParameter(httpChannel, UID_NAME);
		Value token = getParameter(httpChannel, TOKEN_NAME);
		this.uid = uid == null ? null : uid.getAsLong();
		this.token = token == null ? null : token.getAsString();
	}

	public Long getUid() {
		return uid;
	}

	public String getToken() {
		return token;
	}

	protected Value getParameter(HttpChannel httpChannel, String name) {
		Value value = httpChannel.getValue(name);
		if (value == null || value.isEmpty()) {
			String token = httpChannel.getRequest().getHeaders().getFirst(name);
			if (token == null) {
				HttpCookie httpCookie = HttpUtils.getCookie(httpChannel.getRequest(), name);
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

	public boolean accept(UserToken<Long> userToken) {
		if (!userToken.getToken().equals(getToken())) {
			return false;
		}

		if (userToken.getUid() != null && getUid() != null) {
			if (!userToken.getUid().equals(getUid())) {
				return false;
			}
		}

		if (uid == null) {
			this.uid = userToken.getUid();
		}
		return true;
	}
}
