package scw.app.user.service.impl;

import java.util.Collection;

import scw.app.event.AppEvent;
import scw.app.event.AppEventDispatcher;
import scw.app.user.enums.OpenidType;
import scw.app.user.model.AdminUserModel;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.User;
import scw.app.user.service.PermissionGroupService;
import scw.app.user.service.UserService;
import scw.app.util.BaseServiceConfiguration;
import scw.app.vc.service.PhoneVerificationCodeService;
import scw.beans.annotation.Autowired;
import scw.core.GlobalPropertyFactory;
import scw.core.instance.annotation.Configuration;
import scw.core.utils.CollectionUtils;
import scw.core.utils.StringUtils;
import scw.db.DB;
import scw.event.support.EventType;
import scw.lang.NotSupportedException;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.security.SignatureUtils;
import scw.sql.SimpleSql;
import scw.sql.Sql;
import scw.sql.SqlUtils;
import scw.sql.WhereSql;
import scw.util.Pagination;
import scw.value.property.PropertyFactory;

@Configuration(order = Integer.MIN_VALUE)
public class UserServiceImpl extends BaseServiceConfiguration implements UserService {
	public static String ADMIN_NAME = GlobalPropertyFactory.getInstance().getValue("scw.admin.username", String.class,
			"admin");

	@Autowired(required = false)
	private PhoneVerificationCodeService verificationCodeService;
	@Autowired
	private PermissionGroupService permissionGroupService;
	@Autowired
	private AppEventDispatcher appEventDispatcher;

	public UserServiceImpl(DB db, ResultFactory resultFactory, PropertyFactory propertyFactory) {
		super(db, resultFactory);
		db.createTable(User.class, false);
		User user = getUserByUsername(ADMIN_NAME);
		if (user == null) {
			AdminUserModel adminUserModel = new AdminUserModel();
			adminUserModel.setUsername(ADMIN_NAME);
			adminUserModel.setNickname(propertyFactory.getValue("scw.admin.nickname", String.class, "超级管理员"));
			adminUserModel.setPassword(propertyFactory.getValue("scw.admin.password", String.class, "123456"));
			createOrUpdateAdminUser(0, adminUserModel);
		}
	}

	public User getUser(long uid) {
		return db.getById(User.class, uid);
	}

	public User getUserByPhone(String phone) {
		Sql sql = new SimpleSql("select * from user where phone=?", phone);
		return db.selectOne(User.class, sql);
	}

	public User getUserByUsername(String username) {
		Sql sql = new SimpleSql("select * from user where username=?", username);
		return db.selectOne(User.class, sql);
	}

	public User getUserByOpenid(OpenidType type, String openid) {
		Sql sql;
		switch (type) {
		case WX:
			sql = new SimpleSql("select * from user where openidForWX=?", openid);
			break;
		case QQ:
			sql = new SimpleSql("select * from user where openidForQQ=?", openid);
			break;
		default:
			return null;
		}
		return db.selectOne(User.class, sql);
	}

	private String formatPassword(String password) {
		if (StringUtils.isEmpty(password)) {
			return null;
		}

		return SignatureUtils.md5(password, "UTF-8");
	}

	public DataResult<User> registerByUsername(String username, String password,
			UserAttributeModel userAttributeModel) {
		User user = getUserByUsername(username);
		if (user != null) {
			return resultFactory.error("账号已经存在");
		}

		user = new User();
		user.setUsername(username);
		user.setPassword(formatPassword(password));
		user.setCts(System.currentTimeMillis());

		if (userAttributeModel != null) {
			userAttributeModel.writeTo(user);
		}
		db.save(user);
		appEventDispatcher.publishEvent(User.class, new AppEvent<User>(user, EventType.CREATE));
		return resultFactory.success(user);
	}

	public DataResult<User> registerByPhone(String phone, String password, UserAttributeModel userAttributeModel) {
		User user = getUserByPhone(phone);
		if (user != null) {
			return resultFactory.error("账号已经存在");
		}

		user = new User();
		user.setPhone(phone);
		user.setPassword(formatPassword(password));
		user.setCts(System.currentTimeMillis());

		if (userAttributeModel != null) {
			userAttributeModel.writeTo(user);
		}
		db.save(user);
		appEventDispatcher.publishEvent(User.class, new AppEvent<User>(user, EventType.CREATE));
		return resultFactory.success(user);
	}

	private void setOpenid(User user, OpenidType type, String openid) {
		switch (type) {
		case WX:
			user.setOpenidForWX(openid);
			break;
		case QQ:
			user.setOpenidForQQ(openid);
			break;
		default:
			throw new NotSupportedException("不支持的openid类型：" + type);
		}
	}

	public DataResult<User> registerByOpenid(OpenidType type, String openid, UserAttributeModel userAttributeModel) {
		User user = getUserByOpenid(type, openid);
		if (user != null) {
			return resultFactory.error("账号已经存在");
		}

		user = new User();
		user.setCts(System.currentTimeMillis());
		setOpenid(user, type, openid);
		if (userAttributeModel != null) {
			userAttributeModel.writeTo(user);
		}
		db.save(user);
		appEventDispatcher.publishEvent(User.class, new AppEvent<User>(user, EventType.CREATE));
		return resultFactory.success(user);
	}

	public DataResult<User> bindPhone(long uid, String phone) {
		User user = getUserByPhone(phone);
		if (user != null) {
			return resultFactory.error("手机号已被绑定");
		}

		user = getUser(uid);
		if (user == null) {
			return resultFactory.error("账号不存在");
		}

		if (phone.equals(user.getPhone())) {
			return resultFactory.error("与原绑定手机号一致");
		}

		user.setPhone(phone);
		db.update(user);
		return resultFactory.success(user);
	}

	public DataResult<User> bindOpenid(long uid, OpenidType type, String openid,
			UserAttributeModel userAttributeModel) {
		User user = getUserByOpenid(type, openid);
		if (user != null) {
			return resultFactory.error("openid已被绑定");
		}

		user = getUser(uid);
		if (user == null) {
			return resultFactory.error("账号不存在");
		}

		setOpenid(user, type, openid);
		if (userAttributeModel != null) {
			userAttributeModel.writeTo(user);
		}
		db.update(user);
		return resultFactory.success(user);
	}

	public Result updateUserAttribute(long uid, UserAttributeModel userAttributeModel) {
		User user = getUser(uid);
		if (user == null) {
			return resultFactory.error("用户不存在");
		}

		if (userAttributeModel != null) {
			userAttributeModel.writeTo(user);
			db.update(user);
		}
		return resultFactory.success();
	}

	public Result checkPassword(long uid, String password) {
		if (StringUtils.isEmpty(password)) {
			return resultFactory.parameterError();
		}

		User user = getUser(uid);
		if (user == null) {
			return resultFactory.error("账号不存在");
		}

		if (!formatPassword(password).equals(user.getPassword())) {
			return resultFactory.error("账号或密码错误");
		}
		return resultFactory.success();
	}

	public Result updatePassword(long uid, String password) {
		if (StringUtils.isEmpty(password)) {
			return resultFactory.parameterError();
		}

		User user = getUser(uid);
		if (user == null) {
			return resultFactory.error("账号不存在");
		}

		user.setPassword(formatPassword(password));
		user.setLastUpdatePasswordTime(System.currentTimeMillis());
		db.update(user);
		return resultFactory.success();
	}

	public boolean isSuperAdmin(long uid) {
		User user = getUser(uid);
		if (user == null) {
			return false;
		}

		if (ADMIN_NAME.equals(user.getUsername())) {
			return true;
		}

		return false;
	}

	public Pagination<User> search(Collection<Integer> permissionGroupIds, String search, int page, int limit) {
		WhereSql sql = new WhereSql();
		sql.and("permissionGroupId>0");
		if (!CollectionUtils.isEmpty(permissionGroupIds)) {
			sql.andIn("permissionGroupId", permissionGroupIds);
		}

		if (StringUtils.isNotEmpty(search)) {
			String value = SqlUtils.toLikeValue(search);
			sql.and("(uid=? or phone like ? or username like ? or nickname like ?)", search, value, value, value);
		}
		return db.select(User.class, page, limit, sql.assembleSql("select * from user", null));
	}

	public DataResult<User> createOrUpdateAdminUser(long uid, AdminUserModel adminUserModel) {
		if (StringUtils.isEmpty(adminUserModel.getUsername(), adminUserModel.getNickname())) {
			return resultFactory.error("参数错误");
		}

		User username = getUserByUsername(adminUserModel.getUsername());
		User user = getUser(uid);
		if (user == null) {
			if (username != null) {
				return resultFactory.error("账号已经存在");
			}

			user = new User();
			if (StringUtils.isEmpty(adminUserModel.getPassword())) {
				return resultFactory.error("密码不能为空");
			}
			user.setPassword(formatPassword(adminUserModel.getPassword()));
			user.setUsername(adminUserModel.getUsername());
		} else {
			if (!adminUserModel.getUsername().equals(user.getUsername())) {
				if (username != null) {
					return resultFactory.error("账号已经存在");
				}

				user.setUsername(adminUserModel.getUsername());
			}

			if (StringUtils.isNotEmpty(adminUserModel.getPassword())) {
				user.setPassword(formatPassword(adminUserModel.getPassword()));
			}
		}

		user.setNickname(adminUserModel.getNickname());
		user.setDisable(adminUserModel.isDisable());
		user.setPermissionGroupId(adminUserModel.getGroupId());
		db.saveOrUpdate(user);
		return resultFactory.success(user);
	}

	public Result updateDefaultAddressId(long uid, long addressId) {
		User user = getUser(uid);
		if (user == null) {
			return resultFactory.error("启用不存在");
		}

		user.setDefaultAddressId(addressId);
		db.update(user);
		return resultFactory.success();
	}
}
