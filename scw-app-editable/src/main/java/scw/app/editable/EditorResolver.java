package scw.app.editable;

import java.util.List;

public interface EditorResolver {
	boolean canResolve(Class<?> clazz);

	List<Editor> resolve(Class<?> clazz);
}
