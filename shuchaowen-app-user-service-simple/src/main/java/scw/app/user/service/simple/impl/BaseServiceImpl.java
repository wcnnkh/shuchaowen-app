package scw.app.user.service.simple.impl;

import scw.db.DB;

public class BaseServiceImpl {
	protected DB db;
	
	public BaseServiceImpl(DB db){
		this.db = db;
	}
}
