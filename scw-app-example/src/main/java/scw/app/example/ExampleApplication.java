package scw.app.example;

import scw.application.MainApplication;
import scw.beans.annotation.Bean;
import scw.core.GlobalPropertyFactory;
import scw.http.server.cors.Cors;
import scw.http.server.cors.CorsRegistry;
import scw.http.server.resource.DefaultStaticResourceLoader;
import scw.http.server.resource.StaticResourceLoader;
import scw.upload.kind.DefaultKindEditor;
import scw.upload.kind.KindEditor;

public class ExampleApplication {

	public static void main(String[] args) {
		MainApplication.run(ExampleApplication.class);
	}

	@Bean
	public CorsRegistry getCorsRegistry() {
		CorsRegistry corsRegistry = new CorsRegistry();
		corsRegistry.addMapping("/*", Cors.DEFAULT);
		return corsRegistry;
	}

	@Bean
	public KindEditor getKindEditor() {
		return new DefaultKindEditor(GlobalPropertyFactory.getInstance().getWorkPath() + "/upload/",
				"http://localhost:8080/upload/");
	}

	@Bean
	public StaticResourceLoader getStaticResourceLoader() {
		DefaultStaticResourceLoader resourceLoader = new DefaultStaticResourceLoader();
		resourceLoader.addMapping("/", "/upload/*");
		return resourceLoader;
	}
}
