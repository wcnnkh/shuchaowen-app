package scw.app.admin.model;

import java.io.Serializable;

import scw.app.admin.pojo.AdminRole;
import scw.security.token.UserToken;

public class AdminLoginInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private UserToken<Integer> userToken;
	private AdminRole role;

	public UserToken<Integer> getUserToken() {
		return userToken;
	}

	public void setUserToken(UserToken<Integer> userToken) {
		this.userToken = userToken;
	}

	public AdminRole getRole() {
		return role;
	}

	public void setRole(AdminRole role) {
		this.role = role;
	}
}
