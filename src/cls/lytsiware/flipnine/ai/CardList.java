package cls.lytsiware.flipnine.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CardList implements Iterable<Card> {

	protected List<Card> cards = new ArrayList<>();
	protected Integer sum;

	public CardList(List<? extends Card> cards, Integer sum) {
		for (Card card : cards) {
			this.cards.add(card);
		}
		this.sum = sum;
	}
	
	public static CardList fromIntegerList(List<Integer> cards, Integer sum){
		List<Card> _cards = new ArrayList<>();
		for (Integer intg :cards){
			_cards.add(new Card(intg, true));
		}
		return new CardList(_cards, sum);
	}

	public CardList(int... integers) {
		for (Integer intg : integers) {
			cards.add(new Card(intg, true));
		}
	}


	public CardList swap(int index1, int index2) {
		ArrayList<Card> swappedList = new ArrayList<>(cards);
		Collections.swap(swappedList, index1, index2);
		return new CardList(swappedList, computeSum(index1, index2));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		for (Card card : cards) {
			result = prime * result + ((card == null) ? 0 : card.hashCode());
		}
		result = result + (sum == null ? 0 : sum.hashCode());
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
		CardList cl = (CardList) obj;
		if (cl.size() != this.size()) {
			return false;
		}
		if (sum == null && cl.getSum() != null) {
			return false;
		} else if (!sum.equals(cl.getSum())) {
			return false;
		}
		for (int i = 0; i < size(); i++) {
			Card oCard = cl.get(i);
			Card cardSimple = get(i);
			if (oCard == null) {
				if (cardSimple != null) {
					return false;
				} else {
					continue;
				}
			} else if (!oCard.equals(cardSimple)) {
				return false;
			}
		}
		return true;
	}

	public Integer getSum() {
		return sum;
	}

	protected int computeSum(int index1, int index2) {
		int result = cards.get(index1).getValue() + cards.get(index2).getValue();
		if (result > 9) {
			result = result - 9;
		}
		return result;
	}

	@Override
	public String toString() {
		return cards.toString() + " - sum =  " + sum;
	}

	public Card get(int index) {
		return cards.get(index);
	}

	@Override
	public Iterator<Card> iterator() {
		return cards.iterator();
	}
	public int indexBySum(int number) {
		Card card = new Card(number, true);
		return cards.indexOf(card);
	}

	public int size() {
		return cards.size();
	}

}
