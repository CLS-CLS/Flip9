package cls.lytsiware.flipnine.ui.animation;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.util.Duration;
import cls.lytsiware.common.animation.Animation;

public class SwapAnimation implements Animation{
	
	BooleanProperty running = new SimpleBooleanProperty();
	private Node card1;
	private Node card2;
	
	public SwapAnimation(Node card1, Node card2) {
		this.card1 = card1;
		this.card2 = card2;
		
	}

	@Override
	public void animate() {
		running.set(true);
		double startX = card1.getLayoutBounds().getWidth() / 2;
		double startY = card1.getLayoutBounds().getHeight() / 2;
		
		double startX2 = card2.getLayoutBounds().getWidth() / 2;
		
		double endX = card2.getLayoutX() - card1.getLayoutX() + startX;
		double endX2 = card1.getLayoutX() - card2.getLayoutX() + startX2;
		double endY = startY;
		double controlX =  (card2.getLayoutX() - card1.getLayoutX())/2; //endX / 2;
		double controlX2 = (card1.getLayoutX() - card2.getLayoutX())/2;
		double controlY = 100;
		
		final double layoutX1 = card1.getLayoutX();
		final double layoutX2 = card2.getLayoutX();
		Path pathP = new Path();
		Path pathM = new Path();
		pathP.getElements().add(new MoveTo(startX, startY));
		pathP.getElements().add(new QuadCurveTo(controlX, 2*controlY, endX, endY));
		pathM.getElements().add(new MoveTo(startX2, startY));
		pathM.getElements().add(new QuadCurveTo(controlX2, -controlY, endX2, endY));
		final PathTransition pt = new PathTransition(Duration.millis(400), pathP, card1);
		final PathTransition pt2 = new PathTransition(Duration.millis(400), pathM, card2);
		pt.setOrientation(OrientationType.NONE);
		pt.play();
		pt.setInterpolator(Interpolator.EASE_BOTH);
		pt2.play();
		pt2.setInterpolator(Interpolator.EASE_BOTH);
		
		//Workaround because javaFX sucks hard!
		pt.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				card1.setLayoutX(layoutX2);
				card1.setTranslateX(0);
				running.set(pt2.getCurrentTime().lessThan(pt2.getDuration()));
				
			}
		});
		pt2.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				card2.setLayoutX(layoutX1);
				card2.setTranslateX(0);
				running.set(pt.getCurrentTime().lessThan(pt.getDuration()));
			}
		});
		
	}

	@Override
	public ReadOnlyBooleanProperty getRunningProperty() {
		return running;
		
	}

}
