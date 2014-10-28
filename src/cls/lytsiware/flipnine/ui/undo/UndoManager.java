
package cls.lytsiware.flipnine.ui.undo;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import cls.lytsiware.common.animation.FxThreadAnimate;
import cls.lytsiware.flipnine.ui.CardUi;

public class UndoManager {
	/**
	 * The actions that can be undone
	 */
	List<Action> undos = new ArrayList<>();
	
	/**
	 * the number of actions that can be undone. Can be used by the UI to
	 * enable / disable the unde action button
	 */
	IntegerProperty undoSize = new SimpleIntegerProperty(0);
	
	public UndoManager(FxThreadAnimate fxAnimator, List<CardUi> cards) {
		Action.fxAnimator = fxAnimator;
		Action.cards = cards;
	}
	
	public void addUndoAction(Action action){
		undos.add(0,action);
		undoSize.set(undos.size());
	}
	
	/**
	 * Undo the last action. The undo action may not be done if there is
	 * an animation running. 
	 * TODO : the animation running check should be done before the 
	 * undo is called.
	 */
	public void undoLastAction(){
		if (undos.size()==0){
			return;
		}
		Action undoAction = undos.get(0);
		boolean done = undoAction.undo();
		if (done){
			undos.remove(0);
			undoSize.set(undos.size());
		}
		
	}
	
	/**
	 * Clears the undo actions
	 */
	public void clear() {
		undos.clear();
		undoSize.set(undos.size());
	}
	
	public ReadOnlyIntegerProperty undoSizeProperty(){
		return undoSize;
	}

}
