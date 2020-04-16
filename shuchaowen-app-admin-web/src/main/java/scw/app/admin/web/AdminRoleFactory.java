package scw.app.admin.web;

import scw.app.admin.pojo.AdminRole;
import scw.mvc.action.manager.HttpAction;
import scw.mvc.http.HttpChannel;

public interface AdminRoleFactory {
	AdminRole getAdminRole(HttpChannel httpChannel, HttpAction httpAction);
}
