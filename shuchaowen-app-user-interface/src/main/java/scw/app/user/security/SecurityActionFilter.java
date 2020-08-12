package scw.app.user.security;

import scw.app.user.pojo.User;
import scw.app.user.service.PermissionGroupActionService;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.core.GlobalPropertyFactory;
import scw.core.instance.annotation.Configuration;
import scw.http.HttpCookie;
import scw.http.HttpUtils;
import scw.mvc.HttpChannel;
import scw.mvc.action.Action;
import scw.mvc.action.ActionFilter;
import scw.mvc.annotation.ActionAuthority;
import scw.mvc.security.HttpActionAuthorityManager;
import scw.result.ResultFactory;
import scw.security.authority.http.HttpAuthority;
import scw.security.login.UserToken;
import scw.value.StringValue;
import scw.value.Value;
import scw.value.property.PropertyFactory.DynamicValue;

@Configuration
public class SecurityActionFilter implements ActionFilter {
	public static final DynamicValue<String> TOKEN_NAME = GlobalPropertyFactory.getInstance()
			.getDynamicValue("user.security.token.name", String.class, "token");
	public static final DynamicValue<String> UID_NAME = GlobalPropertyFactory.getInstance()
			.getDynamicValue("user.security.uid.name", String.class, "uid");

	@Autowired
	private LoginManager loginManager;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private PermissionGroupActionService permissionGroupActionService;
	@Autowired
	private HttpActionAuthorityManager httpActionAuthorityManager;
	@Autowired
	private UserService userService;

	public Object doFilter(Action action, HttpChannel httpChannel) throws Throwable {
		LoginRequired required = action.getAnnotatedElement().getAnnotation(LoginRequired.class);
		ActionAuthority actionAuthority = action.getMethodAnnotatedElement().getAnnotation(ActionAuthority.class);
		if (actionAuthority != null || (required != null && required.value())) {
			Value token = getParameter(httpChannel, TOKEN_NAME.getValue());
			if (token == null || token.isEmpty()) {
				return resultFactory.authorizationFailure();
			}

			UserToken<Long> userToken = loginManager.getToken(LoginType.User, token.getAsString());
			if (userToken == null) {
				return resultFactory.authorizationFailure();
			}

			Value uid = getParameter(httpChannel, UID_NAME.getValue());
			if (uid == null || uid.isEmpty()) {
				return resultFactory.success();
			}

			if (!userToken.getUid().equals(uid.getAsLong())) {
				return resultFactory.authorizationFailure();
			}

			if (actionAuthority != null) {
				User user = userService.getUser(userToken.getUid());
				if (user == null) {
					return resultFactory.authorizationFailure();
				}

				if (!UserService.ADMIN_NAME.equals(user.getUsername())) {
					HttpAuthority httpAuthority = httpActionAuthorityManager.getAuthority(action);
					if (httpAuthority == null) {
						httpChannel.getLogger().warn("not found autority: {}", action);
					} else {
						if (!permissionGroupActionService.check(user.getPermissionGroupId(), httpAuthority.getId())) {
							return resultFactory.error("权限不足");
						}
					}
				}
			}
		}
		return action.doAction(httpChannel);
	}

	protected Value getParameter(HttpChannel httpChannel, String name) {
		Value value = httpChannel.getValue(name);
		if (value == null || value.isEmpty()) {
			String token = httpChannel.getRequest().getHeaders().getFirst(name);
			if (token == null) {
				HttpCookie httpCookie = HttpUtils.getCookie(httpChannel.getRequest(), name);
				if (httpCookie != null) {
					token = httpCookie.getValue();
				}
			}

			if (token != null) {
				value = new StringValue(token);
			}
		}
		return value;
	}
}
