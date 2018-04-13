import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;

/**
 * Class for hero and monsters.
 *
 * @author mastermk, wangj14, and zous. Created Jan 26, 2018.
 */
public abstract class Creature {
	private int x;
	private int y;
	private JFrame frame;
	private char direction;
	protected Main main;
	private Rectangle2D.Double hitBox;

	/**
	 * Creates a standard creature
	 *
	 * @param x
	 * @param y
	 * @param frame
	 * @param main
	 */
	public Creature(int x, int y, JFrame frame, Main main) {
		super();
		this.x = x;
		this.y = y;
		this.frame = frame;
		this.direction = 'u';
		this.main = main;
		this.setHitBox(new Rectangle2D.Double(x, y, 32, 32));
	}

	/**
	 * Handle creature attack.
	 */
	public abstract void attack();

	/**
	 * Implement how creatures die/disappear from map.
	 */
	public abstract void die();

	/**
	 * movement
	 *
	 * @param dx
	 * @param dy
	 */
	public void move(int dx, int dy) {
		this.setHitBox(new Rectangle2D.Double(this.x, this.y, 32, 32));
		int cornerToEdgeX = 40;
		int cornerToEdgeY = 96;
		int previousX = this.x;
		int previousY = this.y;
		int dx2 = dx;
		this.x += dx2;
		int dy2 = dy;
		this.y -= dy2;

		// check if it is past the edges
		if (this.x >= this.frame.getWidth() - cornerToEdgeX) {
			this.x = this.frame.getWidth() - cornerToEdgeX;
		} else if (this.x <= 0) {
			this.x = 0;
		}

		if (this.y >= this.frame.getHeight() - cornerToEdgeY) {
			this.y = this.frame.getHeight() - cornerToEdgeY;
		} else if (this.y <= 0 + 96) {
			this.y = 96;
		}

		if (!checkForRock(previousX, previousY)) {
			dx2 = -1 * dx2;
			dy2 = -1 * dy2;
		}

		if (dx2 > 0) {
			this.direction = 'r';
		} else if (dy2 > 0) {
			this.direction = 'u';
		} else if (dx2 < 0) {
			this.direction = 'l';
		} else if (dy2 < 0) {
			this.direction = 'd';
		}

	}

	/**
	 * Returns true if there is a rock, and false if there is not.
	 *
	 * @param previousX
	 * @param previousY
	 * @return If there is a rock
	 */
	public boolean checkForRock(int previousX, int previousY) {
		// check if there is rock ahead
		for (Rock rock : this.main.getRocks()) {
			int distX = this.x - rock.getxPosition();
			int distY = this.y - rock.getyPosition();
			if (Math.abs(distX) <= 30 && Math.abs(distY) <= 30) {
				this.x = previousX;
				this.y = previousY;
				return false;
			}
		}
		return true;
	}

	/**
	 * Set the x-location
	 *
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Set the y-location
	 *
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Get the x-coordinate
	 *
	 * @return x
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Get the y-coordinate
	 *
	 * @return y
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Get the x-coordinate of the block
	 * 
	 * @return x block
	 */
	public int getBlockX() {
		return this.x / 8;
	}

	/**
	 * Get the y-coordinate of the block
	 * 
	 * @return y block
	 */
	public int getBlockY() {
		return (this.y - 80) / 8;
	}

	/**
	 * Set the direction a creature is facing
	 *
	 * @param d
	 */
	public void setDirection(char d) {
		this.direction = d;
	}

	/**
	 * get what direction the creature is facing
	 *
	 * @return the direction the creature is facing
	 */
	public char getDirection() {
		return this.direction;
	}

	/**
	 * Draws the graphics for the creature
	 *
	 * @param g
	 */
	public abstract void drawOn(Graphics2D g);

	/**
	 * Returns the value of the field called 'hitBox'.
	 * 
	 * @return Returns the hitBox.
	 */
	public Rectangle2D.Double getHitBox() {
		return this.hitBox;
	}

	/**
	 * Sets the field called 'hitBox' to the given value.
	 * 
	 * @param hitBox
	 *            The hitBox to set.
	 */
	public void setHitBox(Rectangle2D.Double hitBox) {
		this.hitBox = hitBox;
	}
}
