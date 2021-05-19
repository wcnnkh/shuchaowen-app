package scw.app.editable.test;

import scw.beans.annotation.Bean;
import scw.boot.support.MainApplication;
import scw.db.DB;
import scw.env.SystemEnvironment;
import scw.http.server.cors.Cors;
import scw.http.server.cors.CorsRegistry;
import scw.mvc.annotation.Controller;
import scw.sqlite.SQLiteDB;

public class EditableTestApplication {
	/**
	 * 启动后访问http://localhost:8080/hello
	 * 
	 * @see ExampleApplication#helloworld()
	 * 
	 *      因为导入了scw-app-admin-web模块，所以可以访问默认的后台管理系统(http://localhost:8080/admin)
	 * @param args
	 */
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
		return new SQLiteDB(SystemEnvironment.getInstance().getWorkPath() + "/scw-app-example.db");
	}

	/**
	 * 注册跨域路径
	 * 
	 * @return
	 */
	@Bean
	public CorsRegistry getCorsRegistry() {
		CorsRegistry corsRegistry = new CorsRegistry();
		corsRegistry.addMapping("/*", Cors.DEFAULT);
		return corsRegistry;
	}
}
