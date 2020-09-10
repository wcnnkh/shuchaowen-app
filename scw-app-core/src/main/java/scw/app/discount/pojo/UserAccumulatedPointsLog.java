package scw.app.discount.pojo;

import java.io.Serializable;

import scw.mapper.MapperUtils;
import scw.sql.orm.annotation.Index;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;
import scw.sql.orm.support.generation.annotation.CreateTime;
import scw.sql.orm.support.generation.annotation.SequenceId;

/**
 * 用户积分日志
 * 
 * @author shuchaowen
 *
 */
@Table
public class UserAccumulatedPointsLog implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@SequenceId
	private String id;
	@Index
	private long uid;
	@Index
	private int group;
	private int accumulatedPointsChange;// 积分变更 正数为加负数为减
	@CreateTime
	private long cts;
	private boolean cancel;// 是否已取消
	private String describe;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getAccumulatedPointsChange() {
		return accumulatedPointsChange;
	}

	public void setAccumulatedPointsChange(int accumulatedPointsChange) {
		this.accumulatedPointsChange = accumulatedPointsChange;
	}

	public long getCts() {
		return cts;
	}

	public void setCts(long cts) {
		this.cts = cts;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
