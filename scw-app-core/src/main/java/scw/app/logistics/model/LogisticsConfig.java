package scw.app.logistics.model;

import java.io.Serializable;

public class LogisticsConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 物流公司
	 */
	private String name;
	/**
	 * 物流单号
	 */
	private String orderNo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}
