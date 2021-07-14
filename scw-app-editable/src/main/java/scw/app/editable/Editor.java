package scw.app.editable;

import java.util.List;

import scw.app.editable.form.Input;
import scw.mvc.HttpChannel;
import scw.security.authority.http.HttpAuthority;

public interface Editor extends HttpAuthority {

	List<Input> getInputs(Object query);

	Object doAction(HttpChannel httpChannel);
}
