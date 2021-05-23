package scw.app.editable.support;

import java.util.Map;

import scw.app.editable.DataManager;
import scw.app.user.security.SecurityProperties;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;

public abstract class EditorCURD extends EditorParent {
	private final HttpMethod method;

	public EditorCURD(DataManager dataManager, Class<?> editableClass, HttpMethod method, SecurityProperties securityProperties) {
		super(dataManager, editableClass, securityProperties);
		this.method = method;
	}

	@Override
	public String getPath() {
		return getSecurityProperties().getController() + "/" + getEditableClass().getName() + "/" + method;
	}

	@Override
	public final HttpMethod getMethod() {
		return method;
	}

	@Override
	public String getId() {
		return getEditableClass().getName() + "#" + method;
	}

	@Override
	public final String getParentId() {
		return super.getId();
	}

	@Override
	public final boolean isMenu() {
		return false;
	}

	@Override
	public Map<String, String> getAttributeMap() {
		return null;
	}

	@Override
	public abstract Object doAction(HttpChannel httpChannel);
}
