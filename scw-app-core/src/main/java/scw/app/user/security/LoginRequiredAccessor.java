package scw.app.user.security;

import java.util.Set;
import java.util.TreeSet;
import scw.http.server.ServerHttpRequest;
import scw.util.DefaultStringMatcher;
import scw.util.StringMatcher;
import scw.util.XUtils;

public class LoginRequiredAccessor {
	private final Set<String> requireds;
	private final StringMatcher matcher;

	public LoginRequiredAccessor() {
		this(DefaultStringMatcher.getInstance());
	}

	public LoginRequiredAccessor(StringMatcher matcher) {
		this.requireds = new TreeSet<String>(XUtils.getComparator(matcher));
		this.matcher = matcher;
	}

	public final Set<String> getRequireds() {
		return requireds;
	}

	public final StringMatcher getMatcher() {
		return matcher;
	}

	public boolean isLoginRequried(ServerHttpRequest request) {
		for (String path : requireds) {
			if (getMatcher().match(path, request.getPath())) {
				return true;
			}
		}
		return false;
	}
}
