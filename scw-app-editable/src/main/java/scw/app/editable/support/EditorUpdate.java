package scw.app.editable.support;

import scw.app.editable.DataManager;
import scw.app.user.security.SecurityProperties;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;

public class EditorUpdate extends EditorCURD {

	public EditorUpdate(DataManager dataManager, Class<?> editableClass, SecurityProperties securityProperties) {
		super(dataManager, editableClass, HttpMethod.POST, securityProperties);
	}

	@Override
	public String getName() {
		return super.getName() + "(修改)";
	}

	@Override
	public Object doAction(HttpChannel httpChannel) {
		Object requestBean = httpChannel.getInstanceFactory().getInstance(getEditableClass());
		return getDataManager().update(getEditableClass(), requestBean);
	}
}
