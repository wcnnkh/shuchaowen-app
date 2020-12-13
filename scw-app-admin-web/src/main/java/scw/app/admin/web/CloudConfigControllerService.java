package scw.app.admin.web;

import java.util.HashMap;
import java.util.Map;

import scw.config.CloudPropertyFactory;
import scw.mvc.page.Page;
import scw.mvc.page.PageFactory;

public class CloudConfigControllerService {
	private CloudPropertyFactory cloudPropertyFactory;
	private PageFactory pageFactory;
	
	public CloudConfigControllerService(CloudPropertyFactory cloudPropertyFactory, PageFactory pageFactory){
		this.cloudPropertyFactory = cloudPropertyFactory;
		this.pageFactory = pageFactory;
	}
	
	public Page list(){
		Page page = pageFactory.getPage("/admin/ftl/config_list.ftl");
		Map<String, String> configMap = new HashMap<String, String>();
		for(String key : cloudPropertyFactory){
			configMap.put(key, cloudPropertyFactory.getValue(key).getAsString());
		}
		page.put("configMap", configMap);
		return page;
	}
	
	public Page view(String key){
		Page page = pageFactory.getPage("/admin/ftl/config_view.ftl");
		page.put("key", key);
		page.put("value", cloudPropertyFactory.getValue(key).getAsString());
		return page;
	}
}
