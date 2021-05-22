package scw.app.editable.support;

import java.util.Arrays;
import java.util.List;

import scw.app.editable.DataManager;
import scw.app.editable.Editor;
import scw.app.editable.EditorResolver;
import scw.app.editable.annotation.Editable;
import scw.app.user.security.SecurityProperties;
import scw.context.annotation.Provider;
import scw.mvc.page.PageFactory;

@Provider
public class DefaultEditorResolver implements EditorResolver {
	private final DataManager dataManager;
	private final PageFactory pageFactory;
	private final SecurityProperties securityProperties;

	public DefaultEditorResolver(DataManager dataManager, PageFactory pageFactory, SecurityProperties securityProperties) {
		this.dataManager = dataManager;
		this.pageFactory = pageFactory;
		this.securityProperties = securityProperties;
	}

	@Override
	public boolean canResolve(Class<?> clazz) {
		return clazz.isAnnotationPresent(Editable.class);
	}

	@Override
	public List<Editor> resolve(Class<?> clazz) {
		EditorParent parent = new EditorParent(dataManager, clazz, pageFactory, securityProperties);
		EditorAddPage addPage = new EditorAddPage(dataManager, clazz, pageFactory, securityProperties);
		EditorInfoPage infoPage = new EditorInfoPage(dataManager, clazz, pageFactory, securityProperties);
		EditorAdd add = new EditorAdd(dataManager, clazz, pageFactory, securityProperties);
		EditorUpdate update = new EditorUpdate(dataManager, clazz, pageFactory, securityProperties);
		EditorDelete delete = new EditorDelete(dataManager, clazz, pageFactory, securityProperties);
		return Arrays.asList(parent, addPage, infoPage, add, update, delete);
	}

}
