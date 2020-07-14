package scw.app.cms.model;

import java.util.List;

import scw.app.cms.pojo.Channel;

public class ChannelTree extends Channel {
	private static final long serialVersionUID = 1L;
	private List<ChannelTree> subList;

	public List<ChannelTree> getSubList() {
		return subList;
	}

	public void setSubList(List<ChannelTree> subList) {
		this.subList = subList;
	}
}
