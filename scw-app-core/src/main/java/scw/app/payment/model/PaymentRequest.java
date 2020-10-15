package scw.app.payment.model;

import scw.app.address.model.UserAddressModel;
import scw.app.payment.enums.PaymentMethod;
import scw.core.utils.StringUtils;

public class PaymentRequest extends UserAddressModel {
	private static final long serialVersionUID = 1L;
	private String name;// 订单名称
	private long uid;// 订单所属用户
	private int payChannel;// 支付方式
	private int price;// 实付价格
	private String ip;// 创建订单的ip
	private String wxOpenid;// 如果是微信支付，那么应该存在openid

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
}
