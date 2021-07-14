package scw.app.editable.test.ediable;

import java.io.Serializable;

import scw.app.editable.annotation.Editable;
import scw.app.editable.annotation.Image;
import scw.app.editable.annotation.Select;
import scw.lang.Description;
import scw.orm.annotation.PrimaryKey;
import scw.orm.sql.annotation.Table;

@Table
@Editable(name = "测试栏目")
public class TestInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Description("ID")
	@PrimaryKey
	private String id;
	@Description("值")
	private String value;

	@Select(TestOptions.class)
	private Integer selectValue;

	@Image(width = 400, height = 200)
	private String image;
	@Image(multiple = true)
	private String images;

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public Integer getSelectValue() {
		return selectValue;
	}

	public void setSelectValue(Integer selectValue) {
		this.selectValue = selectValue;
	}
}
