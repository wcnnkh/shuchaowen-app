package scw.app.address.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import scw.app.address.model.UserAddressInfo;
import scw.app.address.model.UserAddressModel;
import scw.app.address.pojo.Address;
import scw.app.address.pojo.UserAddress;
import scw.app.address.service.AddressService;
import scw.app.address.service.UserAddressService;
import scw.app.util.BaseServiceConfiguration;
import scw.beans.annotation.Autowired;
import scw.beans.annotation.Service;
import scw.context.result.DataResult;
import scw.context.result.ResultFactory;
import scw.core.utils.CollectionUtils;
import scw.core.utils.StringUtils;
import scw.db.DB;
import scw.mapper.Copy;
import scw.sql.SimpleSql;

@Service
public class UserAddressServiceImpl extends BaseServiceConfiguration implements UserAddressService {
	@Autowired
	private AddressService addressService;

	public UserAddressServiceImpl(DB db, ResultFactory resultFactory) {
		super(db, resultFactory);
		db.createTable(UserAddress.class, false);
	}

	private UserAddressInfo wrapper(UserAddress userAddress) {
		UserAddressInfo info = new UserAddressInfo();
		Copy.copy(info, userAddress);
		info.setAddresses(addressService.getParents(userAddress.getAddressId(), true));
		return info;
	}

	public UserAddressInfo getUserAddressInfo(long id) {
		UserAddress userAddress = db.getById(UserAddress.class, id);
		if (userAddress == null) {
			return null;
		}

		return wrapper(userAddress);
	}

	public List<UserAddressInfo> getUserAddressList(long uid) {
		List<UserAddress> userAddressList = db.select(UserAddress.class,
				new SimpleSql("select * from user_address where uid=? order by lastUpdateTime desc", uid));
		if (CollectionUtils.isEmpty(userAddressList)) {
			return Collections.emptyList();
		}

		List<UserAddressInfo> list = new ArrayList<UserAddressInfo>();
		for (UserAddressInfo info : list) {
			list.add(wrapper(info));
		}
		return list;
	}

	public DataResult<UserAddressInfo> create(long uid, UserAddressModel userAddressModel) {
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
		return resultFactory.success(wrapper(userAddress));
	}

	public DataResult<UserAddressInfo> update(long id, UserAddressModel userAddressModel) {
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
		return resultFactory.success(wrapper(userAddress));
	}

	public UserAddress getById(long id) {
		return db.getById(UserAddress.class, id);
	}

}
