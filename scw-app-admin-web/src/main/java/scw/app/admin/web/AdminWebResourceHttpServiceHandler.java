package scw.app.admin.web;

import scw.context.annotation.Provider;
import scw.web.resource.DefaultStaticResourceLoader;
import scw.web.resource.StaticResourceHttpServiceHandler;

@Provider
public class AdminWebResourceHttpServiceHandler extends StaticResourceHttpServiceHandler {

	public AdminWebResourceHttpServiceHandler() {
		DefaultStaticResourceLoader resourceLoader = new DefaultStaticResourceLoader();
		resourceLoader.addMapping("/", "/admin/js/*", "/admin/css/*", "/admin/fonts/*", "/admin/images/*",
				"/admin/lib/*");
		setResourceLoader(resourceLoader);
	}
}
