package cls.lytsiware.flipnine.ai;

import java.util.ArrayList;
import java.util.List;

import cls.lytsiware.common.Node;
import cls.lytsiware.common.search.breadth.BreadthFirstSearch;
import cls.lytsiware.common.search.breadth.Condition;
import cls.lytsiware.flipnine.ai.onecolor.CardListSimpleNode;
import cls.lytsiware.flipnine.ai.onecolor.WinConditionSimple;
import cls.lytsiware.flipnine.ui.ApplicationMouseHandler;
import cls.lytsiware.flipnine.ui.CardUi;
import cls.lytsiware.flipnine.ui.external.ExternalAdapter;

/**
 * Converts the {@link CardUi} to {@link Card} and adapts the results found after running the BDF 
 * to a solution that can be accepted by the {@link ExternalAdapter} and {@link ApplicationMouseHandler}
 * @author C.LS
 *
 */
public class FlipSolver {

	private Mode mode;
	BreadthFirstSearch bfs;

	public static enum Mode {
		SIMPLE, EXTENDED;
	}

	public FlipSolver(Mode mode) {
		this.mode = mode;
	}

	public List<Integer> solve(List<CardUi> state) {
		bfs = new BreadthFirstSearch();

		List<Card> cards = new ArrayList<>();
		Integer activeCard = null;
		for (CardUi cardUi : state) {
			cards.add(new Card(cardUi.getValue(), cardUi.isFaceUp()));
			if (cardUi.isActive()) {
				activeCard = cardUi.getValue();
			}
		}
		CardList cardList = null;
		Node<CardList> start = null;
		Condition<CardList> condition = null;
		if (mode == Mode.SIMPLE) {
			cardList = new CardList(cards, activeCard);
			start = new CardListSimpleNode(cardList);
			condition = new WinConditionSimple();
		} else {
			// cardList = new CardListExt(cards, activeCard);
			// start = new CardListSimpleNode(cardList);
			// condition = new WinConditionExt();
		}

		List<Node<CardList>> directions = bfs.getDirections(start, condition);
		for (Node<CardList> node : directions) {
			System.out.println(node.getElement());
		}
		return convert(directions);
	}

	private List<Integer> convert(List<? extends Node<? extends CardList>> directions) {
		List<Integer> result = new ArrayList<>();
		for (int i = 0; i < directions.size() - 1; i++) {
			CardList cl1 = directions.get(i).getElement();
			CardList cl2 = directions.get(i + 1).getElement();
			List<Integer> swapped = findSwappedCards(cl1, cl2);
			Integer sum = directions.get(i).getElement().getSum();
			if (sum != null) {
				swapped.remove(Integer.valueOf(sum));
			}
			result.addAll(swapped);
		}
		System.out.println(result);
		return result;

	}

	private List<Integer> findSwappedCards(CardList cl1, CardList cl2) {
		List<Integer> result = new ArrayList<>();
		for (int i = 0; i < cl1.size(); i++) {
			if (cl1.get(i).getValue() != cl2.get(i).getValue()) {
				result.add(cl1.get(i).getValue());
			}
		}
		return result;
	}

	public void cancel() {
		if (bfs != null) {
			bfs.cancel();
		}

	}
}
