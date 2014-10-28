package cls.lytsiware.flipnine.ai.onecolor;

import java.util.ArrayList;
import java.util.List;

import cls.lytsiware.common.Node;
import cls.lytsiware.flipnine.ai.CardList;
import cls.lytsiware.flipnine.ai.Constants;

public class CardListSimpleNode implements Node<CardList> {

	CardList element;

	public CardListSimpleNode(CardList value) {
		this.element = value;
	}

	@Override
	public CardList getElement() {
		return element;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((element == null) ? 0 : element.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CardListSimpleNode other = (CardListSimpleNode) obj;
		if (element == null) {
			if (other.element != null)
				return false;
		} else if (!element.equals(other.element))
			return false;
		return true;
	}

	
	@Override
	public List<Node<CardList>> getSiblings() {
		List<Node<CardList>> siblings = new ArrayList<>();
		if (element.getSum() == null) {
			for (int index = 0; index < Constants.MAX_CARDS; index++) {
				for (int intrn = index; intrn < Constants.MAX_CARDS; intrn++) {
					if (index == intrn) {
						continue;
					}
					siblings.add(new CardListSimpleNode(element.swap(index, intrn)));
				}
			}
		} else {
			int index = element.indexBySum(element.getSum());
			for (int i = 0; i < Constants.MAX_CARDS; i++) {
				if (index == i) {
					continue;
				}
				siblings.add(new CardListSimpleNode(element.swap(index, i)));
			}
		}
		return siblings;
	}

	@Override
	public String toString() {
		return "Node [element=" + element + "]";
	}
}
