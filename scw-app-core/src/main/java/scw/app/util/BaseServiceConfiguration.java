package scw.app.util;

import scw.context.result.ResultFactory;
import scw.db.DB;

public class BaseServiceConfiguration {
	protected final DB db;
	protected final ResultFactory resultFactory;
	
	public BaseServiceConfiguration(DB db, ResultFactory resultFactory){
		this.db = db;
		this.resultFactory = resultFactory;
	}

	public DB getDb() {
		return db;
	}

	public ResultFactory getResultFactory() {
		return resultFactory;
	}
}
