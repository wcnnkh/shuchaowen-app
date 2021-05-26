package scw.app.editable.db;

import java.util.List;

import scw.app.editable.DataManager;
import scw.app.editable.SelectOptions;
import scw.context.annotation.Provider;
import scw.context.result.DataResult;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.core.utils.StringUtils;
import scw.db.DB;
import scw.mapper.Field;
import scw.mapper.FieldFeature;
import scw.mapper.MapperUtils;
import scw.sql.Sql;
import scw.sql.WhereSql;
import scw.util.Pagination;

@Provider
public class DBDataManager implements DataManager {
	private final DB db;
	private final ResultFactory resultFactory;
	
	public DBDataManager(DB db, ResultFactory resultFactory) {
		this.db = db;
		this.resultFactory = resultFactory;
	}

	@Override
	public <T> Pagination<T> list(Class<? extends T> type, T query, int page, int limit) {
		WhereSql where = new WhereSql();
		for(Field field : MapperUtils.getMapper().getFields(type).accept(FieldFeature.EXISTING_GETTER_FIELD)){
			Object value = field.getGetter().get(query);
			if(value == null){
				continue;
			}
			
			where.and(field.getGetter().getName() + "=?", value);
		}
		Sql sql = where.assembleSql("select * from " + StringUtils.humpNamingReplacement(type.getSimpleName(), "_"), null);
		return db.select(type, page, limit, sql);
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
		db.save(result);
		return resultFactory.success(result);
	}

	@Override
	public List<SelectOptions> selectOptions(Class<?> queryClass, Object queryParam) {
		// TODO Auto-generated method stub
		return null;
	}

}
