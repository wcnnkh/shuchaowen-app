package scw.app.common.service;

import java.util.Set;

import scw.app.common.model.ItemDrop;
import scw.app.common.pojo.Item;
import scw.result.DataResult;

public interface ItemService {
	Item getItem(int itemId);

	DataResult<Item> createItem(String name, String describe, Set<ItemDrop> itemDrops);

	DataResult<Item> updateItem(Item item);
}
