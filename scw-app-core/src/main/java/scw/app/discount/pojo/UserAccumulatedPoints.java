package scw.app.discount.pojo;

import java.io.Serializable;

import scw.mapper.MapperUtils;
import scw.sql.orm.annotation.Counter;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

/**
 * 用户积分
 * @author shuchaowen
 *
 */
@Table
public class UserAccumulatedPoints implements Serializable{
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private long uid;
	@Counter
	private int accumulatedPoints;//用户积分
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public int getAccumulatedPoints() {
		return accumulatedPoints;
	}
	public void setAccumulatedPoints(int accumulatedPoints) {
		this.accumulatedPoints = accumulatedPoints;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
