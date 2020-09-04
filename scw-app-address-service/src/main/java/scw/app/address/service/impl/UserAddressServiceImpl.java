package scw.app.address.service.impl;

import java.util.List;

import scw.app.address.model.UserAddressModel;
import scw.app.address.pojo.Address;
import scw.app.address.pojo.UserAddress;
import scw.app.address.service.AddressService;
import scw.app.address.service.UserAddressService;
import scw.app.util.BaseServiceConfiguration;
import scw.beans.annotation.Autowired;
import scw.core.instance.annotation.Configuration;
import scw.core.utils.StringUtils;
import scw.db.DB;
import scw.mapper.Copy;
import scw.result.DataResult;
import scw.result.ResultFactory;
import scw.sql.SimpleSql;

@Configuration
public class UserAddressServiceImpl extends BaseServiceConfiguration implements UserAddressService {
	@Autowired
	private AddressService addressService;

	public UserAddressServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
		db.createTable(UserAddress.class, false);
	}

	public UserAddress getById(long id) {
		return db.getById(UserAddress.class, id);
	}

	public List<UserAddress> getUserAddressList(long uid) {
		return db.select(UserAddress.class,
				new SimpleSql("select * from user_address where uid=? order by lastUpdateTime desc", uid));
	}

	public DataResult<UserAddress> create(long uid, UserAddressModel userAddressModel) {
		if (StringUtils.isEmpty(userAddressModel.getDetailedAddress(), userAddressModel.getContactName(),
				userAddressModel.getContactPhone())) {
			return resultFactory.error("参数错误");
		}

		Address address = addressService.getById(userAddressModel.getAddressId());
		if (address == null) {
			return resultFactory.error("地址不存在(" + userAddressModel.getAddressId() + ")");
		}

		UserAddress userAddress = new UserAddress();
		Copy.copy(userAddress, userAddressModel);
		userAddress.setUid(uid);
		userAddress.setCreateTime(System.currentTimeMillis());
		userAddress.setLastUpdateTime(userAddress.getCreateTime());
		db.save(userAddress);
		return resultFactory.success(userAddress);
	}

	public DataResult<UserAddress> update(long id, UserAddressModel userAddressModel) {
		if (StringUtils.isEmpty(userAddressModel.getContactName(), userAddressModel.getContactPhone(),
				userAddressModel.getContactName())) {
			return resultFactory.error("参数错误");
		}

		Address address = addressService.getById(userAddressModel.getAddressId());
		if (address == null) {
			return resultFactory.error("地址不存在(" + userAddressModel.getAddressId() + ")");
		}

		UserAddress userAddress = getById(id);
		if (userAddress == null) {
			return resultFactory.error("用户地址不存在(" + id + ")");
		}

		Copy.copy(userAddress, userAddressModel);
		userAddress.setLastUpdateTime(System.currentTimeMillis());
		db.update(userAddress);
		return resultFactory.success(userAddress);
	}

}
