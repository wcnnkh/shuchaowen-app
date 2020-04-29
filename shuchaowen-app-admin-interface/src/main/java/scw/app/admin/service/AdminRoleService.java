package scw.app.admin.service;

import java.util.List;

import scw.app.admin.model.AdminRoleModel;
import scw.app.admin.pojo.AdminRole;
import scw.core.GlobalPropertyFactory;
import scw.core.Pagination;
import scw.util.result.DataResult;

public interface AdminRoleService {
	public static String DEFAULT_ADMIN_NAME = GlobalPropertyFactory
			.getInstance()
			.getValue("scw.admin.username", String.class, "admin");
	public static String DEFAULT_PASSWORD = GlobalPropertyFactory.getInstance()
			.getValue("scw.admin.password", String.class, "123456");
	public static String DEFAULT_NICKNAME = GlobalPropertyFactory.getInstance()
			.getValue("scw.admin.nickname", String.class, "超级管理员");

	AdminRole getById(int id);

	AdminRole check(String userName, String password);

	DataResult<AdminRole> create(AdminRoleModel adminRoleModel);

	DataResult<AdminRole> update(int id, AdminRoleModel adminRoleModel);

	Pagination<List<AdminRole>> getPagination(String userName, String nickName,
			int page, int limit);
}
