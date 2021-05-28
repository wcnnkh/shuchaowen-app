package scw.app.editable;

import java.util.List;

import scw.mvc.HttpChannel;
import scw.security.authority.http.HttpAuthority;

public interface Editor extends HttpAuthority {

	List<FieldInfo> getFieldInfos(Object query);

	Object doAction(HttpChannel httpChannel);
}
