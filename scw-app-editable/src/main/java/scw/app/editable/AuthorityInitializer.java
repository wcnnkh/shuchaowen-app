package scw.app.editable;

import java.util.List;

import scw.boot.ApplicationPostProcessor;
import scw.boot.ConfigurableApplication;
import scw.context.annotation.Provider;
import scw.logger.Logger;
import scw.logger.LoggerFactory;

@Provider
public class AuthorityInitializer implements ApplicationPostProcessor {
	private static Logger logger = LoggerFactory
			.getLogger(AuthorityInitializer.class);

	@Override
	public void postProcessApplication(ConfigurableApplication application)
			throws Throwable {
		if (!(canInit(application, EditorRegistry.class) || canInit(
				application, EditorResolver.class))) {
			return;
		}

		EditorRegistry editorRegistry = application.getBeanFactory()
				.getInstance(EditorRegistry.class);
		EditorResolver editorResolver = application.getBeanFactory()
				.getInstance(EditorResolver.class);
		for (Class<?> clazz : application.getContextClasses()) {
			if (!editorResolver.canResolve(clazz)) {
				continue;
			}

			List<Editor> editors = editorResolver.resolve(clazz);
			if (editors == null) {
				continue;
			}

			for (Editor editor : editors) {
				editorRegistry.register(editor);
			}
		}
	}

	private boolean canInit(ConfigurableApplication application, Class<?> clazz) {
		if (!application.getBeanFactory().isInstance(clazz)) {
			logger.error("not instance {}", clazz);
			return false;
		}

		if (!application.getBeanFactory().isSingleton(clazz)) {
			logger.error("{} not is singleton", clazz);
			return false;
		}
		return true;
	}
}
