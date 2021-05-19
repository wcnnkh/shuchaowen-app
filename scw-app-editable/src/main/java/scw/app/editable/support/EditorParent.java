package scw.app.editable.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import scw.app.editable.DataManager;
import scw.app.editable.Editor;
import scw.app.editable.FieldInfo;
import scw.app.editable.annotation.Editable;
import scw.app.editable.annotation.SelectOption;
import scw.http.HttpMethod;
import scw.mapper.Field;
import scw.mapper.FieldFeature;
import scw.mapper.MapperUtils;
import scw.mvc.HttpChannel;
import scw.mvc.page.Page;
import scw.mvc.page.PageFactory;
import scw.orm.annotation.PrimaryKey;
import scw.util.Pagination;

public class EditorParent implements Editor {
	private final Class<?> editableClass;
	private final DataManager dataManager;
	private final PageFactory pageFactory;

	public EditorParent(DataManager dataManager, Class<?> editableClass, PageFactory pageFactory) {
		this.editableClass = editableClass;
		this.dataManager = dataManager;
		this.pageFactory = pageFactory;
	}

	public Class<?> getEditableClass() {
		return editableClass;
	}

	public DataManager getDataManager() {
		return dataManager;
	}

	public PageFactory getPageFactory() {
		return pageFactory;
	}

	@Override
	public String getPath() {
		return editableClass.getName() + "/list";
	}

	@Override
	public HttpMethod getMethod() {
		return HttpMethod.GET;
	}

	@Override
	public String getId() {
		return editableClass.getName() + "#list#" + HttpMethod.GET;
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
		return null;
	}

	@Override
	public Object doAction(HttpChannel httpChannel) {
		Object requestBean = httpChannel.getInstanceFactory().getInstance(editableClass);
		Integer page = httpChannel.getValue("page").getAsInteger();
		if (page == null) {
			page = 1;
		}

		Integer limit = httpChannel.getValue("limit").getAsInteger();
		if (limit == null) {
			limit = 10;
		}

		Pagination<Object> pagination = dataManager.list(editableClass, requestBean, page, limit);
		Page view = pageFactory.getPage("/editable/list.ftl");
		view.put("page", page);
		view.put("limit", limit);
		view.put("list", pagination.getData());
		view.put("totalCount", pagination.getTotalCount());
		view.put("query", requestBean);
		view.put("fields", getFieldInfos(requestBean));
		view.put("name", getName());
		return view;
	}

	@Override
	public List<FieldInfo> getFieldInfos(Object query) {
		List<FieldInfo> list = new ArrayList<FieldInfo>();
		for (Field field : MapperUtils.getMapper().getFields(editableClass).accept(FieldFeature.EXISTING_GETTER_FIELD)
				.accept(FieldFeature.EXISTING_SETTER_FIELD).accept(FieldFeature.IGNORE_STATIC)) {
			FieldInfo fieldInfo = new FieldInfo();
			fieldInfo.setName(field.getGetter().getName());
			fieldInfo.setDescribe(field.getGetter().getDescription());
			fieldInfo.setPrimaryKey(field.getGetter().isAnnotationPresent(PrimaryKey.class));
			if (fieldInfo.getDescribe() == null) {
				fieldInfo.setDescribe(fieldInfo.getName());
			}

			SelectOption selectOption = field.getAnnotation(SelectOption.class);
			if (selectOption != null) {
				fieldInfo.setOptions(dataManager.selectOptions(selectOption.value(), query));
			}
			list.add(fieldInfo);
		}
		return list;
	}

	@Override
	public String toString() {
		return editableClass.getName() + "[id=" + getId() + ", parentId=" + getParentId() + ", path=" + getPath()
				+ ", method=" + getMethod() + ", name=" + getName() + "]";
	}
}
