package scw.app.example;

import scw.app.address.service.AddressService;
import scw.application.MainApplication;
import scw.beans.annotation.Autowired;
import scw.beans.annotation.Bean;
import scw.http.server.cors.Cors;
import scw.http.server.cors.CorsRegistry;

public class ExampleApplication {
	@Autowired
	private AddressService addressService;

	public static void main(String[] args) {
		MainApplication.run(ExampleApplication.class);
	}
	
	@Bean
	public CorsRegistry getCorsRegistry(){
		CorsRegistry corsRegistry = new CorsRegistry();
		corsRegistry.addMapping("/*", Cors.DEFAULT);
		return corsRegistry;
	}
}

