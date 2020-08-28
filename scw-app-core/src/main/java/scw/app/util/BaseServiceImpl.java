package scw.app.util;

import scw.db.DB;
import scw.result.ResultFactory;

public class BaseServiceImpl {
	protected final DB db;
	protected final ResultFactory resultFactory;
	
	public BaseServiceImpl(DB db, ResultFactory resultFactory){
		this.db = db;
		this.resultFactory = resultFactory;
	}
}
