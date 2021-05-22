package scw.app.editable.support;

import scw.app.editable.DataManager;
import scw.app.user.security.SecurityProperties;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;
import scw.mvc.page.Page;
import scw.mvc.page.PageFactory;

public class EditorInfoPage extends EditorCURD {

	public EditorInfoPage(DataManager dataManager, Class<?> editableClass, PageFactory pageFactory, SecurityProperties securityProperties) {
		super(dataManager, editableClass, pageFactory, HttpMethod.GET, securityProperties);
	}

	@Override
	public String getName() {
		return super.getName() + "(查看)";
	}

	@Override
	public Object doAction(HttpChannel httpChannel) {
		Object requestBean = httpChannel.getInstanceFactory().getInstance(getEditableClass());
		Object info = getDataManager().info(getEditableClass(), requestBean);
		Page page = getPageFactory().getPage("info.ftl");
		page.put("info", info);
		page.put("query", requestBean);
		page.put("fields", getFieldInfos(requestBean));
		return page;
	}
}
