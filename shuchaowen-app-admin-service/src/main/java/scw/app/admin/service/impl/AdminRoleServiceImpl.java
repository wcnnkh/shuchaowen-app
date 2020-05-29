package scw.app.admin.service.impl;

import scw.app.admin.model.AdminRoleModel;
import scw.app.admin.pojo.AdminRole;
import scw.app.admin.service.AdminRoleGroupService;
import scw.app.admin.service.AdminRoleService;
import scw.beans.annotation.Autowired;
import scw.beans.annotation.InitMethod;
import scw.beans.annotation.Service;
import scw.core.utils.StringUtils;
import scw.db.DB;
import scw.mapper.Copy;
import scw.result.DataResult;
import scw.result.ResultFactory;
import scw.security.SignatureUtils;
import scw.sql.SimpleSql;
import scw.sql.Sql;
import scw.sql.SqlUtils;
import scw.sql.WhereSql;
import scw.util.Pagination;

@Service
public class AdminRoleServiceImpl extends BaseImpl implements AdminRoleService {
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private AdminRoleGroupService adminRoleGroupService;

	public AdminRoleServiceImpl(DB db) {
		super(db);
	}

	@InitMethod
	private void init() {
		if (!isExist(DEFAULT_ADMIN_NAME)) {
			AdminRoleModel adminRoleModel = new AdminRoleModel();
			adminRoleModel.setPassword(SignatureUtils.md5(DEFAULT_PASSWORD,
					"UTF-8"));
			adminRoleModel.setNickName(DEFAULT_NICKNAME);
			adminRoleModel.setGroupId(0);
			adminRoleModel.setUserName(DEFAULT_ADMIN_NAME);
			create(adminRoleModel);
		}
	}

	public AdminRole getById(int id) {
		return db.getById(AdminRole.class, id);
	}

	public AdminRole check(String userName, String password) {
		Sql sql = new SimpleSql(
				"select * from admin_role where userName=? and password=?",
				userName, SignatureUtils.md5(password, "UTF-8"));
		return db.selectOne(AdminRole.class, sql);
	}

	private boolean isExist(String userName) {
		return db.selectOne(int.class, new SimpleSql(
				"select count(0) from admin_role where userName=?", userName)) != 0;
	}

	public DataResult<AdminRole> create(AdminRoleModel adminRoleModel) {
		if (isExist(adminRoleModel.getUserName())) {
			return resultFactory.error("账号已存在");
		}

		AdminRole adminRole = Copy.copy(AdminRole.class, adminRoleModel);
		db.save(adminRole);
		return resultFactory.success(adminRole);
	}

	public DataResult<AdminRole> update(int id, AdminRoleModel adminRoleModel) {
		AdminRole adminRole = getById(id);
		if (adminRole == null) {
			return resultFactory.error("不存在的用户");
		}

		Copy.copy(adminRoleModel, adminRole);
		db.update(adminRole);
		return resultFactory.success(adminRole);
	}

	public Pagination<AdminRole> getAdminRolePagination(int groupId,
			int page, int limit, String userName, String nickName) {
		WhereSql sql = new WhereSql();
		if (StringUtils.isNotEmpty(userName)) {
			sql.and("userName like " + SqlUtils.toLikeValue(userName));
		}

		if (StringUtils.isNotEmpty(nickName)) {
			sql.and("nickName like " + SqlUtils.toLikeValue(nickName));
		}

		sql.andIn("groupId", adminRoleGroupService.getAllSubList(groupId));
		return db.select(AdminRole.class, page, limit,
				sql.assembleSql("select * from admin_role", null));
	}

}
