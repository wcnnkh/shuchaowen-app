package scw.app.common.model;

import java.io.Serializable;
import java.util.List;

import scw.mapper.MapperUtils;

public class TaskModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean hidden;
	private long beginTime;
	private long endTime;
	public List<TastInfo> tastInfos;

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public List<TastInfo> getTastInfos() {
		return tastInfos;
	}

	public void setTastInfos(List<TastInfo> tastInfos) {
		this.tastInfos = tastInfos;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
