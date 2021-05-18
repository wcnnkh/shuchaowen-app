package scw.app.editable;

import scw.mvc.HttpChannel;
import scw.security.authority.http.HttpAuthority;

public interface Editor extends HttpAuthority {
	Object doAction(HttpChannel httpChannel);
}
