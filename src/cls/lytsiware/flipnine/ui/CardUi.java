package cls.lytsiware.flipnine.ui;

import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class CardUi extends StackPane {

	int value;
	boolean active;
	boolean faceUp;
	
	public final Text text;
	private static boolean faceStyleEnabled = true;

	public CardUi(int number) {
		this(number, true, false);
	}
	
	public CardUi(int number, boolean faceUp, boolean active){
		this(number, faceUp?Constants.STYLE_FACE_UP:Constants.STYLE_FACE_DOWN);
		this.faceUp = faceUp;
		setActive(active);
	}
	
	public CardUi(int number, String ...classIds){
		getStyleClass().add("card-default");
		text = new Text(""+number);
		text.getStyleClass().addAll(classIds);
		value = number;
		getChildren().addAll(text);
	}
	
	/**
	 * if disabled the cards will not change their style when are turned
	 * @param faceStyleEnabled
	 */
	public static void setFaceStyleEnabled(boolean faceStyleEnabled){
		CardUi.faceStyleEnabled = faceStyleEnabled;
	}
	
	/**
	 * change the style of the card to faceUp without turning it up. 
	 * Used to turn up all cards when switching to normal game from extended.
	 */
	public void setFaceUpStyleOnly(boolean faceUp){
		if (faceUp){
			text.getStyleClass().remove(Constants.STYLE_FACE_DOWN);
			text.getStyleClass().add(Constants.STYLE_FACE_UP);
		}else {
			text.getStyleClass().remove(Constants.STYLE_FACE_UP);
			text.getStyleClass().add(Constants.STYLE_FACE_DOWN);
		}
	}
	
	public void setFaceUp(boolean faceUp){
		this.faceUp = faceUp;
		if (!faceStyleEnabled){
			return;
		}
		if (faceUp){
			text.getStyleClass().remove(Constants.STYLE_FACE_DOWN);
			text.getStyleClass().add(Constants.STYLE_FACE_UP);
		}else {
			text.getStyleClass().remove(Constants.STYLE_FACE_UP);
			text.getStyleClass().add(Constants.STYLE_FACE_DOWN);
		}
	}
	
	
	public void setActive(boolean active){
		if (active){
			text.setEffect(new Glow(0.8));
			text.getStyleClass().add(Constants.STYLE_ACTIVE);
		}else {
			text.setEffect(null);
			text.getStyleClass().remove(Constants.STYLE_ACTIVE);
		}
		this.active = active;
	}

	
	public int getValue(){
		return value;
	}
	public boolean isActive(){
		return active;
	}

	public boolean isFaceUp() {
		return faceUp;
	}

}
