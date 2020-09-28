package scw.app.web;

import java.util.Map;

import scw.app.user.pojo.User;
import scw.http.server.ServerHttpRequest;
import scw.http.server.ServerHttpResponse;

public interface UserControllerService {
	Map<String, Object> login(User user, ServerHttpRequest request, ServerHttpResponse response);
}
