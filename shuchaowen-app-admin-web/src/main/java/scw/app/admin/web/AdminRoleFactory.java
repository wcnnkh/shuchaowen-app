package scw.app.admin.web;

import scw.app.admin.pojo.AdminRole;
import scw.mvc.Channel;
import scw.mvc.action.Action;

public interface AdminRoleFactory {
	AdminRole getAdminRole(Channel channel, Action action);
}
