package cls.lytsiware.flipnine.ui.animation;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import cls.lytsiware.common.animation.Animation;
import cls.lytsiware.flipnine.ui.CardUi;

public class WinAnimation implements Animation {

	BooleanProperty running = new SimpleBooleanProperty();

	List<CardUi> cards;

	public WinAnimation(List<CardUi> cards) {
		this.cards = cards;
	}

	@Override
	public void animate() {
		running.set(true);
		Timeline timeline = new Timeline();
		KeyValue[] opacity0 = new KeyValue[cards.size()];
		KeyValue[] opacity1 = new KeyValue[cards.size()];
		for (int i=0; i<cards.size(); i++) {
			opacity0[i] = new KeyValue(cards.get(i).opacityProperty(), 0);
			opacity1[i] = new KeyValue(cards.get(i).opacityProperty(), 1);
		}
		List<KeyFrame> keyFrames = new ArrayList<>();
		for (int i = 1; i <= 6; i++) {
			keyFrames.add(new KeyFrame(Duration.millis(i * 500), i%2==1?opacity0:opacity1));
		}
		timeline.getKeyFrames().addAll(keyFrames);
		timeline.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				running.set(false);
				
			}
		});
		timeline.play();
	}

	@Override
	public ReadOnlyBooleanProperty getRunningProperty() {
		return running;
	}

}
