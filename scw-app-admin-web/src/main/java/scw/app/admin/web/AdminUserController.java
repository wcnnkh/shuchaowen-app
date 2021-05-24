package scw.app.admin.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import scw.app.user.model.AdminUserModel;
import scw.app.user.pojo.PermissionGroup;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginRequired;
import scw.app.user.security.SecurityProperties;
import scw.app.user.service.PermissionGroupService;
import scw.app.user.service.UserService;
import scw.context.result.Result;
import scw.core.parameter.annotation.DefaultValue;
import scw.core.utils.CollectionUtils;
import scw.http.HttpMethod;
import scw.mapper.Field;
import scw.mapper.Fields;
import scw.mapper.MapperUtils;
import scw.mvc.annotation.ActionAuthority;
import scw.mvc.annotation.Controller;
import scw.security.session.UserSession;
import scw.util.Pagination;
import scw.web.message.annotation.RequestBody;
import scw.web.model.Page;

@LoginRequired
@ActionAuthority(value = "系统设置", menu = true)
@Controller(value = SecurityProperties.ADMIN_CONTROLLER)
public class AdminUserController {
	private UserService userService;
	private PermissionGroupService permissionGroupService;

	public AdminUserController(UserService userService, PermissionGroupService permissionGroupService) {
		this.userService = userService;
		this.permissionGroupService = permissionGroupService;
	}

	@ActionAuthority(value = "管理员列表", menu = true)
	@Controller(value = "admin_list")
	public Page admin_list(UserSession<Long> requestUser, Integer groupId, @DefaultValue("1") int page, String search,
			@DefaultValue("10") int limit) {
		User currentUser = userService.getUser(requestUser.getUid());
		List<PermissionGroup> userSubGroups = permissionGroupService.getSubList(currentUser.getPermissionGroupId(),
				true);
		List<Integer> groupIds = null;// groupIds如果为空就表示没有数据，如果长度为0就表示全部
		Field groupIdField = MapperUtils.getMapper().getFields(PermissionGroup.class).find("id", null);
		if (groupId == null) {// 全部
			if (userService.isSuperAdmin(requestUser.getUid())) {
				groupIds = Collections.emptyList();
			} else {
				if (!CollectionUtils.isEmpty(userSubGroups)) {
					groupIds = groupIdField.getValues(userSubGroups);
				}
				// else 该分组下没有子分组了
			}
		} else {
			// 如果选择了分组
			List<PermissionGroup> groups = permissionGroupService.getSubList(groupId, true);
			if (!CollectionUtils.isEmpty(groups)) {
				groupIds = groupIdField.getValues(groups);
			}
			// else 该分组下没有子分组了
		}

		Pagination<User> pagination;
		if (groupIds == null) {
			pagination = new Pagination<User>(limit);
		} else {
			pagination = userService.search(groupIds, search, page, limit);
		}

		List<Object> list = new ArrayList<Object>();
		if (pagination.getData() != null) {
			Fields fields = MapperUtils.getMapper().getFields(User.class);
			for (User user : pagination.getData()) {
				PermissionGroup group = permissionGroupService.getById(user.getPermissionGroupId());
				String groupName = group == null ? "系统分组" : group.getName();
				boolean groupDisable = group == null ? false : group.isDisable();
				Map<String, Object> map = fields.getValueMap(user);
				map.put("groupName", groupName + "(状态：" + (!groupDisable ? "可用" : "禁用") + ")");
				list.add(map);
			}
		}

		Page view = new Page("/admin/ftl/admin_list.ftl");
		view.put("adminList", list);
		view.put("page", page);
		view.put("maxPage", pagination.getMaxPage());
		view.put("totalCount", pagination.getTotalCount());
		view.put("groupId", groupId);
		view.put("search", search);
		view.put("groupList", userSubGroups);
		return view;
	}

	@ActionAuthority(value = "(查看/修改)管理员信息界面")
	@Controller(value = "admin_view")
	public Object admin_view(long toUid, UserSession<Long> requestUser) {
		Page page = new Page("/admin/ftl/admin_view.ftl");
		User user = userService.getUser(toUid);
		User currentUser = userService.getUser(requestUser.getUid());
		List<PermissionGroup> groups = permissionGroupService.getSubList(currentUser.getPermissionGroupId(), true);
		page.put("groupList", groups);
		page.put("admin", user);
		return page;
	}

	@ActionAuthority(value = "(查看/修改)管理员信息")
	@Controller(value = "admin_create_or_update", methods = HttpMethod.POST)
	public Result admin_view(UserSession<Long> requestUser, long toUid, @RequestBody AdminUserModel adminUserModel) {
		return userService.createOrUpdateAdminUser(toUid, adminUserModel);
	}
}
