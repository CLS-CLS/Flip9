package cls.lytsiware.flipnine.ai.onecolor;

import cls.lytsiware.common.search.breadth.Condition;
import cls.lytsiware.flipnine.ai.CardList;

public class WinConditionSimple implements Condition<CardList> {

	CardList winCardList = new CardList(1, 2, 3, 4, 5, 6, 7, 8, 9);

	@Override
	public boolean isMet(CardList current) {
		boolean win = true;
		for (int i = 0; i < current.size(); i++) {
			if (winCardList.get(i).getValue() != current.get(i).getValue()) {
				win = false;
				break;
			}
		}
		return win;
	}

}
