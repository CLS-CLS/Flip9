package cls.lytsiware.common;

import java.util.List;

public interface Node<T> {

	public List<Node<T>> getSiblings();

	public T getElement(); 

}
