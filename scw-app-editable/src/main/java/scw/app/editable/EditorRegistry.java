package scw.app.editable;

import java.util.EnumMap;
import java.util.concurrent.ConcurrentHashMap;

import scw.http.HttpMethod;
import scw.lang.AlreadyExistsException;
import scw.mvc.security.HttpActionAuthorityManager;

public class EditorRegistry {
	private final HttpActionAuthorityManager httpActionAuthorityManager;
	private final ConcurrentHashMap<String, EnumMap<HttpMethod, Editor>> map = new ConcurrentHashMap<String, EnumMap<HttpMethod, Editor>>();

	public EditorRegistry(HttpActionAuthorityManager httpActionAuthorityManager) {
		this.httpActionAuthorityManager = httpActionAuthorityManager;
	}

	public void register(Editor editor) {
		EnumMap<HttpMethod, Editor> methodMap = map.get(editor.getPath());
		if (methodMap == null) {
			methodMap = new EnumMap<HttpMethod, Editor>(HttpMethod.class);
			EnumMap<HttpMethod, Editor> oldMethodMap = map.putIfAbsent(editor.getPath(), methodMap);
			if (oldMethodMap != null) {
				methodMap = oldMethodMap;
			}
		}

		if (methodMap.containsKey(editor.getMethod())) {
			throw new AlreadyExistsException(editor.toString());
		}

		methodMap.put(editor.getMethod(), editor);
		httpActionAuthorityManager.register(editor);
	}

	public Editor getEditor(String path, HttpMethod httpMethod) {
		EnumMap<HttpMethod, Editor> methodMap = map.get(path);
		if (methodMap == null) {
			return null;
		}

		return methodMap.get(httpMethod);
	}
}
