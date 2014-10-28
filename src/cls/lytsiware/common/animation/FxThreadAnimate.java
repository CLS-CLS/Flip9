package cls.lytsiware.common.animation;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * 
 * Contains functionality to run multiple {@link Animation} in parallel and/or in sequence in the <b>FX-Thread</b>.
 * Also provides the means for subscribers to be notified on changes of the <i>running status </i> of the animations. 
 * <p>Specifically <ul>
 * <li>1) run animations in parallel when an animation is executed in {@link #run(Animation)} </li> 
 * <li>2) run animations in sequence when an animation is executed in {@link #queue(Animation)}</li>
 * <li>3) notify observers when all the animations are over</li>
 * </ul>
 * @author C.LS
 *
 */
public class FxThreadAnimate {

	List<AnimationObserver> observers = new ArrayList<>();
	BooleanProperty finished = new SimpleBooleanProperty(true);
	ObservableList<Animation> activeAnimations = FXCollections
			.observableList(new ArrayList<Animation>());
	List<Animation> animationQueue = new ArrayList<>();

	{	
		assertFxThread();
		activeAnimations.addListener(new ListChangeListener<Animation>() {

			@Override
			public void onChanged(ListChangeListener.Change<? extends Animation> c) {
				if (activeAnimations.isEmpty() && !animationQueue.isEmpty()) {
					run(animationQueue.remove(0));
				}
			}
		});

		finished.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
					Boolean newValue) {
				synchronized (FxThreadAnimate.this) {
					if (newValue) {
						for (AnimationObserver ao : observers) {
							ao.animationsFinished();
						}
					}
				}

			}
		});
	}

	/**
	 * Adds the animation to an orderer execution queue. Any animation added by this method is
	 * guaranteed to run after this animation is over. This animation will run when
	 * 1) the preceded queued animations are over. 
	 * 2) no other animation is running.(in case an animation is added by the {@link #run(Animation)} method).
	 * 
	 * @throws IllegalStateException if it is called outside the Fx-Thread
	 * 
	 */
	public void queue(final Animation animation) {
		assertFxThread();
		if (activeAnimations.isEmpty()) {
			run(animation);
		} else {
			animationQueue.add(animation);
		}

	}
	
	/**
	 * Runs the animation now. The animation will run in parallel with any other animations that 
	 * may already run.
	 * @throws IllegalStateException if it is called outside the Fx-Thread   
	 */
	public void run(final Animation animation) throws IllegalStateException {
		assertFxThread();
		finished.set(false);
		animation.getRunningProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
					Boolean newValue) {
				if (newValue == false) {
					activeAnimations.remove(animation);
				}
				if (activeAnimations.isEmpty() && animationQueue.isEmpty()) {
					finished.set(true);
				}
			}
		});
		activeAnimations.add(animation);
		animation.animate();
	}
	
	/**
	 * Registers an observer who will be notified when all the running animations have completed and there is 
	 * no queued animations pending.
	 */
	public synchronized void register(AnimationObserver observer) {
		observers.add(observer);
	}
	
	/**
	 *@return true when there is at least one application running or there are queued animations to be run 
	 * 
	 */
	public synchronized boolean isRunning(){
		return !finished.get();
	}
	
	private static final void assertFxThread(){
		if (!Platform.isFxApplicationThread()){
			throw new IllegalStateException("execution is not allowed outside FX-Thread");
		}
	}

}
