package cls.lytsiware.flipnine.ui.undo;

import java.util.Collections;
import java.util.List;

import cls.lytsiware.common.animation.FxThreadAnimate;
import cls.lytsiware.flipnine.ui.CardUi;
import cls.lytsiware.flipnine.ui.animation.SwapAnimation;
import cls.lytsiware.flipnine.ui.animation.TurnCard;

public class Action {
	static FxThreadAnimate fxAnimator;
	static List<CardUi> cards;

	private CardUi activeCard;
	private CardUi card1;
	private CardUi card2;
	
	public Action(CardUi card1, CardUi card2, CardUi activeCard){
		this.card1 = card1;
		this.card2 = card2;
		this.activeCard = activeCard;
	}
	public Action(){
		
	}

	public boolean undo() {
		//TODO the check should have been done already. 
		if (fxAnimator.isRunning()) {
			return false;
		}
		if (card2 != null){
			fxAnimator.run(new SwapAnimation(card1, card2));
			fxAnimator.run(new TurnCard(card1, card2));
			Collections.swap(cards, cards.indexOf(card1), cards.indexOf(card2));
		}
		inactivateCurrentActiveCard();
		if (activeCard != null){
			activeCard.setActive(true);
		}
		return true;
	}
	
	private void inactivateCurrentActiveCard(){
		for (CardUi card : cards) {
			if (card.isActive()) {
				card.setActive(false);
				break;
			}
		}
	}

}
