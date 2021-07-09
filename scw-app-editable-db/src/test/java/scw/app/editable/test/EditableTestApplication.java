package scw.app.editable.test;

import scw.beans.annotation.Bean;
import scw.boot.support.MainApplication;
import scw.db.DB;
import scw.env.Sys;
import scw.mvc.annotation.Controller;
import scw.sqlite.SQLiteDB;
import scw.upload.Uploader;
import scw.web.cors.Cors;
import scw.web.cors.CorsRegistry;

public class EditableTestApplication {
	
	public static void main(String[] args) {
		MainApplication.run(EditableTestApplication.class, args);
	}

	/**
	 * 示例Controller
	 * 
	 * @return
	 */
	@Controller(value = "hello")
	public String helloworld() {
		return "hello world";
	}

	/**
	 * 使用内嵌的sqlite为示例数据库
	 * 
	 * @return
	 */
	@Bean
	public DB getDB() {
		return new SQLiteDB(Sys.env.getWorkPath() + "/scw-app-example.db");
	}

	/**
	 * 注册跨域路径
	 * 
	 * @return
	 */
	@Bean
	public CorsRegistry getCorsRegistry() {
		CorsRegistry corsRegistry = new CorsRegistry();
		corsRegistry.addMapping("/**", Cors.DEFAULT);
		return corsRegistry;
	}
	
	@Bean
	public Uploader getUploader() {
		return new Uploader(Sys.env.getWorkPath() + "/upload");
	}
}
