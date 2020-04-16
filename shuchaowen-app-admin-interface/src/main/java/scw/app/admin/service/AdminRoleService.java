package scw.app.admin.service;

import java.util.List;

import scw.app.admin.model.AdminRoleModel;
import scw.app.admin.pojo.AdminRole;
import scw.core.Pagination;
import scw.result.DataResult;

public interface AdminRoleService {
	AdminRole getById(int id);
	
	AdminRole check(String userName, String password);

	DataResult<AdminRole> create(AdminRoleModel adminRoleModel);

	DataResult<AdminRole> update(int id, AdminRoleModel adminRoleModel);

	Pagination<List<AdminRole>> getPagination(String userName, String nickName,
			int page, int limit);
}
