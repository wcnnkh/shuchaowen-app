package scw.app.editable.form;

import java.util.List;

import scw.util.Pair;

public class SelectInput extends Input {
	private static final long serialVersionUID = 1L;
	private List<Pair<String, String>> options;

	public SelectInput() {
		super(InputType.SELECT);
	}

	public List<Pair<String, String>> getOptions() {
		return options;
	}

	public void setOptions(List<Pair<String, String>> options) {
		this.options = options;
	}
}
