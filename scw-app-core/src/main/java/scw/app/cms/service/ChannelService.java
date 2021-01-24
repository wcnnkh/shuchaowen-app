package scw.app.cms.service;

import java.util.List;

import scw.app.cms.model.BasicChannel;
import scw.app.cms.model.ChannelTree;
import scw.app.cms.pojo.Channel;
import scw.context.result.DataResult;
import scw.context.result.Result;

public interface ChannelService {
	Channel getChannel(int id);

	List<Channel> getRootList(Long uid);

	List<Channel> getSubList(int id, Long uid);

	List<Channel> getAllList(int id, Long uid);

	ChannelTree getChannelTree(Long uid);

	DataResult<Channel> createChannel(long uid, BasicChannel basicChannel);

	Result updateChannel(int id, long uid, BasicChannel basicChannel);
}
