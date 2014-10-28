package cls.lytsiware.common.animation;

import javafx.beans.property.ReadOnlyBooleanProperty;

/**
 *  The animation that should be executed some point at the time by the {@linkplain FxThreadAnimate}.
 *  <p> When the animation is about to be executed; {@link FxThreadAnimate} will call the method {@link #animate()}. 
 *  After the call to {@link #animate()} the animation should update its runningProperty to true. When the animation is over
 *  the runningPropertu should be set to false. Other classed can retrieve the running status of this animation via the 
 *  offered method {@link #getRunningProperty()}.
 */
public interface Animation {
	
	/**
	 * The method runs the animation and updates the runningProperty accordingly. When it is executed the animation 
	 * should update the runningProperty to true. When the animation is over the runningProperty should be updated to false. 
	 */
	public void animate();
	
	/**
	 * A property to allow listeners to get notifications on any change of the running status of the animation.
	 * @return the property which reflects the 'running' status of the animation
	 */
	public ReadOnlyBooleanProperty getRunningProperty();
	

}
