package scw.app.editable.support;

import scw.app.editable.DataManager;
import scw.app.user.security.SecurityProperties;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;

public class EditorDelete extends EditorCURD {

	public EditorDelete(DataManager dataManager, Class<?> editableClass, SecurityProperties securityProperties) {
		super(dataManager, editableClass, HttpMethod.POST, securityProperties, "delete");
	}
	
	@Override
	public String getName() {
		return super.getName() + "(删除)";
	}

	@Override
	public Object doAction(HttpChannel httpChannel) {
		Object requestBean = httpChannel.getInstance(getEditableClass());
		return getDataManager().delete(getEditableClass(), requestBean);
	}
}
