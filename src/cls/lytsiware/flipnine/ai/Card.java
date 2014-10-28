package cls.lytsiware.flipnine.ai;

import cls.lytsiware.flipnine.ui.CardUi;

/**
 * Used instead of {@link CardUi} to save memory.
 */
public class Card {

	private int value;
	private boolean faceUp;

	public Card(int value, boolean faceUp) {
		this.value = value;
		this.setFaceUp(faceUp);
	}

	public int getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Card(value, faceUp);
	}


	@Override
	public String toString() {
		return "Card [value=" + value + ", faceUp=" + faceUp + "]";
	}

	public boolean isFaceUp() {
		return faceUp;
	}

	public void setFaceUp(boolean faceUp) {
		this.faceUp = faceUp;
	}
}
