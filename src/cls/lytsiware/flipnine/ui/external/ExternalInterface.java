package cls.lytsiware.flipnine.ui.external;

import java.util.List;

public interface ExternalInterface {
	
	/**
	 * Activates the card with the provided value. Use this method in order to access
	 * programmatically the ui.
	 * <p>This method blocks until the UI has finished its animations
	 * 
	 * @param cardValue the value of the card
	 */
	void activateCard(int cardValue);
	
	/**
	 * provides the current board state. The board state is made by the 
	 * cards in their current order (depicted in the return list) and state.
	 * @return
	 */
	List<Integer> getBoardState();

}
