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
	
}
