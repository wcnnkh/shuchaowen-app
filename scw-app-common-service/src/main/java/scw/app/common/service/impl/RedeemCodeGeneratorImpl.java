package scw.app.common.service.impl;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base32;

import scw.app.common.service.RedeemCodeGenerator;
import scw.app.util.BaseServiceConfiguration;
import scw.beans.annotation.Service;
import scw.context.result.ResultFactory;
import scw.db.DB;

@Service
public class RedeemCodeGeneratorImpl extends BaseServiceConfiguration implements RedeemCodeGenerator {
	static final Base32 BASE32 = new Base32();
	private int baseValue = 10;

	public RedeemCodeGeneratorImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
		db.createTable(RedeemCodeMaxIdTable.class, false);
	}

	public int getBaseValue() {
		return baseValue;
	}

	public void setBaseValue(int baseValue) {
		this.baseValue = baseValue;
	}

	public synchronized String generator(int type) {
		RedeemCodeMaxIdTable idTable = db.getById(RedeemCodeMaxIdTable.class, type);
		if (idTable == null) {
			idTable = new RedeemCodeMaxIdTable();
			idTable.setType(type);
			idTable.setMaxId(getBaseValue());
			db.save(idTable);
		} else {
			idTable.setMaxId(idTable.getMaxId() + 1);
			db.update(idTable);
		}

		return base32(idTable.getMaxId());
	}

	protected String base32(long id) {
		String text = BASE32.encodeToString((id + "").getBytes(StandardCharsets.UTF_8));
		while (text.endsWith("=")) {
			text = text.substring(0, text.length() - 1);
		}
		return text;
	}
}
