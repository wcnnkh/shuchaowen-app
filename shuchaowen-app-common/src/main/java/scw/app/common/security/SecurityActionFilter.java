package scw.app.common.security;

import scw.beans.annotation.Autowired;
import scw.core.instance.annotation.Configuration;
import scw.mvc.HttpChannel;
import scw.mvc.action.Action;
import scw.mvc.action.ActionFilter;
import scw.result.Result;

@Configuration(order = Integer.MAX_VALUE)
public class SecurityActionFilter implements ActionFilter {
	@Autowired(required = false)
	private LoginRequiredVerification requiredConfiguration;

	public Object doFilter(Action action, HttpChannel httpChannel)
			throws Throwable {
		if (requiredConfiguration != null) {
			LoginRequired required = action.getAnnotatedElement()
					.getAnnotation(LoginRequired.class);
			if (required != null && required.value()) {
				Result result = requiredConfiguration.verification(action,
						httpChannel);
				if (result.isError()) {
					return result;
				}
			}
		}
		return action.doAction(httpChannel);
	}
}
