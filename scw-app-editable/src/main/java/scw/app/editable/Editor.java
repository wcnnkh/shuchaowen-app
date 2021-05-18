package scw.app.editable;

import java.util.List;

import scw.util.Pagination;

public interface Editor {
	<T> T getById(Class<? extends T> type, Object... ids);

	boolean save(Object editable);

	boolean update(Object editable);

	boolean delete(Object editable);

	<T> List<T> select(T query);

	<T> Pagination<T> getPagination(T query, int page, int limit);
}
