package scw.app.admin.web;

import scw.core.instance.annotation.SPI;
import scw.http.server.resource.DefaultStaticResourceLoader;
import scw.http.server.resource.StaticResourceHttpServiceHandler;

@SPI
public class AdminWebResourceHttpServiceHandler extends StaticResourceHttpServiceHandler {

	public AdminWebResourceHttpServiceHandler() {
		DefaultStaticResourceLoader resourceLoader = new DefaultStaticResourceLoader();
		resourceLoader.addMapping("/", "/admin/js/*", "/admin/css/*", "/admin/fonts/*", "/admin/images/*",
				"/admin/lib/*");
		setResourceLoader(resourceLoader);
	}
}
