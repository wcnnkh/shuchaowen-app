package scw.app.order.pojo;

import scw.app.address.model.UserAddressModel;
import scw.mapper.MapperUtils;
import scw.sql.orm.annotation.PrimaryKey;

public class Order extends UserAddressModel {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private String id;
	private String parentId;// 父级订单编号
	private String name;// 订单名称
	private String img;// 订单图片
	private long uid;// 订单所属用户
	private int payChannel;// 支付方式
	private int status;// 订单状态
	private int price;// 实付价格
	private String pointLogId;// 积分使用日志id
	private String voucherLogId;// 代金券使用日志id
	private int itemType;// 购买的物品类型
	private long itemId;// 购买的物品id
	private String ip;// 创建订单的ip

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getPointLogId() {
		return pointLogId;
	}

	public void setPointLogId(String pointLogId) {
		this.pointLogId = pointLogId;
	}

	public String getVoucherLogId() {
		return voucherLogId;
	}

	public void setVoucherLogId(String voucherLogId) {
		this.voucherLogId = voucherLogId;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
