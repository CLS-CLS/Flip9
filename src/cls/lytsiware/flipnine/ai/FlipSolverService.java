package cls.lytsiware.flipnine.ai;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import cls.lytsiware.flipnine.ui.CardUi;

/**
 * Runs the {@link FlipSolver} to a separate thread
 *
 */
public class FlipSolverService extends javafx.concurrent.Service<List<Integer>> {

	private FlipSolver flipSolver;
	ListProperty<CardUi> cardState = new SimpleListProperty<CardUi>(this, "cardState",
			FXCollections.observableList(new ArrayList<CardUi>()));

	public final void setCardState(List<CardUi> state) {
		cardState.setAll(state);
	}

	public final List<CardUi> getCardState() {
		return cardState.get();
	}

	public FlipSolverService(FlipSolver flipSolver) {
		this.flipSolver = flipSolver;
	}

	@Override
	protected Task<List<Integer>> createTask() {
		final ObservableList<CardUi> _cardState = cardState.get();
		return new Task<List<Integer>>() {

			@Override
			protected List<Integer> call() throws Exception {
				List<CardUi> stateClone = new ArrayList<>(_cardState);
				return flipSolver.solve(stateClone);
			}
		};
	}
	
	@Override
	protected void cancelled() {
		super.cancelled();
		flipSolver.cancel();
	}

}
