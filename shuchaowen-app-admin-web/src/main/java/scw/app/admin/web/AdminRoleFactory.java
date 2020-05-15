package scw.app.admin.web;

import scw.app.admin.pojo.AdminRole;
import scw.net.http.server.mvc.HttpChannel;
import scw.net.http.server.mvc.action.Action;

public interface AdminRoleFactory {
	AdminRole getAdminRole(HttpChannel httpChannel, Action action);
}
