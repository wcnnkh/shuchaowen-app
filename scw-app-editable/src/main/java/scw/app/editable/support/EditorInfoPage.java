package scw.app.editable.support;

import scw.app.editable.DataManager;
import scw.app.user.security.SecurityProperties;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;
import scw.web.model.Page;

public class EditorInfoPage extends EditorCURD {

	public EditorInfoPage(DataManager dataManager, Class<?> editableClass, SecurityProperties securityProperties) {
		super(dataManager, editableClass, HttpMethod.GET, securityProperties, "info");
	}

	@Override
	public String getName() {
		return super.getName() + "(查看)";
	}

	@Override
	public Object doAction(HttpChannel httpChannel) {
		Object requestBean = httpChannel.getInstance(getEditableClass());
		Object info = getDataManager().info(getEditableClass(), requestBean);
		Page page = new Page("/editable/info.ftl");
		page.put("info", info);
		page.put("query", requestBean);
		page.put("fields", getFieldInfos(requestBean));
		return page;
	}
}
