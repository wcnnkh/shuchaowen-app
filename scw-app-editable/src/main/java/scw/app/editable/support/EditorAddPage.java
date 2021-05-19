package scw.app.editable.support;

import scw.app.editable.DataManager;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;
import scw.mvc.page.Page;
import scw.mvc.page.PageFactory;

public class EditorAddPage extends EditorCURD {

	public EditorAddPage(DataManager dataManager, Class<?> editableClass, PageFactory pageFactory) {
		super(dataManager, editableClass, pageFactory, HttpMethod.GET);
	}
	
	@Override
	public String getPath() {
		return getEditableClass().getName() + "/add";
	}
	
	@Override
	public String getId() {
		return getEditableClass().getName() + "#add";
	}

	@Override
	public String getName() {
		return super.getName() + "(添加页面)";
	}

	@Override
	public Object doAction(HttpChannel httpChannel) {
		Object requestBean = httpChannel.getInstanceFactory().getInstance(getEditableClass());
		Page page = getPageFactory().getPage("add.ftl");
		page.put("fields", getFieldInfos(requestBean));
		return page;
	}
}
