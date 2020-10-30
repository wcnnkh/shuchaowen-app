package scw.app.user.pojo;

import scw.app.user.model.UserAttributeModel;
import scw.app.util.RegexUtils;
import scw.core.utils.StringUtils;
import scw.core.utils.XTime;
import scw.mapper.MapperUtils;
import scw.sql.orm.annotation.Column;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;
import scw.sql.orm.support.generation.annotation.Generator;

@Table
public class User extends UserAttributeModel {
	private static final long serialVersionUID = 1L;
	@Generator
	@PrimaryKey
	private long uid;
	private long cts;
	@Column(unique = true, length = 32)
	private String username;
	private String password;
	private long lastUpdatePasswordTime;
	@Column(unique = true, length = 16)
	private String phone;
	@Column(unique = true)
	private String email;
	@Column(unique = true, length = 64)
	private String openidForWX;
	@Column(unique = true, length = 64)
	private String openidForQQ;
	private int permissionGroupId;// 权限组id
	private boolean disable;// 是否禁用
	private long lastLoginTime;
	private long defaultAddressId;// 用户默认收货地址

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getCts() {
		return cts;
	}

	public String getCtsDescribe() {
		return XTime.format(cts, "yyyy-MM-dd HH:mm:ss");
	}

	public void setCts(long cts) {
		this.cts = cts;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOpenidForWX() {
		return openidForWX;
	}

	public void setOpenidForWX(String openidForWX) {
		this.openidForWX = openidForWX;
	}

	public String getOpenidForQQ() {
		return openidForQQ;
	}

	public void setOpenidForQQ(String openidForQQ) {
		this.openidForQQ = openidForQQ;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPermissionGroupId() {
		return permissionGroupId;
	}

	public void setPermissionGroupId(int permissionGroupId) {
		this.permissionGroupId = permissionGroupId;
	}

	public long getLastUpdatePasswordTime() {
		return lastUpdatePasswordTime;
	}

	public void setLastUpdatePasswordTime(long lastUpdatePasswordTime) {
		this.lastUpdatePasswordTime = lastUpdatePasswordTime;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginTimeDescribe() {
		return XTime.format(lastLoginTime, "yyyy-MM-dd HH:mm:ss");
	}

	public long getDefaultAddressId() {
		return defaultAddressId;
	}

	public void setDefaultAddressId(long defaultAddressId) {
		this.defaultAddressId = defaultAddressId;
	}

	public String getAvailableNickname() {
		if (StringUtils.isNotEmpty(getNickname())) {
			return getNickname();
		}

		if (StringUtils.isNotEmpty(phone)) {
			return RegexUtils.hidePhone(phone);
		}

		if (StringUtils.isNotEmpty(username)) {
			return getUsername();
		}
		return null;
	}

	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
