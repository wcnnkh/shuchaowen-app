package scw.app.example;

import scw.application.MainApplication;
import scw.beans.annotation.Bean;
import scw.http.server.cors.Cors;
import scw.http.server.cors.CorsRegistry;

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
}
