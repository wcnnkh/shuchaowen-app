package scw.app.admin.web.controller;

import scw.app.admin.service.AdminRoleService;
import scw.app.admin.web.AdminActionFilter;
import scw.app.admin.web.AdminLogin;
import scw.beans.annotation.Autowired;
import scw.core.annotation.KeyValuePair;
import scw.core.parameter.annotation.DefaultValue;
import scw.mvc.annotation.Authority;
import scw.mvc.annotation.Controller;
import scw.mvc.annotation.ResultFactory;
import scw.net.http.HttpMethod;

@Authority(value = "系统设置", menu = true)
@AdminLogin
@Controller(value = "/admin/user")
@ResultFactory
public class AdminRoleController {
	@Autowired
	private AdminRoleService adminRoleService;

	@Authority(value = "管理员列表", menu = true, attributes = { @KeyValuePair(key = AdminActionFilter.ROUTE_ATTR_NAME, value = "ManagementList") })
	@Controller("list")
	public void list(@DefaultValue("1") Integer page,
			@DefaultValue("10") Integer limit, String userName, String nickName) {
	}

	@Authority("创建/更新管理员信息")
	@Controller(value = "create_or_update", methods = HttpMethod.POST)
	public void create() {
	}

	@Authority("创建管理员")
	@Controller(value = "create", methods = HttpMethod.POST)
	public void update() {
	}
}
