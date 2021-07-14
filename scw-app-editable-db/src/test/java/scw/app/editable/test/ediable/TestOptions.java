package scw.app.editable.test.ediable;

import scw.app.editable.annotation.Editable;
import scw.app.editable.annotation.SelectOption;
import scw.orm.annotation.PrimaryKey;
import scw.orm.sql.annotation.Table;

@Table
@Editable(name = "测试选项")
public class TestOptions {
	@PrimaryKey
	private Integer id;
	@SelectOption
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
