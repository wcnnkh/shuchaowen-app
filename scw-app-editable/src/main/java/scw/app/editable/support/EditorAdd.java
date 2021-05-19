package scw.app.editable.support;

import scw.app.editable.DataManager;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;
import scw.mvc.page.PageFactory;

public class EditorAdd extends EditorCURD {

	public EditorAdd(DataManager dataManager, Class<?> editableClass, PageFactory pageFactory) {
		super(dataManager, editableClass, pageFactory, HttpMethod.PUT);
	}

	@Override
	public String getName() {
		return super.getName() + "(添加)";
	}

	@Override
	public Object doAction(HttpChannel httpChannel) {
		Object requestBean = httpChannel.getInstanceFactory().getInstance(getEditableClass());
		return getDataManager().add(getEditableClass(), requestBean);
	}

}
