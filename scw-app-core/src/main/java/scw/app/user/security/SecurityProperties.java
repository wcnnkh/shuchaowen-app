package scw.app.user.security;

import scw.beans.annotation.ConfigurationProperties;
import scw.beans.annotation.Value;

@ConfigurationProperties(prefix = "security.")
public class SecurityProperties {
	public static final String ADMIN_CONTROLLER = "${security.controller:/admin}";

	@Value(ADMIN_CONTROLLER)
	private String controller;

	private String toLoginPath;

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public String getToLoginPath() {
		return toLoginPath == null ? (controller + "/to_login") : toLoginPath;
	}

	public void setToLoginPath(String toLoginPath) {
		this.toLoginPath = toLoginPath;
	}
}
