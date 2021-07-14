package scw.app.user.service.impl;

import java.util.Collection;

import scw.app.event.AppEvent;
import scw.app.event.AppEventDispatcher;
import scw.app.user.enums.AccountType;
import scw.app.user.enums.UnionIdType;
import scw.app.user.model.AdminUserModel;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.UidToUnionId;
import scw.app.user.pojo.UnionIdToUid;
import scw.app.user.pojo.User;
import scw.app.user.service.PermissionGroupService;
import scw.app.user.service.UserService;
import scw.app.util.BaseServiceConfiguration;
import scw.app.vc.service.PhoneVerificationCodeService;
import scw.beans.annotation.Autowired;
import scw.beans.annotation.Service;
import scw.codec.Encoder;
import scw.codec.support.CharsetCodec;
import scw.context.result.DataResult;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.core.utils.CollectionUtils;
import scw.core.utils.StringUtils;
import scw.db.DB;
import scw.env.Environment;
import scw.env.Sys;
import scw.event.EventType;
import scw.sql.SimpleSql;
import scw.sql.Sql;
import scw.sql.SqlUtils;
import scw.sql.WhereSql;
import scw.util.Pagination;

@Service
public class UserServiceImpl extends BaseServiceConfiguration implements UserService {
	public static String ADMIN_NAME = Sys.env.getValue("scw.admin.username", String.class, "admin");

	private static final Encoder<String, String> PASSWORD_ENCODER = CharsetCodec.UTF_8.toMD5();

	@Autowired(required = false)
	private PhoneVerificationCodeService verificationCodeService;
	@Autowired
	private PermissionGroupService permissionGroupService;
	@Autowired
	private AppEventDispatcher appEventDispatcher;

	public UserServiceImpl(DB db, ResultFactory resultFactory, Environment environment) {
		super(db, resultFactory);
		db.createTable(User.class, false);
		User user = getUserByAccount(AccountType.USERNAME, ADMIN_NAME);
		if (user == null) {
			AdminUserModel adminUserModel = new AdminUserModel();
			adminUserModel.setUsername(ADMIN_NAME);
			adminUserModel.setNickname(environment.getValue("scw.admin.nickname", String.class, "超级管理员"));
			adminUserModel.setPassword(environment.getValue("scw.admin.password", String.class, "123456"));
			createOrUpdateAdminUser(0, adminUserModel);
		}
	}

	public User getUser(long uid) {
		return db.getById(User.class, uid);
	}

	private String formatPassword(String password) {
		if (StringUtils.isEmpty(password)) {
			return null;
		}

		return PASSWORD_ENCODER.encode(password);
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
		user.setLastUpdatePasswordTime(Sys.currentTimeMillis());
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
		return db.paginationQuery(User.class, sql.assembleSql("select * from user", null), page, limit);
	}

	public DataResult<User> createOrUpdateAdminUser(long uid, AdminUserModel adminUserModel) {
		if (StringUtils.isEmpty(adminUserModel.getUsername(), adminUserModel.getNickname())) {
			return resultFactory.error("参数错误");
		}

		User username = getUserByAccount(AccountType.USERNAME, adminUserModel.getUsername());
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

	public User getUserByAccount(AccountType type, String account) {
		return getUserByAccount(type, account, null);
	}

	public User getUserByAccount(AccountType type, String account, String password) {
		Sql sql;
		if (StringUtils.isNotEmpty(password)) {
			sql = new SimpleSql("select * from user where " + type.getFieldName() + "=? and password=?", account,
					password);
		} else {
			sql = new SimpleSql("select * from user where " + type.getFieldName() + "=?", account);
		}
		return db.query(User.class, sql).first();
	}

	public DataResult<User> register(AccountType accountType, String account, String password,
			UserAttributeModel userAttributeModel) {
		User user = getUserByAccount(accountType, account);
		if (user != null) {
			return resultFactory.error("账号已经存在");
		}

		user = new User();
		accountType.setAccount(user, account);
		user.setPassword(formatPassword(password));
		user.setCts(Sys.currentTimeMillis());

		if (userAttributeModel != null) {
			userAttributeModel.writeTo(user);
		}
		db.save(user);
		appEventDispatcher.publishEvent(User.class, new AppEvent<User>(user, EventType.CREATE));
		return resultFactory.success(user);
	}

	public DataResult<User> bind(long uid, AccountType accountType, String account) {
		User user = getUserByAccount(accountType, account);
		if (user != null) {
			return resultFactory.error("账号已被绑定");
		}

		user = getUser(uid);
		if (user == null) {
			return resultFactory.error("账号不存在");
		}

		if (account.equals(accountType.getAccount(user))) {
			return resultFactory.error("与原绑定信息一致");
		}

		accountType.setAccount(user, account);
		db.update(user);
		return resultFactory.success(user);
	}

	public DataResult<User> registerUnionId(int unionIdType, String unionId, String password,
			UserAttributeModel userAttributeModel) {
		UnionIdToUid unionIdToUid = getUidByUnionId(unionId, unionIdType);
		if (unionIdToUid != null) {
			return resultFactory.error("账号已经存在");
		}

		User user = new User();
		user.setPassword(formatPassword(password));
		user.setCts(Sys.currentTimeMillis());

		if (userAttributeModel != null) {
			userAttributeModel.writeTo(user);
		}
		db.save(user);

		unionIdToUid = new UnionIdToUid();
		unionIdToUid.setType(unionIdType);
		unionIdToUid.setUid(user.getUid());
		unionIdToUid.setUnionId(unionId);
		db.save(unionIdToUid);

		UidToUnionId uidToUnionId = new UidToUnionId();
		uidToUnionId.setUnionId(unionId);
		uidToUnionId.setType(unionIdType);
		uidToUnionId.setUid(user.getUid());
		db.save(unionIdToUid);

		appEventDispatcher.publishEvent(User.class, new AppEvent<User>(user, EventType.CREATE));
		return resultFactory.success(user);
	}

	public Result bindUnionId(long uid, int unionIdType, String unionId) {
		UnionIdToUid unionIdToUid = getUidByUnionId(unionId, unionIdType);
		if (unionIdToUid != null) {
			return resultFactory.error("账号已被绑定");
		}

		unionIdToUid = new UnionIdToUid();
		unionIdToUid.setUid(uid);
		unionIdToUid.setType(unionIdType);
		unionIdToUid.setUnionId(unionId);
		db.save(unionIdToUid);

		UidToUnionId uidToUnionId = getUnionIdByUid(uid, unionIdType);
		if (uidToUnionId == null) {
			uidToUnionId = new UidToUnionId();
			uidToUnionId.setUid(uid);
			uidToUnionId.setType(unionIdType);
			uidToUnionId.setUnionId(unionId);
			db.save(uidToUnionId);
		} else {
			if (uidToUnionId.getUnionId().equals(unionId)) {
				return resultFactory.error("与原绑定信息一致");
			}

			UnionIdToUid old = getUidByUnionId(uidToUnionId.getUnionId(), unionIdType);
			if (old != null) {
				db.delete(old);
			}

			uidToUnionId.setUnionId(unionId);
			db.update(uidToUnionId);
		}
		return resultFactory.success();
	}

	public User getUserByUnionId(String unionId, UnionIdType type) {
		return getUserByUnionId(unionId, type.getValue());
	}

	public User getUserByUnionId(String unionId, int type) {
		UnionIdToUid unionIdToUid = getUidByUnionId(unionId, type);
		return unionIdToUid == null ? null : getUser(unionIdToUid.getUid());
	}

	public UidToUnionId getUnionIdByUid(long uid, int type) {
		return db.getById(UidToUnionId.class, uid, type);
	}

	public UnionIdToUid getUidByUnionId(String unionId, int type) {
		return db.getById(UnionIdToUid.class, unionId, type);
	}

	public DataResult<User> registerUnionId(UnionIdType type, String unionId, String password,
			UserAttributeModel userAttributeModel) {
		return registerUnionId(type.getValue(), unionId, password, userAttributeModel);
	}

	public Result bindUnionId(long uid, UnionIdType type, String unionId) {
		return bindUnionId(uid, type.getValue(), unionId);
	}

	public UidToUnionId getUnionIdByUid(long uid, UnionIdType type) {
		return getUnionIdByUid(uid, type.getValue());
	}

	public UnionIdToUid getUidByUnionId(String unionId, UnionIdType type) {
		return getUidByUnionId(unionId, type.getValue());
	}
}
