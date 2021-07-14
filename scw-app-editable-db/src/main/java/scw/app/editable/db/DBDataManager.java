package scw.app.editable.db;

import java.util.List;
import java.util.stream.Collectors;

import scw.app.editable.DataManager;
import scw.app.editable.EditableExcpetion;
import scw.app.editable.annotation.SelectOption;
import scw.context.annotation.Provider;
import scw.context.result.DataResult;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.core.utils.StringUtils;
import scw.db.DB;
import scw.mapper.Field;
import scw.mapper.FieldFeature;
import scw.mapper.Fields;
import scw.mapper.MapperUtils;
import scw.sql.SimpleSql;
import scw.sql.Sql;
import scw.sql.SqlUtils;
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
			if (value.isEmpty()) {
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
	public List<Pair<String, String>> queryOptions(Class<?> queryClass, String query) {
		Fields primaryKeys = db.getSqlDialect().getPrimaryKeys(queryClass).shared();
		if (primaryKeys.size() != 1) {
			throw new EditableExcpetion("查询选项的主键数量只能存在一个");
		}
		Field queryField = db.getSqlDialect().getFields(queryClass).stream()
				.filter((f) -> f.isAnnotationPresent(SelectOption.class)).findFirst().get();
		String sql = "select * from " + db.getSqlDialect().getName(queryClass);
		Sql querySql = StringUtils.isEmpty(query) ? new SimpleSql(sql)
				: new SimpleSql(sql + " where `" + db.getSqlDialect().getName(queryField.getSetter()) + "` like ?",
						SqlUtils.toLikeValue(query));
		return db.query(queryClass, querySql).map((obj) -> {
			String id = String.valueOf(primaryKeys.first().getGetter().get(obj));
			String text = String.valueOf(queryField.getGetter().get(obj));
			return new Pair<String, String>(id, text);
		}).collect(Collectors.toList());
	}
}
