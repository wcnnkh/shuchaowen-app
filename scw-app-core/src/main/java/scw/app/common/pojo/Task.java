package scw.app.common.pojo;

import scw.app.common.model.TaskModel;
import scw.mapper.MapperUtils;
import scw.sql.orm.annotation.AutoIncrement;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

/**
 * 任务
 * 
 * @author shuchaowen
 *
 */
@Table
public class Task extends TaskModel {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@AutoIncrement
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	};
}
