package scw.app.admin.model;

import java.io.Serializable;

import scw.sql.orm.annotation.Index;

public class AdminRoleModel implements Serializable {
	private static final long serialVersionUID = 1L;
	@Index
	private String userName;
	private String password;
	private String nickName;
	private int groupId;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
}
