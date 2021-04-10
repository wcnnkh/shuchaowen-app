package scw.app.user.security;

import java.util.ArrayList;
import java.util.List;

import scw.app.user.pojo.PermissionGroup;
import scw.app.user.pojo.User;
import scw.app.user.service.PermissionGroupActionService;
import scw.app.user.service.PermissionGroupService;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.context.annotation.Provider;
import scw.context.result.ResultFactory;
import scw.core.annotation.AnnotationUtils;
import scw.core.utils.StringUtils;
import scw.logger.Logger;
import scw.logger.LoggerFactory;
import scw.mvc.HttpChannel;
import scw.mvc.action.Action;
import scw.mvc.action.ActionInterceptor;
import scw.mvc.action.ActionInterceptorAccept;
import scw.mvc.action.ActionInterceptorChain;
import scw.mvc.action.ActionParameters;
import scw.mvc.annotation.ActionAuthority;
import scw.mvc.security.HttpActionAuthorityManager;
import scw.mvc.view.Redirect;
import scw.security.authority.http.HttpAuthority;
import scw.security.session.UserSession;

@Provider
public class SecurityActionInterceptor implements ActionInterceptor, ActionInterceptorAccept {
	private static Logger logger = LoggerFactory.getLogger(SecurityActionInterceptor.class);

	@Autowired
	private SecurityProperties securityConfigProperties;
	
	@Autowired(required = false)
	private ResultFactory resultFactory;
	@Autowired(required = false)
	private HttpActionAuthorityManager httpActionAuthorityManager;
	@Autowired(required = false)
	private LoginRequiredRegistry loginRequiredRegistry;

	@Autowired(required = false)
	private PermissionGroupActionService permissionGroupActionService;
	@Autowired(required = false)
	private PermissionGroupService permissionGroupService;
	@Autowired(required = false)
	private UserService userService;
	
	private boolean isSupported() {
		return resultFactory != null && httpActionAuthorityManager != null && loginRequiredRegistry != null && userService != null && permissionGroupActionService != null && permissionGroupService != null;
	}
	
	private Object getUnsupportedDesc() {
		return new Object() {
			@Override
			public String toString() {
				List<Object> list = new ArrayList<Object>(8);
				if(resultFactory == null) {
					list.add(ResultFactory.class.getName());
				}
				
				if(httpActionAuthorityManager == null) {
					list.add(HttpActionAuthorityManager.class);
				}
				
				if(loginRequiredRegistry == null) {
					list.add(LoginRequiredRegistry.class);
				}
				
				if(permissionGroupActionService == null) {
					list.add(PermissionGroupActionService.class);
				}
				
				if(permissionGroupService == null) {
					list.add(PermissionGroupService.class);
				}
				
				if(userService == null) {
					list.add(UserService.class);
				}
				return "@Autowired fail " + StringUtils.collectionToCommaDelimitedString(list);
			}
		};
	}
	
	private boolean loginRequired(LoginRequired loginRequired, ActionAuthority actionAuthority) {
		return actionAuthority != null || (loginRequired != null && loginRequired.value());
	}

	public boolean isAccept(HttpChannel httpChannel, Action action, ActionParameters parameters) {
		LoginRequired required = AnnotationUtils.getAnnotation(LoginRequired.class, action.getDeclaringClass(),
				action.getAnnotatedElement());
		ActionAuthority actionAuthority = action.getAnnotatedElement().getAnnotation(ActionAuthority.class);
		return loginRequired(required, actionAuthority)
				|| (loginRequiredRegistry == null || loginRequiredRegistry.isLoginRequried(httpChannel.getRequest()));
	}

	public Object intercept(HttpChannel httpChannel, Action action, ActionParameters parameters,
			ActionInterceptorChain chain) throws Throwable {
		LoginRequired required = AnnotationUtils.getAnnotation(LoginRequired.class, action.getDeclaringClass(),
				action.getAnnotatedElement());
		ActionAuthority actionAuthority = action.getAnnotatedElement().getAnnotation(ActionAuthority.class);
		boolean loginRequired = loginRequired(required, actionAuthority);
		if(loginRequired && !isSupported()) {
			logger.warn("Authentication is required, but authentication service is not supported! {}, {}", getUnsupportedDesc(), httpChannel);
			return resultFactory.error("Authentication is required, but authentication service is not supported");
		}
		
		if (loginRequired || loginRequiredRegistry.isLoginRequried(httpChannel.getRequest())) {
			UserSession<Long> userSession = httpChannel.getUserSession(Long.class);
			if (userSession == null) {
				return authorizationFailure(httpChannel, action);
			}
			
			if (httpChannel.getRequest().getPath().startsWith(securityConfigProperties.getController())) {
				User user = userService.getUser(userSession.getUid());
				if (user == null) {
					return authorizationFailure(httpChannel, action);
				}
			}

			if (actionAuthority != null) {
				if (!userService.isSuperAdmin(userSession.getUid())) {
					HttpAuthority httpAuthority = httpActionAuthorityManager.getAuthority(action);
					if (httpAuthority == null) {
						return error(httpChannel, action, resultFactory.error("权限不足(1)"));
					}

					User user = userService.getUser(userSession.getUid());
					if (user == null) {
						return authorizationFailure(httpChannel, action);
					}

					if (user.isDisable()) {
						return error(httpChannel, action, resultFactory.error("账号已禁用，如有问题请联系管理员!"));
					}

					PermissionGroup group = permissionGroupService.getById(user.getPermissionGroupId());
					if (group == null) {
						return error(httpChannel, action, resultFactory.error("权限不足(2)"));
					}

					if (group.isDisable()) {
						return error(httpChannel, action, resultFactory.error("账号分组已禁用，如有问题请联系管理员!"));
					}

					if (!permissionGroupActionService.check(user.getPermissionGroupId(), httpAuthority.getId())) {
						return error(httpChannel, action, resultFactory.error("权限不足"));
					}
				}
			}
		}
		return chain.intercept(httpChannel, action, parameters);
	}

	protected Object authorizationFailure(HttpChannel httpChannel, Action action) throws Throwable {
		if (httpChannel.getRequest().getPath().startsWith(securityConfigProperties.getController())) {
			if (!httpChannel.getRequest().getHeaders().isAjax()) {
				return new Redirect(securityConfigProperties.getToLoginPath());
			}
		}
		return resultFactory.authorizationFailure();
	}

	protected Object error(HttpChannel httpChannel, Action action, Object result) {
		return result;
	}

}
