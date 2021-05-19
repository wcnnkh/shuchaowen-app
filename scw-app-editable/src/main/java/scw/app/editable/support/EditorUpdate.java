package scw.app.editable.support;

import scw.app.editable.DataManager;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;
import scw.mvc.page.PageFactory;

public class EditorUpdate extends EditorCURD {

	public EditorUpdate(DataManager dataManager, Class<?> editableClass, PageFactory pageFactory) {
		super(dataManager, editableClass, pageFactory, HttpMethod.POST);
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
