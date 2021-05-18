package scw.app.editable.db;

import java.util.List;

import scw.app.editable.Editor;
import scw.db.DB;
import scw.util.Pagination;

public class DBEditor implements Editor{
	private final DB db;
	
	public DBEditor(DB db) {
		this.db = db;
	}

	@Override
	public <T> T getById(Class<? extends T> type, Object... ids) {
		return db.getById(type, ids);
	}

	@Override
	public boolean save(Object editable) {
		return db.save(editable);
	}

	@Override
	public boolean update(Object editable) {
		return db.update(editable);
	}

	@Override
	public boolean delete(Object editable) {
		return db.delete(editable);
	}

	@Override
	public <T> List<T> select(T query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Pagination<T> getPagination(T query, int page, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

}
