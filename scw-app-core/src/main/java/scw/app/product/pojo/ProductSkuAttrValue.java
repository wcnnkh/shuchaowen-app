package scw.app.product.pojo;

import java.io.Serializable;

import scw.mapper.MapperUtils;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

@Table
public class ProductSkuAttrValue implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private long id;
	@PrimaryKey
	private long attrNameId;
	private String value;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAttrNameId() {
		return attrNameId;
	}

	public void setAttrNameId(long attrNameId) {
		this.attrNameId = attrNameId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
