package scw.app.user.enums;

import scw.app.user.pojo.User;
import scw.lang.NotSupportedException;

public enum OpenidType {
	WX, QQ;

	public void setOpenid(User user, String openid) {
		switch (this) {
		case WX:
			user.setOpenidForWX(openid);
			break;
		case QQ:
			user.setOpenidForQQ(openid);
			break;
		default:
			throw new NotSupportedException("不支持的openid类型：" + this);
		}
	}
}
