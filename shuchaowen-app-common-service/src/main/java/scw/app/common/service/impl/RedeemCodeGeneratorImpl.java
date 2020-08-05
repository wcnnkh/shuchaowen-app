package scw.app.common.service.impl;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base32;

import scw.app.common.BaseServiceImpl;
import scw.app.common.service.RedeemCodeGenerator;
import scw.core.instance.annotation.Configuration;
import scw.db.DB;
import scw.result.ResultFactory;
import scw.util.RandomUtils;

@Configuration(order=Integer.MIN_VALUE)
public class RedeemCodeGeneratorImpl extends BaseServiceImpl implements RedeemCodeGenerator {
	private static final Base32 BASE32 = new Base32();

	public RedeemCodeGeneratorImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
		db.createTable(RedeemCodeMaxIdTable.class, false);
	}

	public synchronized String generator(int type) {
		RedeemCodeMaxIdTable idTable = db.getById(RedeemCodeMaxIdTable.class, type);
		if (idTable == null) {
			idTable = new RedeemCodeMaxIdTable();
			idTable.setType(type);
			idTable.setMaxId(1);
			db.save(idTable);
		} else {
			idTable.setMaxId(idTable.getMaxId() + 1);
			db.update(idTable);
		}

		return base32(idTable.getMaxId());
	}

	protected String base32(int id) {
		// 乘1000是因为base32(1000)的结果是6位(不包含'=')，加上随机数是为了看起来不那么巧合
		int number = id * 1000 + RandomUtils.getRandValue(0, 999);
		String text = BASE32.encodeToString((number + "").getBytes(StandardCharsets.UTF_8));
		while (text.endsWith("=")) {
			text = text.substring(0, text.length() - 1);
		}
		return text;
	}
}
