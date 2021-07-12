package scw.app.editable;

import java.util.Date;

import scw.app.user.security.SecurityProperties;
import scw.beans.annotation.Autowired;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.data.ResourceStorageService;
import scw.data.ResourceStorageService.UploadPolicy;
import scw.http.HttpMethod;
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
	public Result generateUploadPolicy(UserSession<Long> requestUser, String group, String suffix) {
		if (resourceStorageService == null) {
			return resultFactory.error("不支持资源上传");
		}

		UploadPolicy uploadPolicy = resourceStorageService.generatePolicy(
				group + "/" + requestUser.getUid() + "/" + XUtils.getUUID() + "." + suffix,
				new Date(System.currentTimeMillis() + 10000L));
		return resultFactory.success(uploadPolicy);
	}
}
