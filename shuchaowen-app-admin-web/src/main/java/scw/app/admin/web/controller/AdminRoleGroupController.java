package scw.app.admin.web.controller;

import java.util.Collection;
import java.util.List;

import scw.app.admin.pojo.AdminRole;
import scw.app.admin.pojo.AdminRoleGroup;
import scw.app.admin.service.AdminRoleGroupActionService;
import scw.app.admin.service.AdminRoleGroupService;
import scw.app.admin.service.AdminRoleService;
import scw.app.admin.web.AdminActionFilter;
import scw.app.admin.web.AdminLogin;
import scw.beans.annotation.Autowired;
import scw.core.annotation.KeyValuePair;
import scw.mvc.action.authority.HttpActionAuthorityManager;
import scw.mvc.annotation.Authority;
import scw.mvc.annotation.AuthorityParent;
import scw.mvc.annotation.Controller;
import scw.mvc.annotation.ResultFactory;
import scw.mvc.exception.TapeDescriptionException;
import scw.net.http.HttpMethod;

@AuthorityParent(AdminRoleController.class)
@AdminLogin
@Controller(value = "/admin/group/")
@ResultFactory
public class AdminRoleGroupController {
	@Autowired
	private AdminRoleGroupService adminRoleGroupService;
	@Autowired
	private AdminRoleGroupActionService adminRoleGroupActionService;
	@Autowired
	private AdminRoleService adminRoleService;
	@Autowired
	private HttpActionAuthorityManager httpActionAuthorityManager;

	@Authority(value = "管理员权限组", menu = true, attributes = { @KeyValuePair(key = AdminActionFilter.ROUTE_ATTR_NAME, value = "ManagementAuthority") })
	@Controller(value = "list")
	public Collection<AdminRoleGroup> list(int uid) {
		AdminRole adminRole = adminRoleService.getById(uid);
		if(adminRole == null){
			throw new TapeDescriptionException("系统错误，用户不存在");
		}
		
		return adminRoleGroupService.getSubList(adminRole.getGroupId());
	}
	
	@Authority("创建/更新管理员权限组")
	@Controller(value = "create_or_update", methods = HttpMethod.POST)
	public void create(int id, String name, List<String> actionIds) {
		//TODO
	}

	@Authority("删除管理员权限组")
	@Controller(value = "delete", methods = HttpMethod.POST)
	public void delete(int groupId) {
		//TODO
	}
}
