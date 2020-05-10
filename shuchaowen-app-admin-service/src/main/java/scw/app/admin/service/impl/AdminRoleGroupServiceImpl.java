package scw.app.admin.service.impl;

import java.util.Collection;
import java.util.List;

import scw.app.admin.pojo.AdminRoleGroup;
import scw.app.admin.service.AdminRoleGroupActionService;
import scw.app.admin.service.AdminRoleGroupService;
import scw.beans.annotation.Service;
import scw.db.DB;
import scw.result.Result;
import scw.sql.SimpleSql;

@Service
public class AdminRoleGroupServiceImpl extends BaseImpl implements AdminRoleGroupService {
	private AdminRoleGroupActionService adminRoleGroupActionService;
	
	public AdminRoleGroupServiceImpl(DB db, AdminRoleGroupActionService adminRoleGroupActionService) {
		super(db);
		this.adminRoleGroupActionService = adminRoleGroupActionService;
	}

	public AdminRoleGroup getById(int id) {
		return db.getById(AdminRoleGroup.class, id);
	}
	
	public Result createOrUpdate(int id, String name,
			Collection<String> authorityIds) {
		AdminRoleGroup adminRoleGroup = getById(id);
		if(adminRoleGroup == null){
			adminRoleGroup = new AdminRoleGroup();
			adminRoleGroup.setId(id);
		}
		adminRoleGroup.setName(name);
		db.saveOrUpdate(adminRoleGroup);
		return adminRoleGroupActionService.setActions(id, authorityIds);
	}

	public List<AdminRoleGroup> getSubList(int id) {
		return db.select(AdminRoleGroup.class, new SimpleSql("select * from admin_role_group where parentId=?", id));
	}
}
