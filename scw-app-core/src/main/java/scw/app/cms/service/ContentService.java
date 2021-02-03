package scw.app.cms.service;

import scw.app.cms.model.BasicContent;
import scw.app.cms.pojo.Content;
import scw.context.result.DataResult;
import scw.context.result.Result;

/**
 * 内容服务
 * 
 * @author shuchaowen
 *
 */
public interface ContentService {
	Content getContent(long id);

	DataResult<Content> createContent(long uid, BasicContent basicContent);

	Result updateContent(long id, long uid, BasicContent basicContent);

	/**
	 * 阅读
	 * @param uid
	 * @param contentId
	 * @return
	 */
	Result read(long uid, long contentId);

	/**
	 * 赞
	 * @param uid
	 * @param contentId
	 * @return
	 */
	Result fabulous(long uid, long contentId);

	/**
	 * 踩
	 * @param uid
	 * @param contentId
	 * @return
	 */
	Result step(long uid, long contentId);
	
	/**
	 * 收藏
	 * @param uid
	 * @param contentId
	 * @return
	 */
	Result collection(long uid, long contentId);
}
