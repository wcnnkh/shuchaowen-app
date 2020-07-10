package scw.app.user.pojo;

import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;
import scw.tencent.qq.Userinfo;

@Table
public class QQUserInfo extends Userinfo{
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private long uid;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
}
