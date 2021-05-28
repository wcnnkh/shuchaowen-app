package scw.app.editable;

import java.util.List;

import scw.context.result.DataResult;
import scw.context.result.Result;
import scw.lang.Nullable;
import scw.util.Pagination;

public interface DataManager {
	<T> Pagination<T> list(Class<? extends T> type, T query, int page, int limit);

	@Nullable
	<T> T info(Class<? extends T> type, T query);

	<T> Result update(Class<? extends T> type, T result);

	<T> Result delete(Class<? extends T> type, T query);

	<T> DataResult<T> add(Class<? extends T> type, T result);

	List<SelectOptions> selectOptions(Class<?> queryClass, Object queryParam);
}
