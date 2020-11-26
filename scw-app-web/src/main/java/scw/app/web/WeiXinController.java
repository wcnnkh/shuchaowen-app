package scw.app.web;

import java.util.Map;

import scw.app.enums.SexType;
import scw.app.user.enums.AccountType;
import scw.app.user.enums.UnionIdType;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginRequired;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.core.utils.StringUtils;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;
import scw.mvc.annotation.Controller;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.security.session.UserSession;
import scw.tencent.wx.Scope;
import scw.tencent.wx.UserAccessToken;
import scw.tencent.wx.UserGrantClient;
import scw.tencent.wx.Userinfo;
import scw.tencent.wx.WeiXinUtils;
import scw.tencent.wx.miniprogram.PhoneNumber;
import scw.tencent.wx.miniprogram.Session;
import scw.tencent.wx.miniprogram.WeiXinMiniprogramUtils;
import scw.tencent.wx.miniprogram.WeixinMiniprogram;

@Controller(value = "weixin", methods = { HttpMethod.GET, HttpMethod.POST })
@scw.mvc.annotation.FactoryResult
public class WeiXinController {
	public static final String WX_XCX_SESSION_KEY = "wx.xcx.session.key";
	
	@Autowired(required=false)
	private UserGrantClient userGrantClient;
	private UserService userService;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private UserControllerService userControllerService;
	@Autowired(required=false)
	private WeixinMiniprogram weixinMiniprogram;

	public WeiXinController(UserService userService) {
		this.userService = userService;
	}

	@Controller(value = "login")
	public Result login(String code, Scope scope, HttpChannel httpChannel) {
		if(userGrantClient == null){
			return resultFactory.error("暂不支持微信登录");
		}
		
		if (StringUtils.isEmpty(code)) {
			return resultFactory.parameterError();
		}

		UserAccessToken userAccessToken = userGrantClient.getAccessToken(code, null);
		User user = userService.getUserByUnionId(userAccessToken.getOpenid(), UnionIdType.WX_OPENID);
		if (user == null) {
			UserAttributeModel userAttributeModel = new UserAttributeModel();
			if (scope == Scope.USERINFO) {
				Userinfo userinfo = WeiXinUtils.getUserinfo(userAccessToken.getOpenid(),
						userAccessToken.getToken().getToken());
				userAttributeModel.setSex(SexType.forValue(userinfo.getSex()));
				userAttributeModel.setNickname(userinfo.getNickname());
				if (StringUtils.isNotEmpty(userinfo.getHeadimgurl())) {
					userAttributeModel.setHeadImg(StringUtils.split(userinfo.getHeadimgurl(), ",")[0]);
				}
			}

			DataResult<User> result = userService.registerUnionId(UnionIdType.WX_OPENID, userAccessToken.getOpenid(),
					null, userAttributeModel);
			if (result.isError()) {
				return result;
			}
			user = result.getData();
		}

		Map<String, Object> infoMap = userControllerService.login(user, httpChannel);
		return resultFactory.success(infoMap);
	}

	@LoginRequired
	@Controller(value = "bind")
	public Result bind(long uid, String code, Scope scope) {
		if(userGrantClient == null){
			return resultFactory.error("暂不支持微信绑定");
		}
		
		UserAccessToken userAccessToken = userGrantClient.getAccessToken(code, null);
		User user = userService.getUserByUnionId(userAccessToken.getOpenid(), UnionIdType.WX_OPENID);
		if (user == null) {
			return resultFactory.error("未注册，无法绑定");
		}
		
		Result result = userService.bindUnionId(uid, UnionIdType.WX_OPENID, userAccessToken.getOpenid());
		if(result.isError()){
			return result;
		}

		UserAttributeModel userAttributeModel = new UserAttributeModel();
		if (scope == Scope.USERINFO) {
			Userinfo userinfo = WeiXinUtils.getUserinfo(userAccessToken.getOpenid(),
					userAccessToken.getToken().getToken());
			userAttributeModel.setSex(SexType.forValue(userinfo.getSex()));
			userAttributeModel.setNickname(userinfo.getNickname());
			userAttributeModel.setHeadImg(StringUtils.split(userinfo.getHeadimgurl(), ",")[0]);
			return userService.updateUserAttribute(uid, userAttributeModel);
		}
		
		return resultFactory.success(user);
	}
	
	@Controller(value="/xcx/login")
	public Result xcx_login(String code, HttpChannel httpChannel){
		if(weixinMiniprogram == null){
			return resultFactory.error("暂不支持小程序登录");
		}
		
		Session session = weixinMiniprogram.code2session(code);
		if(session.isError()){
			return resultFactory.error(session.getErrmsg());
		}

		User user = userService.getUserByUnionId(session.getOpenid(), UnionIdType.WX_XCX_OPENID);
		if(user == null){
			//不存在，注册一下
			DataResult<User> dataResult = userService.registerUnionId(UnionIdType.WX_XCX_OPENID, session.getOpenid(), null, null);
			if(dataResult.isError()){
				return dataResult;
			}
			
			user = dataResult.getData();
		}
		
		Map<String, Object> result = userControllerService.login(user, httpChannel);
		UserSession<Long> userSession = httpChannel.getUserSession(Long.class);
		if(userSession != null){
			userSession.setAttribute(WX_XCX_SESSION_KEY, session.getSession_key());
		}
		return resultFactory.success(result);
	}
	
	@LoginRequired
	@Controller(value="/xcx/getPhoneNumber")
	public Result xcx_bind(String encryptedData, String iv, UserSession<Long> userSession){
		if(StringUtils.isEmpty(encryptedData, iv)){
			return resultFactory.parameterError();
		}
		
		String sessionKey = (String) userSession.getAttribute(WX_XCX_SESSION_KEY);
		if(sessionKey == null){
			return resultFactory.error("系统错误(sessionKey)");
		}
		
		PhoneNumber phoneNumber = WeiXinMiniprogramUtils.decrypt(encryptedData, sessionKey, iv);
		DataResult<User> dataResult = userService.bind(userSession.getUid(), AccountType.PHONE, phoneNumber.getPhoneNumber());
		if(dataResult.isError()){
			return dataResult;
		}

		return resultFactory.success();
	}
}
