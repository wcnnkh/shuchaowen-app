package scw.app.editable.db;

import java.util.Collections;
import java.util.List;

import scw.app.editable.DataManager;
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
import scw.util.Pair;
import scw.value.AnyValue;

@Provider
public class DBDataManager implements DataManager {
	private final DB db;
	private final ResultFactory resultFactory;

	public DBDataManager(DB db, ResultFactory resultFactory) {
		this.db = db;
		this.resultFactory = resultFactory;
	}

	private <T> Sql toSql(Class<? extends T> type, T query) {
		WhereSql where = new WhereSql();
		for (Field field : MapperUtils.getMapper().getFields(type).accept(FieldFeature.EXISTING_GETTER_FIELD)) {
			AnyValue value = new AnyValue(field.getGetter().get(query));
			if(value.isEmpty()) {
				continue;
			}
			where.and(field.getGetter().getName() + "=?", value.getAsObject(Object.class));
		}
		return where.assembleSql("select * from " + StringUtils.humpNamingReplacement(type.getSimpleName(), "_"), null);
	}

	@Override
	public <T> Pagination<T> list(Class<? extends T> type, T query, int page, int limit) {
		return db.paginationQuery(type, toSql(type, query), page, limit);
	}

	@Override
	public <T> T info(Class<? extends T> type, T query) {
		return db.query(type, toSql(type, query)).first();
	}
	
	@Override
	public <T> Result update(Class<? extends T> type, T result) {
		db.update(result);
		return resultFactory.success(result);
	}

	@Override
	public <T> Result delete(Class<? extends T> type, T query) {
		db.delete(query);
		return resultFactory.success();
	}

	@Override
	public <T> DataResult<T> add(Class<? extends T> type, T result) {
		db.save(result);
		return resultFactory.success(result);
	}

	@Override
	public List<Pair<String, String>> selectOptions(Class<?> queryClass, Object queryParam) {
		return Collections.emptyList();
	}

}
