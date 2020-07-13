package scw.app.example;

import scw.app.admin.web.AdminRoleFactory;
import scw.app.admin.web.DefaultAdminRoleFactory;
import scw.application.MainApplication;
import scw.beans.annotation.Bean;
import scw.data.TemporaryCache;
import scw.security.login.DefaultLoginService;

public class ExampleApplication {

	public static void main(String[] args) {
		MainApplication.run(ExampleApplication.class);
	}

	@Bean
	public AdminRoleFactory getAdminRoleFactory(TemporaryCache temporaryCache) {
		return new DefaultAdminRoleFactory(
				new DefaultLoginService<Integer>(temporaryCache, 7 * 24 * 60 * 60, "admin:"));
	}
}
