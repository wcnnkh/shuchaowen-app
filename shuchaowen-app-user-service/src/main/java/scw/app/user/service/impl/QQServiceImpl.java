package scw.app.user.service.impl;

import scw.app.common.BaseServiceImpl;
import scw.app.common.enums.SexType;
import scw.app.user.enums.UnionIdType;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.QQUserInfo;
import scw.app.user.pojo.User;
import scw.app.user.service.QQService;
import scw.app.user.service.UserService;
import scw.core.utils.StringUtils;
import scw.db.DB;
import scw.mapper.Copy;
import scw.oauth2.AccessToken;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.security.login.LoginService;
import scw.security.login.UserToken;
import scw.tencent.qq.Configuration;
import scw.tencent.qq.QQUtils;
import scw.tencent.qq.Userinfo;

@scw.core.instance.annotation.Configuration(order = Integer.MIN_VALUE)
public class QQServiceImpl extends BaseServiceImpl implements QQService {
	private final Configuration configuration;
	private final UserService userService;
	private final LoginService<Long> loginService;

	public QQServiceImpl(DB db, ResultFactory resultFactory, Configuration configuration, UserService userService,
			LoginService<Long> loginService) {
		super(db, resultFactory);
		this.configuration = configuration;
		this.userService = userService;
		this.loginService = loginService;
	}

	public UserToken<Long> login(String openid, String accessToken) {
		if (StringUtils.isEmpty(openid, accessToken)) {
			throw new RuntimeException("参数错误");
		}

		User user = userService.getUser(UnionIdType.qq_openid, openid);
		if (user == null) {
			QQUserInfo qqUserInfo = new QQUserInfo();
			Userinfo userinfo = QQUtils.getUserinfo(configuration.getAppId(), accessToken, openid);
			Copy.copy(qqUserInfo, userinfo);

			UserAttributeModel userAttributeModel = new UserAttributeModel();
			userAttributeModel.setSex(SexType.forDescribe(qqUserInfo.getGender()));
			DataResult<User> dataResult = userService.register(UnionIdType.qq_openid, openid, null, userAttributeModel);
			if (dataResult.isError()) {
				throw new RuntimeException("register error:" + dataResult);
			}

			user = dataResult.getData();
			qqUserInfo.setUid(user.getUid());
			db.saveOrUpdate(qqUserInfo);
		}
		return loginService.login(user.getUid());
	}

	public UserToken<Long> webLogin(String code, String redirect_uri) {
		if (StringUtils.isEmpty(code, redirect_uri)) {
			throw new RuntimeException("参数错误");
		}

		AccessToken accessToken = QQUtils.getAccessToken(configuration.getAppId(), configuration.getAppKey(),
				redirect_uri, code);
		String openid = QQUtils.getOpenId(accessToken.getAccessToken().getToken());
		return login(openid, accessToken.getAccessToken().getToken());
	}

	public Result bind(long uid, String openid, String accessToken) {
		if (StringUtils.isEmpty(openid, accessToken)) {
			return resultFactory.parameterError();
		}

		User user = userService.getUser(UnionIdType.qq_openid, openid);
		if (user == null) {
			return resultFactory.error("用户不存在");
		}

		QQUserInfo qqUserInfo = getUserInfo(uid);
		if (qqUserInfo == null) {
			qqUserInfo = new QQUserInfo();
			qqUserInfo.setUid(uid);
		}

		Userinfo userinfo = QQUtils.getUserinfo(configuration.getAppId(), accessToken, openid);
		Copy.copy(qqUserInfo, userinfo);
		db.saveOrUpdate(userinfo);

		UserAttributeModel userAttributeModel = new UserAttributeModel();
		userAttributeModel.setSex(SexType.forDescribe(userinfo.getGender()));
		Result result = userService.updateUserAttribute(uid, userAttributeModel);
		if (result.isError()) {
			return result;
		}
		return userService.bind(uid, UnionIdType.qq_openid, openid);
	}

	public Result webBind(long uid, String code, String redirect_uri) {
		if (StringUtils.isEmpty(code, redirect_uri)) {
			return resultFactory.parameterError();
		}

		AccessToken accessToken = QQUtils.getAccessToken(configuration.getAppId(), configuration.getAppKey(),
				redirect_uri, code);
		String openid = QQUtils.getOpenId(accessToken.getAccessToken().getToken());
		return bind(uid, openid, accessToken.getAccessToken().getToken());
	}

	public QQUserInfo getUserInfo(long uid) {
		return db.getById(QQUserInfo.class, uid);
	}
}
