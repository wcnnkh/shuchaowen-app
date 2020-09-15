package scw.app.admin.web;

import scw.core.instance.annotation.Configuration;
import scw.http.server.resource.DefaultStaticResourceLoader;
import scw.http.server.resource.StaticResourceHttpServiceHandler;

@Configuration(order = Integer.MIN_VALUE)
public class AdminWebResourceHttpServiceHandler extends StaticResourceHttpServiceHandler {

	public AdminWebResourceHttpServiceHandler() {
		DefaultStaticResourceLoader resourceLoader = new DefaultStaticResourceLoader();
		resourceLoader.addMapping("/", "/admin/js/*", "/admin/css/*", "/admin/fonts/*", "/admin/images/*",
				"/admin/lib/*");
		setResourceLoader(resourceLoader);
	}
}
