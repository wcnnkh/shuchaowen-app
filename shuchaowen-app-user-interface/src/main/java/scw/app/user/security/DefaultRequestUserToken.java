package scw.app.user.security;

import scw.core.GlobalPropertyFactory;
import scw.core.instance.annotation.Configuration;
import scw.event.support.DynamicValue;
import scw.http.HttpCookie;
import scw.http.HttpUtils;
import scw.mvc.HttpChannel;
import scw.security.login.UserToken;
import scw.value.StringValue;
import scw.value.Value;

@Configuration(order = Integer.MIN_VALUE)
public class DefaultRequestUserToken implements RequestUserToken {
	public static final DynamicValue<String> TOKEN_NAME = GlobalPropertyFactory.getInstance()
			.getDynamicValue("user.security.token.name", String.class, "token");
	public static final DynamicValue<String> UID_NAME = GlobalPropertyFactory.getInstance()
			.getDynamicValue("user.security.uid.name", String.class, "uid");
	private Long uid;
	private String token;

	public DefaultRequestUserToken(HttpChannel httpChannel) {
		Value uid = getParameter(httpChannel, UID_NAME.getValue());
		Value token = getParameter(httpChannel, TOKEN_NAME.getValue());
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
