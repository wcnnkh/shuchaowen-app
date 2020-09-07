package scw.app.address.controller;

import java.util.Collections;
import java.util.List;

import scw.app.address.model.AddressTree;
import scw.app.address.pojo.Address;
import scw.app.address.service.AddressService;
import scw.beans.annotation.Autowired;
import scw.http.HttpMethod;
import scw.http.HttpUtils;
import scw.http.server.ServerHttpRequest;
import scw.http.server.ServerHttpResponse;
import scw.mvc.annotation.Controller;
import scw.result.Result;
import scw.result.ResultFactory;

@Controller(value = "address", methods = { HttpMethod.GET, HttpMethod.POST })
public class AddressController {
	private AddressService addressService;
	@Autowired
	private ResultFactory resultFactory;

	public AddressController(AddressService addressService) {
		this.addressService = addressService;
	}

	@Controller(value = "sub_list")
	public Result subList(int id) {
		List<Address> list = addressService.getAddressSubList(id);
		if (list == null) {
			list = Collections.emptyList();
		}
		return resultFactory.success(list);
	}

	@Controller(value = "list")
	public Result list(ServerHttpRequest request, ServerHttpResponse response) {
		if (!HttpUtils.isExpired(request, response, addressService.lastModified())) {
			return null;
		}

		List<Address> list = addressService.getAddressList();
		if (list == null) {
			list = Collections.emptyList();
		}
		return resultFactory.success(list);
	}

	@Controller(value = "trees")
	public Result tree(ServerHttpRequest request, ServerHttpResponse response) {
		if (!HttpUtils.isExpired(request, response, addressService.lastModified())) {
			return null;
		}

		List<AddressTree> trees = addressService.getAddressTrees();
		if (trees == null) {
			trees = Collections.emptyList();
		}
		return resultFactory.success(trees);
	}

	@Controller(value = "roots")
	public Result roots() {
		List<Address> list = addressService.getRootAddressList();
		if (list == null) {
			list = Collections.emptyList();
		}
		return resultFactory.success(list);
	}
}
