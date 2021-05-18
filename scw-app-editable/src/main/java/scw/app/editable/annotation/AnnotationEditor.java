package scw.app.editable.annotation;

import java.util.Map;

import scw.app.editable.Editor;
import scw.http.HttpMethod;
import scw.mvc.HttpChannel;

public class AnnotationEditor implements Editor{
	private final Class<?> clazz;
	
	public AnnotationEditor(Class<?> clazz, HttpMethod method) {
		this.clazz = clazz;
	}
	
	@Override
	public String getPath() {
		return clazz.getName();
	}

	@Override
	public HttpMethod getHttpMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isMenu() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, String> getAttributeMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object doAction(HttpChannel httpChannel) {
		// TODO Auto-generated method stub
		return null;
	}

}
