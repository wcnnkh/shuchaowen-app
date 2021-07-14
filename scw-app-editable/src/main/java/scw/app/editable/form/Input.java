package scw.app.editable.form;

import java.io.Serializable;

public class Input implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String type;
	private String name;
	private String describe;
	private boolean required;
	private boolean primaryKey;

	public Input() {
		this(InputType.INPUT);
	}

	protected Input(InputType type) {
		this.type = type.name();
	}

	protected Input(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

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

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
}
