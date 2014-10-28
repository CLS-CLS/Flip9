package cls.lytsiware.common.search.breadth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import cls.lytsiware.common.Node;
import cls.lytsiware.flipnine.ai.Card;
import cls.lytsiware.flipnine.ai.CardList;
import cls.lytsiware.flipnine.ai.onecolor.CardListSimpleNode;
import cls.lytsiware.flipnine.ai.onecolor.WinConditionSimple;

public class BreadthFirstSearch {

	private boolean cancelled = false;

	private Map<Node<?>, Boolean> vis = new HashMap<Node<?>, Boolean>(10000000);

	public void cancel() {
		cancelled = true;
	}
	
	/**
	 * returns the shortest path from the starting node to the node that satisfies the endCondition 
	 * @param start
	 * @param endCondition
	 * @return
	 */
	public <T> List<Node<T>> getDirections(Node<T> start, Condition<T> endCondition) {
		Map<Node<?>, Node<?>> prev = new HashMap<Node<?>, Node<?>>(10000000);
		cancelled = false;
		vis.clear();
		Node<T> successNode = null;
		List<Node<T>> directions = new LinkedList<>();
		Queue<Node<T>> q = new LinkedList<>();
		Node<T> current = start;
		q.add(current);
		vis.put(current, true);
		outer: while (!q.isEmpty() && !cancelled) {
			current = q.remove();
			List<Node<T>> siblings = current.getSiblings();
			for (Node<T> node : siblings) {
				if (!vis.containsKey(node)) {
					q.add(node);
					vis.put(node, true);
					prev.put(node, current);
					if (endCondition.isMet(node.getElement())) {
						successNode = node;
						break outer;
					}
				}
			}
		}
		if (successNode == null) {
			System.out.println("End Condition was not met");
		}

		for (Node<?> node = successNode; node != null; node = prev.get(node)) {
			@SuppressWarnings("unchecked")
			Node<T> nodeT = (Node<T>) node;
			directions.add(nodeT);
		}
		Collections.reverse(directions);
		return directions;
	}

	public long getNodesVisited() {
		return vis.size();
	}

	
	
	///////////////////////////////////////////////////////////////////////////////////////  testing
	/// can safely be deleted
	public static void main(String[] args) {
		List<Integer> init = Arrays.asList(1, 2, 3, 4);

		List<List<Integer>> permutations = permutation(Arrays.asList(5, 6, 7, 8, 9));

		for (List<Integer> subPerm : permutations) {
			subPerm.addAll(0, new ArrayList<>(init));
			Node<CardList> start = new CardListSimpleNode(CardList.fromIntegerList(subPerm, null));
			Condition<CardList> winCondition = new WinConditionSimple();
			BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch();
			long starTime = System.nanoTime();
			List<Node<CardList>> directions = breadthFirstSearch.getDirections(start, winCondition);
			System.out.println("tile elapsed :" + (System.nanoTime() - starTime) / 1000000000D);
			
			
//			for (Node<CardList> node : directions) {
				Node<CardList> node = directions.get(0);
				String toString = "";
				for (Card card : node.getElement()) {
					toString += card.getValue() + ", ";
				}
				toString += "v=" + node.getElement().getSum();
//				toString += " dist = " + getDist(node.getElement());
				toString += " moves = "+ directions.size(); 
				
				System.out.println(toString);
				System.out.println();
//			}
		}
		
	}

	public static void main2(String[] args) {
		List<Integer> intList = Arrays.asList(1, 2, 3);
		List<List<Integer>> result = permutation(new ArrayList<>(intList));
		System.out.println(result);
	}

	private static <T> List<List<T>> permutation(List<T> initialList) {
		if (initialList.size() == 2) {
			List<List<T>> result = new ArrayList<>();
			result.add(new ArrayList<>(initialList));
			ArrayList<T> swapRes = new ArrayList<>(initialList);
			Collections.swap(swapRes, 0, 1);
			result.add(swapRes);
			return result;
		} else {
			List<List<T>> fresult = new ArrayList<>();
			for (int i = 0; i < initialList.size(); i++) {
				ArrayList<T> workingList = new ArrayList<>(initialList);
				if (i != 0) {
					Collections.swap(workingList, 0, i);
				}
				T item = workingList.remove(0);
				List<List<T>> result = permutation(workingList);
				for (List<T> sub : result) {
					sub.add(0, item);
				}
				fresult.addAll(result);
			}
			return fresult;
		}

	}
}
