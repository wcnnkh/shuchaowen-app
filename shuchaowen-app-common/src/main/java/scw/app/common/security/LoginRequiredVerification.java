package scw.app.common.security;

import scw.core.GlobalPropertyFactory;
import scw.mvc.HttpChannel;
import scw.mvc.action.Action;
import scw.result.Result;

public interface LoginRequiredVerification {
	public static final String TOKEN_NAME = GlobalPropertyFactory
			.getInstance().getValue("login.verification.token.name",
					String.class, "token");
	
	Result verification(Action action, HttpChannel httpChannel);
}