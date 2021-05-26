package scw.app.editable.support;

import scw.app.editable.DataManager;
import scw.app.user.security.SecurityProperties;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;

public class EditorAdd extends EditorCURD {

	public EditorAdd(DataManager dataManager, Class<?> editableClass, SecurityProperties securityProperties) {
		super(dataManager, editableClass, HttpMethod.PUT, securityProperties);
	}

	@Override
	public String getName() {
		return super.getName() + "(添加)";
	}

	@Override
	public Object doAction(HttpChannel httpChannel) {
		Object requestBean = httpChannel.getInstance(getEditableClass());
		return getDataManager().add(getEditableClass(), requestBean);
	}

}
