package scw.app.address.pojo;

import scw.app.address.model.AddressModel;
import scw.mapper.MapperUtils;
import scw.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

@Table
public class Address extends AddressModel implements Cloneable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private int id;
	@PrimaryKey
	private int parentId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	@Override
	public Address clone() {
		try {
			return (Address) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().getFields(Address.class).getValueMap(this).toString();
	}
}
