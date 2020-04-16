package scw.app.admin.service.impl;

import scw.db.DB;

public class BaseImpl {
	protected final DB db;
	
	public BaseImpl(DB db){
		this.db = db;
	}
}
