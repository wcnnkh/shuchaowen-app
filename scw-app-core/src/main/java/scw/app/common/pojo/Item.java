package scw.app.common.pojo;

import java.io.Serializable;
import java.util.Set;

import scw.app.common.model.ItemDrop;
import scw.mapper.MapperUtils;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

@Table
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private int id;
	private String name;
	private String describe;
	private Set<ItemDrop> itemDrops;// 使用后的掉落

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ItemDrop> getItemDrops() {
		return itemDrops;
	}

	public void setItemDrops(Set<ItemDrop> itemDrops) {
		this.itemDrops = itemDrops;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
