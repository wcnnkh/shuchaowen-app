package scw.app.editable.form;

import java.util.List;

import scw.app.editable.annotation.Select;
import scw.util.Pair;

public class SelectInput extends Input {
	private static final long serialVersionUID = 1L;
	private List<Pair<String, String>> options;
	private Class<?> queryClass;

	public SelectInput() {
		super(InputType.SELECT);
	}
	
	public SelectInput(Select select) {
		this();
		this.queryClass = select.value();
	}

	public List<Pair<String, String>> getOptions() {
		return options;
	}

	public void setOptions(List<Pair<String, String>> options) {
		this.options = options;
	}

	public Class<?> getQueryClass() {
		return queryClass;
	}

	public void setQueryClass(Class<?> queryClass) {
		this.queryClass = queryClass;
	}
}
