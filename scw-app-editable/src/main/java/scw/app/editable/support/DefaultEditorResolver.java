package scw.app.editable.support;

import java.util.Arrays;
import java.util.List;

import scw.app.editable.DataManager;
import scw.app.editable.Editor;
import scw.app.editable.EditorResolver;
import scw.app.editable.annotation.Editable;
import scw.context.annotation.Provider;
import scw.mvc.page.PageFactory;

@Provider
public class DefaultEditorResolver implements EditorResolver {
	private final DataManager dataManager;
	private final PageFactory pageFactory;

	public DefaultEditorResolver(DataManager dataManager, PageFactory pageFactory) {
		this.dataManager = dataManager;
		this.pageFactory = pageFactory;
	}

	@Override
	public boolean canResolve(Class<?> clazz) {
		return clazz.isAnnotationPresent(Editable.class);
	}

	@Override
	public List<Editor> resolve(Class<?> clazz) {
		EditorParent parent = new EditorParent(dataManager, clazz, pageFactory);
		EditorAddPage addPage = new EditorAddPage(dataManager, clazz, pageFactory);
		EditorInfoPage infoPage = new EditorInfoPage(dataManager, clazz, pageFactory);
		EditorAdd add = new EditorAdd(dataManager, clazz, pageFactory);
		EditorUpdate update = new EditorUpdate(dataManager, clazz, pageFactory);
		EditorDelete delete = new EditorDelete(dataManager, clazz, pageFactory);
		return Arrays.asList(parent, addPage, infoPage, add, update, delete);
	}

}
