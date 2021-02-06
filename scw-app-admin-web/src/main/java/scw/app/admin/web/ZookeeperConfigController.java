package scw.app.admin.web;

import java.util.HashMap;
import java.util.Map;

import scw.beans.annotation.Autowired;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.core.utils.StringUtils;
import scw.http.HttpMethod;
import scw.mvc.annotation.ActionAuthority;
import scw.mvc.annotation.Controller;
import scw.mvc.page.Page;
import scw.mvc.page.PageFactory;
import scw.mvc.view.View;
import scw.value.Value;
import scw.zookeeper.ZookeeperCloudPropertyFactory;

@Controller(value= AdminConstants.ADMIN_CONTROLLER_PREFIX + "/zookeeper/config", methods={HttpMethod.GET, HttpMethod.POST})
public class ZookeeperConfigController {
	private final ZookeeperCloudPropertyFactory cloudPropertyFactory;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private PageFactory pageFactory;
	
	public ZookeeperConfigController(ZookeeperCloudPropertyFactory cloudPropertyFactory){
		this.cloudPropertyFactory = cloudPropertyFactory;
	}
	
	@ActionAuthority(menu=true, value="配置中心(zookeeper)")
	@Controller(value="list")
	public View list(){
		Page page = pageFactory.getPage("/admin/ftl/config_list.ftl");
		Map<String, String> configMap = new HashMap<String, String>();
		for(String key : cloudPropertyFactory){
			Value value = cloudPropertyFactory.getValue(key);
			if(value == null){
				continue;
			}
			
			String text = value.getAsString();
			if(text == null){
				continue;
			}
			
			text = text.replaceAll("\\n", "<br/>");
			configMap.put(key, text);
		}
		page.put("configMap", configMap);
		return page;
	}
	
	@ActionAuthority(value="添加/修改配置界面(zookeeper)")
	@Controller(value="view")
	public View view(String key){
		Page page = pageFactory.getPage("/admin/ftl/config_view.ftl");
		page.put("key", key);
		page.put("value", cloudPropertyFactory.getValue(key).getAsString());
		return page;
	}
	
	@ActionAuthority(value="添加/修改配置操作(zookeeper)")
	@Controller(value="save_or_update")
	public Result saveOrUpdate(String key, String value){
		if(StringUtils.isEmpty(key, value)){
			return resultFactory.parameterError();
		}
		
		cloudPropertyFactory.put(key, value);
		return resultFactory.success();
	}
	
	@ActionAuthority(value="删除配置操作(zookeeper)")
	@Controller(value="delete")
	public Result delete(String key){
		if(StringUtils.isEmpty(key)){
			return resultFactory.parameterError();
		}
		
		cloudPropertyFactory.remove(key);
		return resultFactory.success();
	}
}
