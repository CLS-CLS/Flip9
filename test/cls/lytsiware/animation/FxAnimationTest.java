package cls.lytsiware.animation;

import cls.lytsiware.common.animation.Animation;
import cls.lytsiware.common.animation.AnimationObserver;
import cls.lytsiware.common.animation.FxThreadAnimate;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FxAnimationTest extends Application {

	private static int counter = 0;

	class AnimImpl implements Animation {

		private String id = "";
		private int defualtDuration = 3000;

		/**
		 * @param defualtDuration
		 */
		public AnimImpl(int defualtDuration) {
			id = "" + ++counter;
			this.defualtDuration = defualtDuration;
		}

		public AnimImpl() {
			id = "" + ++counter;
		}

		private SimpleBooleanProperty runningProperty = new SimpleBooleanProperty();

		@Override
		public void animate() {
			System.out.println(this + " -- start ");
			runningProperty.set(true);
			Timeline tl = new Timeline();
			tl.getKeyFrames()
					.add(new KeyFrame(Duration.millis(defualtDuration), new KeyValue[] {}));
			tl.setOnFinished(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					System.out.println(AnimImpl.this + " -- End ");
					runningProperty.set(false);
				}
			});
			tl.play();
		}

		@Override
		public ReadOnlyBooleanProperty getRunningProperty() {
			return runningProperty;
		}

		@Override
		public String toString() {
			return "AnimImpl [+" + id + "]";

		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(new Group(), 100, 100));
		primaryStage.show();
		final FxThreadAnimate fxThreadAnim = new FxThreadAnimate();
		new Thread(new Runnable() {

			@Override
			public void run() {
				synchronized (fxThreadAnim) {

					fxThreadAnim.register(new AnimationObserver() {

						@Override
						public void animationsFinished() {
							System.out.println("External Observer - ALL Animations are OVER  ");

						}
					});
				}
			}
		}).start();
		;
		final AnimImpl an = new AnimImpl();
		final AnimImpl an2 = new AnimImpl();
		AnimImpl an3 = new AnimImpl(10000);
		fxThreadAnim.queue(an);
		fxThreadAnim.run(an3);
		fxThreadAnim.queue(an2);

		fxThreadAnim.register(new AnimationObserver() {

			@Override
			public void animationsFinished() {
				System.out.println("Obs -2 ALL Animations are OVER ");
			}
		});

	}

	public static void main(String[] args) {
		launch(args);
	}

}
