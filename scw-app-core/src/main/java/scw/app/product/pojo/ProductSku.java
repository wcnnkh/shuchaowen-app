package scw.app.product.pojo;

import java.io.Serializable;

import scw.mapper.MapperUtils;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

@Table
public class ProductSku implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private long id;
	private long attrValueId;// 规格属性值id
	private long attrNameId;// 规格属性名id
	private String name;
	private long stock;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAttrValueId() {
		return attrValueId;
	}

	public void setAttrValueId(long attrValueId) {
		this.attrValueId = attrValueId;
	}

	public long getAttrNameId() {
		return attrNameId;
	}

	public void setAttrNameId(long attrNameId) {
		this.attrNameId = attrNameId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getStock() {
		return stock;
	}

	public void setStock(long stock) {
		this.stock = stock;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
