package scw.app.editable.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import scw.app.editable.DataManager;
import scw.app.editable.Editor;
import scw.app.editable.annotation.Editable;
import scw.app.editable.annotation.Image;
import scw.app.editable.annotation.Select;
import scw.app.editable.form.ImageInput;
import scw.app.editable.form.Input;
import scw.app.editable.form.SelectInput;
import scw.app.user.security.SecurityProperties;
import scw.core.annotation.AnnotatedElementUtils;
import scw.http.HttpMethod;
import scw.mapper.Field;
import scw.mapper.FieldFeature;
import scw.mapper.MapperUtils;
import scw.mvc.HttpChannel;
import scw.orm.annotation.PrimaryKey;
import scw.util.Pagination;
import scw.web.model.Page;

public class EditorParent implements Editor {
	private final Class<?> editableClass;
	private final DataManager dataManager;
	private final SecurityProperties securityProperties;

	public EditorParent(DataManager dataManager, Class<?> editableClass, SecurityProperties securityProperties) {
		this.editableClass = editableClass;
		this.dataManager = dataManager;
		this.securityProperties = securityProperties;
	}

	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public Class<?> getEditableClass() {
		return editableClass;
	}

	public DataManager getDataManager() {
		return dataManager;
	}

	@Override
	public String getPath() {
		return securityProperties.getController() + "/" + editableClass.getName() + "/root";
	}

	@Override
	public String getMethod() {
		return HttpMethod.GET.name();
	}

	@Override
	public String getId() {
		return editableClass.getName() + "#root";
	}

	@Override
	public String getParentId() {
		return null;
	}

	@Override
	public String getName() {
		Editable editable = editableClass.getAnnotation(Editable.class);
		return editable == null ? getId() : editable.name();
	}

	@Override
	public boolean isMenu() {
		return true;
	}

	@Override
	public Map<String, String> getAttributeMap() {
		return Collections.emptyMap();
	}

	@Override
	public Object doAction(HttpChannel httpChannel) {
		Object requestBean = httpChannel.getInstance(editableClass);
		Integer page = httpChannel.getValue("page").getAsInteger();
		if (page == null || page < 1) {
			page = 1;
		}

		Integer limit = httpChannel.getValue("limit").getAsInteger();
		if (limit == null || limit < 1) {
			limit = 10;
		}

		Pagination<Object> pagination = dataManager.list(editableClass, requestBean, page, limit);
		Page view = new Page("/editable/list.ftl");
		int maxPage = pagination == null ? 1 : pagination.getMaxPage();
		int currentPage = Math.min(page, maxPage);
		view.put("page", currentPage);
		view.put("limit", limit);
		view.put("list", pagination == null ? null : pagination.getData());
		view.put("totalCount", pagination == null ? 0 : pagination.getTotalCount());
		view.put("query", requestBean);
		view.put("fields", getInputs(requestBean));
		view.put("maxPage", maxPage);
		view.put("name", getName());
		return view;
	}

	private Input createInput(Object query, Field field) {
		Select selects = field.getAnnotation(Select.class);
		if (selects != null) {
			SelectInput select = new SelectInput(selects);
			select.setOptions(dataManager.queryOptions(selects.value(), null));
			return select;
		}

		Image images = field.getAnnotation(Image.class);
		if (images != null) {
			return new ImageInput(images);
		}

		return new Input();
	}

	@Override
	public List<Input> getInputs(Object query) {
		List<Input> list = new ArrayList<Input>();
		for (Field field : MapperUtils.getMapper().getFields(editableClass).accept(FieldFeature.EXISTING_GETTER_FIELD)
				.accept(FieldFeature.EXISTING_SETTER_FIELD).accept(FieldFeature.IGNORE_STATIC)) {
			Input input = createInput(query, field);
			input.setName(field.getGetter().getName());
			input.setDescribe(AnnotatedElementUtils.getDescription(field));
			input.setPrimaryKey(field.getGetter().isAnnotationPresent(PrimaryKey.class));
			if (input.getDescribe() == null) {
				input.setDescribe(input.getName());
			}
			list.add(input);
		}
		return list;
	}

	@Override
	public String toString() {
		return editableClass.getName() + "[id=" + getId() + ", parentId=" + getParentId() + ", path=" + getPath()
				+ ", method=" + getMethod() + ", name=" + getName() + "]";
	}
}
