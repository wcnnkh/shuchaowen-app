package scw.app.user.security;

import scw.app.user.pojo.PermissionGroup;
import scw.app.user.pojo.User;
import scw.app.user.service.PermissionGroupActionService;
import scw.app.user.service.PermissionGroupService;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.core.annotation.AnnotationUtils;
import scw.core.instance.annotation.Configuration;
import scw.mvc.HttpChannel;
import scw.mvc.action.Action;
import scw.mvc.action.ActionInterceptor;
import scw.mvc.action.ActionInterceptorChain;
import scw.mvc.action.ActionParameters;
import scw.mvc.annotation.ActionAuthority;
import scw.mvc.page.PageFactory;
import scw.mvc.security.HttpActionAuthorityManager;
import scw.mvc.view.Redirect;
import scw.result.ResultFactory;
import scw.security.authority.http.HttpAuthority;
import scw.security.login.UserToken;

@Configuration
public class SecurityActionInterceptor implements ActionInterceptor {
	public static final String ADMIN_LOGIN_PATH = "/admin/to_login";
	public static final String ADMIN_PATH_PREFIX = "/admin";

	@Autowired
	private LoginManager loginManager;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private PermissionGroupActionService permissionGroupActionService;
	@Autowired
	private PermissionGroupService permissionGroupService;
	@Autowired
	private HttpActionAuthorityManager httpActionAuthorityManager;
	@Autowired
	private UserService userService;
	@Autowired
	private PageFactory pageFactory;

	public Object intercept(HttpChannel httpChannel, Action action, ActionParameters parameters,
			ActionInterceptorChain chain) throws Throwable {
		LoginRequired required = AnnotationUtils.getAnnotation(LoginRequired.class, action.getSourceClass(),
				action.getAnnotatedElement());
		ActionAuthority actionAuthority = action.getAnnotatedElement().getAnnotation(ActionAuthority.class);
		if (actionAuthority != null || (required != null && required.value())) {
			RequestUser requestUser = httpChannel.getBean(RequestUser.class);
			if (requestUser == null) {
				return authorizationFailure(httpChannel, action);
			}

			UserToken<Long> userToken = loginManager.getToken(requestUser.getToken());
			if (userToken == null) {
				return authorizationFailure(httpChannel, action);
			}

			if (!requestUser.accept(userToken)) {
				return authorizationFailure(httpChannel, action);
			}

			if (actionAuthority != null) {
				if (!userService.isSuperAdmin(userToken.getUid())) {
					HttpAuthority httpAuthority = httpActionAuthorityManager.getAuthority(action);
					if (httpAuthority == null) {
						httpChannel.getLogger().warn("not found autority: {}", action);
					} else {
						User user = userService.getUser(userToken.getUid());
						if (user == null) {
							return authorizationFailure(httpChannel, action);
						}
						
						if(user.isDisable()){
							return error(httpChannel, action, resultFactory.error("账号已禁用，如有问题请联系管理员!"));
						}

						PermissionGroup group = permissionGroupService.getById(user.getPermissionGroupId());
						if (group != null && group.isDisable()) {
							return error(httpChannel, action, resultFactory.error("账号分组已禁用，如有问题请联系管理员!"));
						}

						if (!permissionGroupActionService.check(user.getPermissionGroupId(), httpAuthority.getId())) {
							return error(httpChannel, action, resultFactory.error("权限不足"));
						}
					}
				}
			}
		}
		return chain.intercept(httpChannel, action, parameters);
	}

	protected Object authorizationFailure(HttpChannel httpChannel, Action action) throws Throwable {
		if (httpChannel.getRequest().getPath().startsWith(ADMIN_PATH_PREFIX)) {
			if (!httpChannel.getRequest().getHeaders().isAjax()) {
				return new Redirect(ADMIN_LOGIN_PATH);
			}
		}
		return resultFactory.authorizationFailure();
	}

	protected Object error(HttpChannel httpChannel, Action action, Object result) {
		return result;
	}

}
