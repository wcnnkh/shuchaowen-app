package scw.app.common.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * element ui需要的树结构
 * @author shuchaowen
 *
 */
public class ElementUiTree<V> implements Serializable {
	private static final long serialVersionUID = 1L;
	private final V value;
	private final String lable;
	private final Collection<ElementUiTree<V>> children;

	public ElementUiTree(V value, String lable,
			Collection<ElementUiTree<V>> children) {
		this.value = value;
		this.lable = lable;
		this.children = children;
	}

	public V getValue() {
		return value;
	}

	public String getLable() {
		return lable;
	}

	public Collection<ElementUiTree<V>> getChildren() {
		return children;
	}
}
