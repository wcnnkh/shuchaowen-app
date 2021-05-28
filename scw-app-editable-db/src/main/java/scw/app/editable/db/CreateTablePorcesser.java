package scw.app.editable.db;

import scw.app.editable.EditorResolver;
import scw.boot.ApplicationPostProcessor;
import scw.boot.ConfigurableApplication;
import scw.context.annotation.Provider;
import scw.db.DB;
import scw.sql.orm.annotation.Table;

@Provider
public class CreateTablePorcesser implements ApplicationPostProcessor{

	@Override
	public void postProcessApplication(ConfigurableApplication application)
			throws Throwable {
		if(application.isInstance(DB.class) && application.isInstance(EditorResolver.class)){
			DB db = application.getInstance(DB.class);
			for(Class<?> clazz : application.getContextClassesLoader()){
				if(clazz.isAnnotationPresent(Table.class)){
					db.createTable(clazz);
				}
			}
		}
	}

}
