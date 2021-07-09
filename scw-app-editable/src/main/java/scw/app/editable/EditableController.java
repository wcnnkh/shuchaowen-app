package scw.app.editable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import scw.app.user.security.SecurityProperties;
import scw.beans.annotation.Autowired;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.data.ResourceStorageService;
import scw.http.HttpMethod;
import scw.http.HttpRequestEntity;
import scw.http.HttpStatus;
import scw.mvc.HttpChannel;
import scw.mvc.annotation.Controller;
import scw.security.session.UserSession;
import scw.util.XUtils;

@Controller(SecurityProperties.ADMIN_CONTROLLER)
public class EditableController {
	private final EditorRegistry editorRegistry;
	@Autowired(required = false)
	private ResourceStorageService resourceStorageService;
	@Autowired
	private ResultFactory resultFactory;

	public EditableController(EditorRegistry editorRegistry) {
		this.editorRegistry = editorRegistry;
	}

	@Controller(value = "/**", methods = { HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT })
	public Object editable(HttpChannel httpChannel) {
		Editor editor = editorRegistry.getEditor(httpChannel.getRequest().getPath(),
				httpChannel.getRequest().getRawMethod());
		if (editor == null) {
			httpChannel.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
			return null;
		}
		return editor.doAction(httpChannel);
	}

	@Controller(value = "/generate_upload_policy")
	public Result generateUploadPolicy(UserSession<Long> requestUser) {
		if (resourceStorageService == null) {
			return resultFactory.error("不支持资源上传");
		}
		HttpRequestEntity<?> requestEntity = resourceStorageService.generatePolicy(
				requestUser.getUid() + "/" + XUtils.getUUID(), new Date(System.currentTimeMillis() + 10000L));
		Map<String, Object> map = new HashMap<>(4);
		map.put("url", requestEntity.getURI());
		map.put("method", requestEntity.getMethod());
		return resultFactory.success(map);
	}
}
