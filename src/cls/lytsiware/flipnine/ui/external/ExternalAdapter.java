package cls.lytsiware.flipnine.ui.external;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import cls.lytsiware.common.animation.AnimationObserver;
import cls.lytsiware.common.animation.FxThreadAnimate;
import cls.lytsiware.flipnine.ui.ApplicationMouseHandler;
import cls.lytsiware.flipnine.ui.CardUi;

/**
 * provides methods in order for external systems to access the UI programatically. 
 * Usually the methods will be called by an AI in order to show the solution.
 *
 */
public class ExternalAdapter implements ExternalInterface {
	int counter = 0;

	ApplicationMouseHandler mouseHandler;
	FxThreadAnimate cardAnimator;
	private List<CardUi> cards;
	private volatile boolean ready;

	/**
	 * @param mouseHandler
	 * @param cardAnimator
	 */
	public ExternalAdapter(ApplicationMouseHandler mouseHandler, FxThreadAnimate cardAnimator,
			List<CardUi> cards) {
		this.mouseHandler = mouseHandler;
		this.cardAnimator = cardAnimator;
		this.cards = cards;
		cardAnimator.register(createListener());
	}

	public AnimationObserver createListener() {
		return new AnimationObserver() {

			@Override
			public void animationsFinished() {
				synchronized (cardAnimator) {
					ready = true;
					cardAnimator.notifyAll();
				}
			}
		};
	}
	
	/**
	 * Use with caution. The method only provides the initial state of the cards
	 * as the program does not currently holds the actual place of the cards in the board
	 * after the game starts.
	 */
	@Override
	public List<Integer> getBoardState() {
		List<Integer> boardState = new ArrayList<>();
		for (CardUi c : cards) {
			boardState.add(c.getValue());
		}
		return boardState;
	}
	
	
	@Override
	public void activateCard(final int cardValue) {
		System.out.println("activating card" + cardValue);
		CardUi cardUi = null;
		for (CardUi nc : cards) {
			if (nc.getValue() == cardValue) {
				cardUi = nc;
				break;
			}
		}
		final CardUi cardToSwap = cardUi;
		// Scanner sc = new Scanner(System.in);
		// sc.nextLine();
		synchronized (cardAnimator) {
			if (cardAnimator.isRunning()) {
				try {
					cardAnimator.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			ready = false;
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					mouseHandler.swapCards(cardToSwap);
				}
			});
			while (counter++ != 0 && !ready) {
				try {
					cardAnimator.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
