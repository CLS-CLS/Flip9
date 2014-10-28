package cls.lytsiware.flipnine.ui;

import java.util.Collections;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import cls.lytsiware.flipnine.ui.animation.SwapAnimation;
import cls.lytsiware.flipnine.ui.animation.TurnCard;
import cls.lytsiware.flipnine.ui.animation.WinAnimation;
import cls.lytsiware.flipnine.ui.undo.Action;

public class ApplicationMouseHandler implements EventHandler<Event> {

	private Solitaire9 app;

	private boolean extended = true;

	public ApplicationMouseHandler(Solitaire9 app) {
		this.app = app;
	}

	@Override
	public void handle(Event event) {
		EventTarget target = event.getTarget();
		if (target instanceof Text) {
			Parent parent = ((Text) target).getParent();
			if (parent != null && parent instanceof CardUi) {
				swapCards((CardUi) parent);
			}
		}
	}

	public void extendedEnabled(boolean enabled) {
		this.extended = enabled;
		CardUi.setFaceStyleEnabled(enabled);
		for (CardUi card: app.cards){
			card.setFaceUpStyleOnly(!enabled || card.isFaceUp());
		}
	}

	public void swapCards(CardUi card) {
		CardUi activeCard = findActiveCard();
		if (app.fxAnimate.isRunning())
			return;
		app.flipSolverService.cancel();
		if (activeCard == null) {
			activeCard = card;
			card.setActive(true);
			app.undoManager.addUndoAction(new Action(null, null, null));
		} else if (activeCard == card) {
			return;
		} else {
			app.undoManager.addUndoAction(new Action(card, activeCard, activeCard));
			int sum = sum(card, activeCard);
			app.fxAnimate.run(new SwapAnimation(card, activeCard));
			app.fxAnimate.queue(new TurnCard(card, activeCard));
			Collections.swap(app.cards, app.cards.indexOf(card), app.cards.indexOf(activeCard));
			for (CardUi newActiveCard : app.cards) {
				if (newActiveCard.getValue() == sum) {
					activeCard.setActive(false);
					newActiveCard.setActive(true);
					activeCard = newActiveCard;
					break;
				}
			}
			if (isWin(extended)) {
				app.fxAnimate.queue(new WinAnimation(app.cards));
				app.undoManager.clear();
			}
		}
	}

	private CardUi findActiveCard() {
		for (CardUi card : app.cards) {
			if (card.isActive()) {
				return card;
			}
		}
		;
		return null;

	}

	private boolean isWin(boolean extended) {
		for (int i = 0; i < Constants.GAME_CARDS; i++) {
			if (app.cards.get(i).getValue() != i + 1) {
				return false;
			}
		}
		if (extended) {
			for (int i = 0; i < Constants.GAME_CARDS - 1; i++) {
				if (app.cards.get(i).isFaceUp() != app.cards.get(i + 1).isFaceUp()) {
					return false;
				}
			}
		}
		return true;

	}

	private static int sum(CardUi card1, CardUi card2) {
		int sum = card1.getValue() + card2.getValue();
		return sum = (sum > 9 ? sum - 9 : sum);
	}
}
