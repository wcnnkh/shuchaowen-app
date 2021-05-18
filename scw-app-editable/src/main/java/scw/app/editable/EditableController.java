package scw.app.editable;

import scw.http.HttpMethod;
import scw.http.HttpStatus;
import scw.mvc.HttpChannel;
import scw.mvc.annotation.Controller;

@Controller
public class EditableController {
	private final EditorRegistry editorRegistry;

	public EditableController(EditorRegistry editorRegistry) {
		this.editorRegistry = editorRegistry;
	}

	@Controller(value = "/*", methods = { HttpMethod.GET, HttpMethod.POST,
			HttpMethod.DELETE, HttpMethod.PUT })
	public Object editable(HttpChannel httpChannel) {
		Editor editor = editorRegistry.getEditor(httpChannel.getRequest()
				.getPath(), httpChannel.getRequest().getMethod());
		if (editor == null) {
			httpChannel.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
			return null;
		}
		return editor.doAction(httpChannel);
	}
}
