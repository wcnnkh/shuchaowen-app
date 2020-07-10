package scw.app.common.security;

import scw.mvc.HttpChannel;
import scw.mvc.action.Action;
import scw.result.Result;

public interface LoginRequiredVerification {
	Result verification(Action action, HttpChannel httpChannel);
}
