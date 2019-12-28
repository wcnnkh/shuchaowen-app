package scw.app.simple.service.impl;

import scw.db.DB;

public class BaseServiceImpl {
	protected DB db;
	
	public BaseServiceImpl(DB db){
		this.db = db;
	}
}
