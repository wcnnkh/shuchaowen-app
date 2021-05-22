package scw.app.editable.support;

import scw.app.editable.DataManager;
import scw.app.user.security.SecurityProperties;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;
import scw.mvc.page.PageFactory;

public class EditorDelete extends EditorCURD {

	public EditorDelete(DataManager dataManager, Class<?> editableClass, PageFactory pageFactory, SecurityProperties securityProperties) {
		super(dataManager, editableClass, pageFactory, HttpMethod.DELETE, securityProperties);
	}
	
	@Override
	public String getName() {
		return super.getName() + "(删除)";
	}

	@Override
	public Object doAction(HttpChannel httpChannel) {
		Object requestBean = httpChannel.getInstanceFactory().getInstance(getEditableClass());
		return getDataManager().delete(getEditableClass(), requestBean);
	}
}
