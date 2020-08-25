package scw.app.user.security;

import scw.app.user.pojo.User;
import scw.app.user.service.PermissionGroupActionService;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.core.instance.annotation.Configuration;
import scw.mvc.HttpChannel;
import scw.mvc.action.Action;
import scw.mvc.action.ActionFilter;
import scw.mvc.action.ActionFilterChain;
import scw.mvc.annotation.ActionAuthority;
import scw.mvc.security.HttpActionAuthorityManager;
import scw.result.ResultFactory;
import scw.security.authority.http.HttpAuthority;
import scw.security.login.UserToken;

@Configuration
public class SecurityActionFilter implements ActionFilter {
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
	
	public Object doFilter(HttpChannel httpChannel, Action action, Object[] args, ActionFilterChain filterChain)
			throws Throwable {
		LoginRequired required = action.getAnnotatedElement().getAnnotation(LoginRequired.class);
		ActionAuthority actionAuthority = action.getMethodAnnotatedElement().getAnnotation(ActionAuthority.class);
		if (actionAuthority != null || (required != null && required.value())) {
			RequestUserToken requestUserToken = httpChannel.getBean(RequestUserToken.class);
			if(requestUserToken == null){
				return resultFactory.authorizationFailure();
			}

			UserToken<Long> userToken = loginManager.getToken(requestUserToken.getToken());
			if (userToken == null) {
				return resultFactory.authorizationFailure();
			}
			
			if(!requestUserToken.accept(userToken)){
				return resultFactory.authorizationFailure();
			}

			if (actionAuthority != null) {
				if (!userService.isSuperAdmin(userToken.getUid())) {
					HttpAuthority httpAuthority = httpActionAuthorityManager.getAuthority(action);
					if (httpAuthority == null) {
						httpChannel.getLogger().warn("not found autority: {}", action);
					} else {
						User user = userService.getUser(userToken.getUid());
						if (user == null) {
							return resultFactory.authorizationFailure();
						}
						
						if (!permissionGroupActionService.check(user.getPermissionGroupId(), httpAuthority.getId())) {
							return resultFactory.error("权限不足");
						}
					}
				}
			}
		}
		return filterChain.doFilter(httpChannel, action, args);
	}
}
