package scw.app.editable;

import java.io.Serializable;
import java.util.List;

public class FieldInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String describe;
	private List<SelectOptions> options;
	private boolean required;
	private boolean primaryKey;
	private InputType inputType = InputType.INPUT;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public List<SelectOptions> getOptions() {
		return options;
	}

	public void setOptions(List<SelectOptions> options) {
		this.options = options;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public InputType getInputType() {
		return inputType;
	}

	public void setInputType(InputType inputType) {
		this.inputType = inputType;
	}
}
