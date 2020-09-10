package scw.app.common.model;

import java.io.Serializable;

import scw.mapper.MapperUtils;

/**
 * 如果itemId相同，那么两个对象相等
 * 
 * @author shuchaowen
 *
 */
public class ItemDrop implements Serializable {
	private static final long serialVersionUID = 1L;
	private int itemId;
	private int weight;
	
	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}

	@Override
	public int hashCode() {
		return Long.hashCode(itemId);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof ItemDrop) {
			return itemId == ((ItemDrop) obj).itemId;
		}

		return false;
	}
}
