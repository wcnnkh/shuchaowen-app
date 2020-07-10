package scw.app.user.service.impl;

import scw.app.common.BaseServiceImpl;
import scw.app.common.enums.SexType;
import scw.app.user.enums.UnionIdType;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.User;
import scw.app.user.pojo.WeiXinUserInfo;
import scw.app.user.service.UserService;
import scw.app.user.service.WeiXinService;
import scw.core.instance.annotation.Configuration;
import scw.db.DB;
import scw.mapper.Copy;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.security.login.LoginService;
import scw.security.login.UserToken;
import scw.tencent.wx.Scope;
import scw.tencent.wx.UserAccessToken;
import scw.tencent.wx.UserGrantClient;
import scw.tencent.wx.Userinfo;
import scw.tencent.wx.WeiXinUtils;

@Configuration(order = Integer.MIN_VALUE)
public class WeiXinServiceImpl extends BaseServiceImpl implements WeiXinService {
	protected final UserGrantClient userGrantClient;
	private UserService userService;
	private LoginService<Long> loginService;

	public WeiXinServiceImpl(DB db, ResultFactory resultFactory, UserGrantClient userGrantClient,
			UserService userService, LoginService<Long> loginService) {
		super(db, resultFactory);
		this.userGrantClient = userGrantClient;
		this.loginService = loginService;
		this.userService = userService;
	}

	public UserToken<Long> login(String code, Scope scope) {
		UserAccessToken userAccessToken = userGrantClient.getAccessToken(code, null);
		User user = userService.getUser(UnionIdType.wx_openid, userAccessToken.getOpenid());
		if (user == null) {
			WeiXinUserInfo weiXinUserInfo = new WeiXinUserInfo();
			UserAttributeModel userAttributeModel = new UserAttributeModel();
			if (scope == Scope.USERINFO) {
				Userinfo userinfo = WeiXinUtils.getUserinfo(userAccessToken.getOpenid(),
						userAccessToken.getAccessToken().getToken());
				Copy.copy(weiXinUserInfo, userinfo);
				userAttributeModel.setSex(SexType.forValue(userinfo.getSex()));
			} else {
				weiXinUserInfo.setOpenid(userAccessToken.getOpenid());
			}

			DataResult<User> result = userService.register(UnionIdType.wx_openid, userAccessToken.getOpenid(), null,
					userAttributeModel);
			if (result.isError()) {
				throw new RuntimeException("register error: " + result);
			}
			user = result.getData();
			weiXinUserInfo.setUid(user.getUid());
			db.saveOrUpdate(weiXinUserInfo);
		}
		return loginService.login(user.getUid());
	}

	public WeiXinUserInfo getUserInfo(long uid) {
		return db.getById(WeiXinUserInfo.class, uid);
	}

	public Result bind(long uid, String code, Scope scope) {
		UserAccessToken userAccessToken = userGrantClient.getAccessToken(code, null);
		User user = userService.getUser(UnionIdType.wx_openid, userAccessToken.getOpenid());
		if (user == null) {
			return resultFactory.error("未注册，无法绑定");
		}

		WeiXinUserInfo weiXinUserInfo = getUserInfo(uid);
		if (weiXinUserInfo == null) {
			weiXinUserInfo = new WeiXinUserInfo();
			weiXinUserInfo.setUid(uid);
		}

		UserAttributeModel userAttributeModel = new UserAttributeModel();
		if (scope == Scope.USERINFO) {
			Userinfo userinfo = WeiXinUtils.getUserinfo(userAccessToken.getOpenid(),
					userAccessToken.getAccessToken().getToken());
			Copy.copy(weiXinUserInfo, userinfo);
			userAttributeModel.setSex(SexType.forValue(userinfo.getSex()));
			Result result = userService.updateUserAttribute(uid, userAttributeModel);
			if (result.isError()) {
				return result;
			}
		} else {
			weiXinUserInfo.setOpenid(userAccessToken.getOpenid());
		}
		db.saveOrUpdate(weiXinUserInfo);
		return userService.bind(uid, UnionIdType.wx_openid, weiXinUserInfo.getOpenid()).result();
	}

}
