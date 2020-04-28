package scw.app.admin.service.impl;

import java.util.List;

import scw.app.admin.model.AdminRoleModel;
import scw.app.admin.pojo.AdminRole;
import scw.app.admin.service.AdminRoleService;
import scw.beans.annotation.Autowired;
import scw.core.Pagination;
import scw.core.instance.annotation.Configuration;
import scw.core.reflect.CloneUtils;
import scw.db.DB;
import scw.sql.SimpleSql;
import scw.sql.Sql;
import scw.util.result.DataResult;
import scw.util.result.ResultFactory;

@Configuration
public class AdminRoleServiceImpl extends BaseImpl implements AdminRoleService {
	@Autowired
	private ResultFactory resultFactory;

	public AdminRoleServiceImpl(DB db) {
		super(db);
	}

	public AdminRole getById(int id) {
		return db.getById(AdminRole.class, id);
	}

	public AdminRole check(String userName, String password) {
		Sql sql = new SimpleSql(
				"select * from admin_role where userName=? and password=?",
				userName, password);
		return db.selectOne(AdminRole.class, sql);
	}

	public DataResult<AdminRole> create(AdminRoleModel adminRoleModel) {
		AdminRole adminRole = CloneUtils.copy(adminRoleModel, AdminRole.class);
		return resultFactory.success(adminRole);
	}

	public DataResult<AdminRole> update(int id, AdminRoleModel adminRoleModel) {
		AdminRole adminRole = getById(id);
		if (adminRole == null) {
			return resultFactory.error("不存在的用户");
		}

		CloneUtils.copy(adminRoleModel, adminRole);
		db.update(adminRole);
		return resultFactory.success(adminRole);
	}

	public Pagination<List<AdminRole>> getPagination(String userName,
			String nickName, int page, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

}
