package scw.app.editable.test.ediable;

import java.io.Serializable;

import scw.app.editable.annotation.Editable;
import scw.lang.Description;
import scw.orm.annotation.PrimaryKey;
import scw.orm.sql.annotation.Table;

@Table
@Editable(name = "测试栏目")
public class TestInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	@Description("ID")
	@PrimaryKey
	private String id;
	@Description("值")
	private String value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
