package scw.app.user.enums;

import scw.app.user.pojo.User;
import scw.lang.NotSupportedException;

public enum AccountType {
	USERNAME, PHONE, EMAIL;

	public String getAccount(User user) {
		switch (this) {
		case PHONE:
			return user.getPhone();
		case USERNAME:
			return user.getUsername();
		case EMAIL:
			return user.getEmail();
		default:
			throw new NotSupportedException("不支持的account类型：" + this + ", user=" + user);
		}
	}

	public void setAccount(User user, String account) {
		switch (this) {
		case PHONE:
			user.setPhone(account);
			break;
		case USERNAME:
			user.setUsername(account);
			break;
		case EMAIL:
			user.setEmail(account);
			break;
		default:
			throw new NotSupportedException("不支持的account类型：" + this + ", account=" + account);
		}
	}
}
