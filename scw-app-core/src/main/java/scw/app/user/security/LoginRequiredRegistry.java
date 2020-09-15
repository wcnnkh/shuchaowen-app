package scw.app.user.security;

import scw.http.server.HttpServiceConfig;
import scw.http.server.ServerHttpRequest;
import scw.util.StringMatcher;

public class LoginRequiredRegistry extends HttpServiceConfig<Boolean> {

	public LoginRequiredRegistry() {
		super();
	}

	public LoginRequiredRegistry(StringMatcher matcher) {
		super(matcher);
	}

	public boolean isLoginRequried(ServerHttpRequest request) {
		Boolean v = getConfig(request.getPath());
		if (v == null) {
			return false;
		}
		return v;
	}
}
