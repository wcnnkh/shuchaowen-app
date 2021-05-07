package scw.app.web;

import java.util.HashMap;
import java.util.Map;

import scw.http.HttpMethod;
import scw.http.server.MultiPartServerHttpRequest;
import scw.mvc.annotation.Controller;
import scw.net.message.multipart.FileItem;
import scw.security.session.UserSession;
import scw.upload.kind.KindDirType;
import scw.upload.kind.KindEditor;
import scw.upload.kind.KindOrderType;

@Controller(value = "kind")
public class KindController {
	private KindEditor kindEditor;

	public KindController(KindEditor kindEditor) {
		this.kindEditor = kindEditor;
	}

	public String getRequestGroup(UserSession<Long> requestUser) {
		return requestUser != null? (requestUser.getUid() + "") : null;
	}

	@Controller(value = "upload", methods = { HttpMethod.POST, HttpMethod.PUT })
	public Object upload(UserSession<Long> requestUser, MultiPartServerHttpRequest request, KindDirType dir) {
		FileItem fileItem = request.getFirstFile();
		if (fileItem == null) {
			return error("请选择文件");
		}

		try {
			String url = kindEditor.upload(getRequestGroup(requestUser), dir, fileItem);
			return success(url);
		} catch (Exception e) {
			return error(e.getMessage());
		} finally {
			request.close();
		}
	}

	@Controller(value = "manager", methods = { HttpMethod.GET })
	public Object manager(UserSession<Long> requestUser, KindDirType dir, String path, KindOrderType order) {
		return kindEditor.manager(getRequestGroup(requestUser), dir, path, order);
	}

	private Object success(String url) {
		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("code", 0);
		map.put("url", url);
		return map;
	}

	private Object error(String msg) {
		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("code", 1);
		map.put("msg", msg);
		return map;
	}
}
