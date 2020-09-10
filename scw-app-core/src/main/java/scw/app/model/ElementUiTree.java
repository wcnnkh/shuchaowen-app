package scw.app.model;

import java.io.Serializable;
import java.util.Collection;

import scw.mapper.MapperUtils;

/**
 * element ui需要的树结构
 * @author shuchaowen
 *
 */
public class ElementUiTree<V> implements Serializable {
	private static final long serialVersionUID = 1L;
	private final V value;
	private final String label;
	private final Collection<ElementUiTree<V>> children;

	public ElementUiTree(V value, String label,
			Collection<ElementUiTree<V>> children) {
		this.value = value;
		this.label = label;
		this.children = children;
	}

	public V getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public Collection<ElementUiTree<V>> getChildren() {
		return children;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
