package scw.app.admin.web;

import scw.core.instance.annotation.Configuration;
import scw.http.server.resource.StaticResourceHttpServiceHandler;

@Configuration(order = Integer.MIN_VALUE + 1)
public class AdminWebResourceHttpServiceHandler extends StaticResourceHttpServiceHandler {

	public AdminWebResourceHttpServiceHandler() {
		super("/", "/admin/js/*", "/admin/css/*", "/admin/fonts/*", "/admin/images/*", "/admin/lib/*");
	}
}
