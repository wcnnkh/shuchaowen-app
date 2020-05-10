package scw.app.admin.service;

import java.util.Collection;
import java.util.List;

import scw.app.admin.pojo.AdminRoleGroup;
import scw.result.Result;

public interface AdminRoleGroupService {
	AdminRoleGroup getById(int id);

	List<AdminRoleGroup> getSubList(int id);

	Result createOrUpdate(int id, String name, Collection<String> authorityIds);
}
