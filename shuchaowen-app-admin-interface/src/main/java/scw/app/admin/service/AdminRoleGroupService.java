package scw.app.admin.service;

import java.util.List;

import scw.app.admin.model.AdminRoleGroupModel;
import scw.app.admin.model.AdminRoleGroupTree;
import scw.app.admin.pojo.AdminRoleGroup;
import scw.result.DataResult;

public interface AdminRoleGroupService {
	AdminRoleGroup getById(int id);

	List<AdminRoleGroup> getSubList(int id);

	List<AdminRoleGroupTree> getAdminRoleGroupTreeList(int id);

	DataResult<AdminRoleGroup> create(AdminRoleGroupModel adminRoleGroupModel);

	DataResult<AdminRoleGroup> update(int id,
			AdminRoleGroupModel adminRoleGroupModel);
}
