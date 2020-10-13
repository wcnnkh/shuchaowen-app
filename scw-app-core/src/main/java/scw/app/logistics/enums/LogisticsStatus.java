package scw.app.logistics.enums;

/**
 * 物流状态
 * 
 * @author shuchaowen
 *
 */
public enum LogisticsStatus {
	NOT(100, "未发货"), 
	ALREADY(200, "已发货/运输中"), 
	RECEIVED(300, "已收货");

	private final int status;
	private final String describe;

	private LogisticsStatus(int status, String describe) {
		this.status = status;
		this.describe = describe;
	}

	public int getStatus() {
		return status;
	}

	public String getDescribe() {
		return describe;
	}
	
	public static LogisticsStatus forStatus(int status){
		for(LogisticsStatus logisticsStatus : values()){
			if(logisticsStatus.status == status){
				return logisticsStatus;
			}
		}
		
		return null;
	}
}
