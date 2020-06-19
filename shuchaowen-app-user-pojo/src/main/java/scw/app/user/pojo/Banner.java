package scw.app.user.pojo;

import scw.app.user.pojo.enums.BannerModel;
import scw.sql.orm.annotation.AutoIncrement;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

@Table
public class Banner extends BannerModel {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@AutoIncrement
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
