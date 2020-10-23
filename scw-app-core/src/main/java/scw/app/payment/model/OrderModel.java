package scw.app.payment.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import scw.app.address.model.UserAddressModel;
import scw.app.payment.enums.PaymentMethod;
import scw.core.utils.StringUtils;
import scw.sql.orm.annotation.Column;
import scw.sql.orm.annotation.Index;
import scw.value.EmptyValue;
import scw.value.StringValue;
import scw.value.Value;

public class OrderModel extends UserAddressModel {
	private static final long serialVersionUID = 1L;
	@Index
	private int group;
	private String name;// 订单名称
	private long uid;// 订单所属用户
	private int payChannel;// 支付方式
	private int price;// 实付价格
	private String ip;// 创建订单的ip
	private String wxOpenid;// 如果是微信支付，那么应该存在openid
	@Column(type = "text")
	private Map<String, String> metaData;

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(int payChannel) {
		this.payChannel = payChannel;
	}

	public PaymentMethod getPaymentMethod() {
		return PaymentMethod.forChannel(payChannel);
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getPriceDescribe() {
		return StringUtils.formatNothingToYuan(price);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getWxOpenid() {
		return wxOpenid;
	}

	public void setWxOpenid(String wxOpenid) {
		this.wxOpenid = wxOpenid;
	}
	
	public Map<String, String> getMetaData() {
		if (metaData == null) {
			return Collections.emptyMap();
		}
		return Collections.unmodifiableMap(metaData);
	}

	public void setMetaData(Map<String, String> metaData) {
		this.metaData = metaData;
	}

	public Value getMetaData(String name) {
		if (metaData == null) {
			return EmptyValue.INSTANCE;
		}

		String value = metaData.get(name);
		return value == null ? EmptyValue.INSTANCE : new StringValue(value);
	}

	public void setMetaData(String name, String value) {
		Map<String, String> map = metaData == null ? new HashMap<String, String>()
				: new HashMap<String, String>(metaData);
		map.put(name, value);
		setMetaData(map);
	}
}
