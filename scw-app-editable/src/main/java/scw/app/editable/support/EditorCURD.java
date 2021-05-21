package scw.app.editable.support;

import java.util.Map;

import scw.app.editable.DataManager;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;
import scw.mvc.page.PageFactory;

public abstract class EditorCURD extends EditorParent {
	private final HttpMethod method;

	public EditorCURD(DataManager dataManager, Class<?> editableClass, PageFactory pageFactory, HttpMethod method) {
		super(dataManager, editableClass, pageFactory);
		this.method = method;
	}

	@Override
	public String getPath() {
		return "/" + getEditableClass().getName() + "/" + method;
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
