package scw.app.product.pojo;

import java.io.Serializable;

import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

@Table
public class ProductMapping implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private long spuId;
	@PrimaryKey
	private long skuId;

	public long getSpuId() {
		return spuId;
	}

	public void setSpuId(long spuId) {
		this.spuId = spuId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}
}
