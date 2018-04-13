import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * Creates a standard Monster
 *
 * @author mastermk, wangj14, and zous. Created Jan 26, 2018.
 */
public abstract class Monster extends Creature {
	private Map map;
	private int moveSpeed = 8;
	private int originalX, originalY;
	private int timeInflated = 0;
	protected Main main;
	protected boolean ghost = false;

	/**
	 * Returns the value of the field called 'moveSpeed'.
	 * 
	 * @return Returns the moveSpeed.
	 */
	public int getMoveSpeed() {
		return this.moveSpeed;
	}

	/**
	 * Sets the field called 'moveSpeed' to the given value.
	 * 
	 * @param moveSpeed
	 *            The moveSpeed to set.
	 */
	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	/**
	 * Creates a generic monster
	 *
	 * @param x
	 * @param y
	 * @param frame
	 * @param map
	 * @param main
	 */
	public Monster(int x, int y, JFrame frame, Map map, Main main) {
		super(x, y, frame, main);
		this.map = map;
		super.setDirection('d');
		this.originalX = x;
		this.originalY = y;
		this.main = main;
	}

	/**
	 * Returns the value of the field called 'originalX'.
	 * 
	 * @return Returns the originalX.
	 */
	public int getOriginalX() {
		return this.originalX;
	}

	/**
	 * Returns the value of the field called 'originalY'.
	 * 
	 * @return Returns the originalY.
	 */
	public int getOriginalY() {
		return this.originalY;
	}

	@Override
	public void attack() {
		specialAttack();
		Rectangle2D.Double monsterGraphics = new Rectangle2D.Double(super.getX(), super.getY(), 32, 32);
		Rectangle2D.Double heroGraphics = new Rectangle2D.Double(this.main.getHero().getX(), this.main.getHero().getY(),
				32, 32);
		if (monsterGraphics.intersects(heroGraphics)) {
			this.main.getHero().die();
			return;
		}

	}

	/**
	 * Allows the Monsters to go between cleared areas
	 *
	 */
	public void ghost() {
		if (this.ghost == true) {
			int goToX = this.main.getHero().getX();
			int goToY = this.main.getHero().getY();
			int currentX = this.getX();
			int currentY = this.getY();

			if (goToX > currentX) {
				if (goToX - currentX >= 8)
					this.move(8, 0);
				else
					this.setX(goToX);
			} else if (goToX < currentX) {
				if (currentX - goToX >= 8)
					this.move(-8, 0);
				else
					this.setX(goToX);
			}

			if (goToY > currentY) {
				if (goToY - currentY >= 8)
					this.move(0, -8);
				else
					this.setY(goToY);
			} else if (goToY < currentY) {
				if (currentY - goToY >= 8)
					this.move(0, 8);
				else
					this.setY(goToY);
			}

			if (this.map.fourByFourIsEmpty(this.getBlockX(), this.getBlockY())) {
				this.ghost = false;
			}
		}
	}

	@Override
	public void drawOn(Graphics2D g2) {
		String direction = ("" + super.getDirection()).toUpperCase();
		String fileName = this.getClass().getSimpleName().toLowerCase();
		if (this.ghost) {
			fileName += "Ghost.png";
		} else {
			fileName += direction + ".png";
		}
		BufferedImage img;
		try {
			img = ImageIO.read(new File(fileName));
			g2.drawImage(img, super.getX(), super.getY(), 32, 32, null);
		} catch (IOException e) {
			g2.setColor(Color.RED);
			Rectangle2D.Double monsterGraphics = new Rectangle2D.Double(super.getX(), super.getY(), 32, 32);
			g2.fill(monsterGraphics);
		}
		drawSpecialAttack(g2);

	}

	/**
	 * calls the Move method using the movement strategies that are given
	 *
	 */
	public void updateLocation() {
		boolean attacked = beAttacked();
		if (!attacked) {
			alterMovement();
			int shouldGhost = (int) (Math.random() * 1000 / this.main.getLevel());
			if (shouldGhost == 1) {
				this.ghost = true;
			}
			ghost();

			if (!(LeftIsEmpty() && rightIsEmpty() && aheadIsEmpty())) {
				if (rightIsEmpty())
					this.turnRight();
				if (!aheadIsEmpty())
					turnLeft();
			} else if (!lowerRightIsEmpty())
				this.turnRight();
			boolean moved = moveForward();
			if (!moved) {
				turnLeft();
			}
			attack();
		}
	}

	/**
	 * Handles the monster being attacked
	 * 
	 * @return if the monster is being attacked
	 *
	 */
	public boolean beAttacked() {
		Rectangle2D.Double monsterGraphics = new Rectangle2D.Double(super.getX(), super.getY(), 32, 32);
		if (monsterGraphics.intersects(this.main.getHero().getHitBlock()) && !this.main.getHero().getHitting()) {
			inflate();
			this.moveSpeed = 0;
			this.main.getHero().setHitting(true);
			this.main.getHero().setTarget(this);
			return true;
		}
		return false;
	}

	/**
	 * Checks if the attempted motion is valid
	 *
	 * @param dx
	 * @param dy
	 * @return true if there is no overlap
	 */
	public boolean checkValidMotion(int dx, int dy) {
		if (this.getBlockX() + (dx / 8) < 0) {
			return false;
		}
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 4; i++) {
				int newXBlock = super.getBlockX() + i + (dx / 8);
				int newYBlock = super.getBlockY() + j + (dy / 8);
				if (!this.map.isEmpty(newXBlock, newYBlock)) {
					return false;
				}
			}
		}
		return super.checkForRock(this.getX(), this.getY());
	}

	/**
	 * Tells us if the right is empty
	 *
	 * @return if there is no block to the right return true
	 */
	public boolean rightIsEmpty() {
		switch (super.getDirection()) {
		case 'u':
			return checkValidMotion(8, 0);
		case 'd':
			return checkValidMotion(-8, 0);
		case 'l':
			return checkValidMotion(0, 8);
		case 'r':
			return checkValidMotion(0, -8);
		default:
			break;
		}
		return false;
	}

	/**
	 * Tells us if the right is empty
	 *
	 * @return if there is no block to the right return true
	 */
	public boolean LeftIsEmpty() {

		switch (super.getDirection()) {
		case 'u':
			return checkValidMotion(-8, 0);
		case 'd':
			return checkValidMotion(8, 0);
		case 'l':
			return checkValidMotion(0, -8);
		case 'r':
			return checkValidMotion(0, 8);
		default:
			break;
		}
		return false;
	}

	/**
	 * If there is nothing blocking moving ahead
	 *
	 * @return
	 */
	private boolean aheadIsEmpty() {
		switch (super.getDirection()) {
		case 'u':
			return checkValidMotion(0, -8);
		case 'd':
			return checkValidMotion(0, 8);
		case 'l':
			return checkValidMotion(-8, 0);
		case 'r':
			return checkValidMotion(8, 0);
		default:
			break;
		}
		return false;
	}

	/**
	 * If there is no block on the lower right corner of hero
	 *
	 * @return
	 */
	private boolean lowerRightIsEmpty() {
		switch (super.getDirection()) {
		case 'u':
			return checkValidMotion(8, 8);
		case 'd':
			return checkValidMotion(-8, -8);
		case 'l':
			return checkValidMotion(8, -8);
		case 'r':
			return checkValidMotion(-8, 8);
		default:
			break;
		}
		return false;
	}

	/**
	 * Turns the direction the monster is facing once clockwise
	 *
	 */
	public void turnRight() {
		switch (super.getDirection()) {
		case 'u':
			super.setDirection('r');
			return;
		case 'd':
			super.setDirection('l');
			return;
		case 'l':
			super.setDirection('u');
			return;
		case 'r':
			super.setDirection('d');
			return;
		default:
			break;
		}
	}

	/**
	 * Turns the direction the monster is facing once counterclockwise
	 *
	 */
	public void turnLeft() {
		switch (super.getDirection()) {
		case 'u':
			super.setDirection('l');
			return;
		case 'd':
			super.setDirection('r');
			return;
		case 'l':
			super.setDirection('d');
			return;
		case 'r':
			super.setDirection('u');
			return;
		default:
			break;
		}
	}

	/**
	 * moves the monster forward one block
	 *
	 * @return if the monster moves or not
	 */
	public boolean moveForward() {
		int dx = 0;
		int dy = 0;

		switch (super.getDirection()) {
		case 'u':
			dy = this.moveSpeed;
			break;
		case 'd':
			dy = -1 * this.moveSpeed;
			break;
		case 'l':
			dx = -1 * this.moveSpeed;
			break;
		case 'r':
			dx = this.moveSpeed;
			break;
		default:
			break;
		}
		if (!aheadIsEmpty()) {
			return false;
		}
		move(dx, dy);
		return true;
	}

	@Override
	public void die() {
		super.main.addMonsterToDie(this);

	}

	/**
	 * Inflate the monster once
	 *
	 */
	public void inflate() {
		if (this.timeInflated == 3) {
			this.die();
			this.main.getHero().setTarget(null);
			this.main.getHero().setOut(false);
			this.main.getHero().setHitting(false);
		} else {
			this.timeInflated++;
		}
	}

	/**
	 * Special attack for each monster
	 *
	 */
	public abstract void specialAttack();

	/**
	 * Draw anything needed for special attack
	 * 
	 * @param g2
	 *
	 */
	public abstract void drawSpecialAttack(Graphics2D g2);

	/**
	 * Changes the movement depending on type of monster
	 *
	 */
	public abstract void alterMovement();
}
