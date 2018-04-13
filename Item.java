import java.awt.Graphics2D;

/**
 * Build items in game.
 *
 * @author mastermk, wangj14, and zous. Created Jan 26, 2018.
 */
public abstract class Item {

	private int xPosition;
	private int yPosition;
	private boolean visible;

	/**
	 * Contructs the items in a given position
	 *
	 * @param x
	 * @param y
	 * @param visible
	 */
	public Item(int x, int y, boolean visible) {
		this.xPosition = x;
		this.yPosition = y;
		this.visible = visible;
	}

	/**
	 * Returns the value of the field called 'xPosition'.
	 * 
	 * @return Returns the xPosition.
	 */
	public int getxPosition() {
		return this.xPosition;
	}

	/**
	 * Sets the field called 'xPosition' to the given value.
	 * 
	 * @param xPosition
	 *            The xPosition to set.
	 */
	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	/**
	 * Returns the value of the field called 'yPosition'.
	 * 
	 * @return Returns the yPosition.
	 */
	public int getyPosition() {
		return this.yPosition;
	}

	/**
	 * Sets the field called 'yPosition' to the given value.
	 * 
	 * @param yPosition
	 *            The yPosition to set.
	 */
	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}

	/**
	 * Makes the item appear on the screen
	 *
	 */
	public void appear() {
		this.visible = true;
	}

	/**
	 * Makes the item no longer on the screen
	 *
	 */
	public void disappear() {
		this.visible = false;
	}

	/**
	 * Returns the value of the field called 'visible'.
	 * 
	 * @return Returns the visible.
	 */
	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * Draws the grpahics on the screen
	 *
	 * @param g2
	 */
	public abstract void drawOn(Graphics2D g2);
}
