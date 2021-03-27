package scw.app.logistics.model;

import java.io.Serializable;

import scw.core.utils.XTime;
import scw.mapper.MapperUtils;

/**
 * 物流信息
 * 
 * @author shuchaowen
 *
 */
public class LogisticsInformation implements Serializable {
	private static final long serialVersionUID = 1L;
	private long time;
	private String describe;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getTimeDescribe() {
		return XTime.format(time, "yyyy-MM-dd HH:mm");
	}

	@Override
	public String toString() {
		return MapperUtils.getMapper().getFields(LogisticsInformation.class).getValueMap(this).toString();
	}
}
