package scw.app.address.service;

import java.util.List;

import scw.app.address.pojo.Address;

public interface AddressService {
	Address getById(int id);

	/**
	 * 获取所有父级地址
	 * 
	 * @param id
	 * @param includeSelf
	 *            是否包含自身
	 * @return
	 */
	List<Address> getParents(int id, boolean includeSelf);

	List<Address> getAddressSubList(int id);

	List<Address> getRootAddressList();
}
