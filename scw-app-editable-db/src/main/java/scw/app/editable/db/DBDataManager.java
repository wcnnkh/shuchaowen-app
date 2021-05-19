package scw.app.editable.db;

import java.util.List;

import scw.app.editable.DataManager;
import scw.app.editable.SelectOptions;
import scw.context.annotation.Provider;
import scw.context.result.DataResult;
import scw.context.result.Result;
import scw.util.Pagination;

@Provider
public class DBDataManager implements DataManager {

	@Override
	public <T> Pagination<T> list(Class<? extends T> type, T query, int page, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T info(Class<? extends T> type, T query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Result update(Class<? extends T> type, T result) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Result delete(Class<? extends T> type, T query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> DataResult<T> add(Class<? extends T> type, T result) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SelectOptions> selectOptions(Class<?> queryClass, Object queryParam) {
		// TODO Auto-generated method stub
		return null;
	}

}
