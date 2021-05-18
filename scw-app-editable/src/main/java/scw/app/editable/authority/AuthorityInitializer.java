package scw.app.editable.authority;

import scw.app.editable.annotation.Editable;
import scw.boot.ApplicationPostProcessor;
import scw.boot.ConfigurableApplication;
import scw.http.HttpMethod;
import scw.logger.Logger;
import scw.logger.LoggerFactory;
import scw.mvc.security.HttpActionAuthorityManager;
import scw.security.authority.http.DefaultHttpAuthority;
import scw.security.authority.http.HttpAuthority;
import scw.security.authority.http.HttpAuthorityManager;

public class AuthorityInitializer implements ApplicationPostProcessor {
	private static Logger logger = LoggerFactory.getLogger(AuthorityInitializer.class);
	
	@Override
	public void postProcessApplication(ConfigurableApplication application) throws Throwable {
		if(!application.getBeanFactory().isInstance(HttpActionAuthorityManager.class.getName())) {
			
		}
		
		HttpActionAuthorityManager authorityManager = application.getBeanFactory().getInstance(HttpActionAuthorityManager.class);
		for(Class<?> clazz : application.getContextClassesLoader()) {
			Editable editable = clazz.getAnnotation(Editable.class);
			if(editable == null) {
				continue;
			}

			//列表
			String listPath = clazz.getName() + "/list";
			HttpAuthority list = new DefaultHttpAuthority(listPath + "/get", null, editable.name(), null, true, listPath, HttpMethod.GET);
			
			//添加
			String addPath = clazz.getName() + "/add";
		}
	}

}
