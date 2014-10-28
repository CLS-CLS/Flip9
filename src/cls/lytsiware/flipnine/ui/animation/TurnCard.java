package cls.lytsiware.flipnine.ui.animation;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import cls.lytsiware.common.animation.Animation;
import cls.lytsiware.flipnine.ui.CardUi;

public class TurnCard implements Animation{
	
	BooleanProperty running = new SimpleBooleanProperty();
	private CardUi[] cards;
	
	
	public TurnCard(CardUi ...cards) {
		this.cards = cards;
	}

	@Override
	public void animate() {
		running.set(true);
		for (CardUi card:cards){
			card.setFaceUp(!card.isFaceUp());
		}
		running.set(false);
	}

	@Override
	public ReadOnlyBooleanProperty getRunningProperty() {
		return running;
		
	}
	
	

}
