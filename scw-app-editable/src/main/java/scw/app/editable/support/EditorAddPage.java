package scw.app.editable.support;

import scw.app.editable.DataManager;
import scw.app.user.security.SecurityProperties;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;
import scw.web.model.Page;

public class EditorAddPage extends EditorCURD {

	public EditorAddPage(DataManager dataManager, Class<?> editableClass, SecurityProperties securityProperties) {
		super(dataManager, editableClass, HttpMethod.GET, securityProperties);
	}
	
	@Override
	public String getPath() {
		return getSecurityProperties().getController() + "/" + getEditableClass().getName() + "/add";
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
		Page page = new Page("/editable/add.ftl");
		page.put("fields", getFieldInfos(requestBean));
		return page;
	}
}
