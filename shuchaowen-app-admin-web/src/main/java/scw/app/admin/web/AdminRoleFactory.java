package scw.app.admin.web;

import scw.app.admin.pojo.AdminRole;
import scw.mvc.HttpChannel;
import scw.mvc.action.Action;

public interface AdminRoleFactory {
	AdminRole getAdminRole(HttpChannel httpChannel, Action action);
}
