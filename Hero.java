import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * Build the hero in game.
 *
 * @author mastermk, wangj14, and zous. Created Jan 26, 2018.
 */
public class Hero extends Creature {

	private boolean attacking = false;
	private boolean out = true;
	private boolean hitting = false;
	private int length = 0;
	private Rectangle2D.Double hitBlock = new Rectangle2D.Double(0, 0, 0, 0);
	private JFrame frame;
	private static int moveSpeed = 8;
	private int lives;
	private Monster target;

	/**
	 * Hero Constructor
	 *
	 * @param frame
	 * @param main
	 */
	public Hero(JFrame frame, Main main) {
		super(frame.getWidth() / 2, (frame.getHeight() - 80) / 2, frame, main);
		this.frame = frame;
		this.lives = 3;

	}

	@Override
	public void attack() {
		if (this.attacking) {
			Hero.moveSpeed = 0;
			if (this.out) {
				int dx = 0;
				int dy = 0;
				switch (this.getDirection()) {
				case 'u':
					dy = -this.length;
					break;
				case 'd':
					dy = 4 + this.length;
					break;
				case 'r':
					dx = 4 + this.length;
					break;
				case 'l':
					dx = -this.length;
					break;
				default:
					break;
				}
				int nextBlockX = this.getBlockX() + dx;
				int nextBlockY = this.getBlockY() + dy;
				if (this.length == 12) {
					this.hitBlock = new Rectangle2D.Double(0, 0, 0, 0);
					this.out = false;
				} else if (!this.main.getCurrentMap().isEmpty(nextBlockX, nextBlockY)) {
					this.out = false;
				} else if (!this.hitting) {
					dx = 0;
					dy = 0;
					switch (this.getDirection()) {

					case 'u':
						dy = -(this.length + 2) * 8;
						break;
					case 'd':
						dy = (this.length + 2) * 8 + 32;
						break;
					case 'r':
						dx = (this.length + 2) * 8 + 32;
						break;
					case 'l':
						dx = -(this.length + 2) * 8;
						break;
					default:
						break;
					}
					double initX = this.getX() + dx;
					double initY = this.getY() + dy;
					this.hitBlock = new Rectangle2D.Double(initX, initY, 32, 16);
					this.length += 2;
				}
			} else {
				if (this.length == 0) {
					this.attacking = false;
					this.out = true;
					Hero.moveSpeed = 8;
				} else {
					this.length -= 2;
				}
			}
		}
	}

	/**
	 * Sets if the hero is attacking
	 *
	 * @param map
	 */
	public void updateAttack() {
		if (!this.attacking) {
			this.attacking = true;
		} else if (this.target != null) {
			this.target.inflate();
		}
	}

	/**
	 * Returns the rectangle that forms the pump
	 *
	 * @return the attack zone of the player
	 */
	public Rectangle2D.Double getHitBlock() {
		return this.hitBlock;
	}

	/**
	 * Sets the hit block of the monster
	 *
	 * @param r
	 */
	public void setHitBlock(Rectangle2D.Double r) {
		this.hitBlock = r;
	}

	/**
	 * Gets if the hero is hitting
	 *
	 * @return hitting
	 */
	public boolean getHitting() {
		return this.hitting;
	}

	/**
	 * Sets hitting to the input value
	 *
	 * @param b
	 */
	public void setHitting(boolean b) {
		this.hitting = b;
	}

	/**
	 * set target monster for Hero
	 *
	 * @param target
	 */
	public void setTarget(Monster target) {
		if (target == null && this.target != null) {
			this.target.setMoveSpeed(8);
		}
		this.target = target;
	}

	/**
	 * set if the pump is going out.
	 *
	 * @param b
	 */
	public void setOut(boolean b) {
		this.out = b;
	}

	/**
	 * set attacking boolean
	 *
	 * @param b
	 */
	public void setAttacking(boolean b) {
		this.attacking = b;
	}

	/**
	 * set pump length
	 *
	 * @param i
	 */
	public void setLength(int i) {
		this.length = i;
	}

	/**
	 * set hero movement speed
	 *
	 * @param i
	 */
	public void setSpeed(int i) {
		Hero.moveSpeed = i;
	}

	/**
	 * Return the speed of Hero.
	 *
	 * @return
	 */
	@SuppressWarnings("javadoc")
	public int getSpeed() {
		return Hero.moveSpeed;
	}

	@Override
	public void drawOn(Graphics2D g2) {
		if (this.attacking) {
			drawPump(g2);
		}
		String direction = ("" + super.getDirection()).toUpperCase();
		String fileName = "hero" + direction + ".png";
		BufferedImage img;
		try {
			img = ImageIO.read(new File(fileName));
			g2.drawImage(img, super.getX(), super.getY(), 32, 32, null);
		} catch (IOException e) {
			g2.setColor(Color.GREEN);
			Rectangle2D.Double heroGraphics = new Rectangle2D.Double(super.getX(), super.getY(), 32, 32);
			g2.fill(heroGraphics);
		}
	}

	@Override
	public void die() {
		if (this.lives == 0) {
			System.out.println("Game Over!");
			System.exit(0);
		}
		super.setX(this.frame.getWidth() / 2);
		super.setY((this.frame.getHeight() - 80) / 2);
		for (Monster m : this.main.getMonsters()) {
			m.setX(m.getOriginalX());
			m.setY(m.getOriginalY());
		}
		this.lives--;
	}

	/**
	 * Draw's the pump
	 *
	 * @param g2
	 */
	public void drawPump(Graphics2D g2) {
		double initBlockX = this.getBlockX();
		double initBlockY = this.getBlockY() + 10;
		double headInitBlockX = initBlockX;
		double headInitBlockY = initBlockY;
		double pipeLength = 0;
		if (this.length >= 2) {
			pipeLength = this.length - 2;
		}
		boolean isVertical = false;
		Rectangle2D pumpHead = new Rectangle2D.Double(0, 0, 0, 0);

		switch (this.getDirection()) {
		case 'u':
			initBlockY -= pipeLength;
			headInitBlockY = initBlockY - 2;
			isVertical = true;
			break;
		case 'd':
			initBlockY += 4;
			headInitBlockY = initBlockY + pipeLength;
			isVertical = true;
			break;
		case 'l':
			initBlockX -= pipeLength;
			headInitBlockX = initBlockX - 2;
			break;
		case 'r':
			initBlockX += 4;
			headInitBlockX = initBlockX + pipeLength;
			break;
		default:
			break;
		}
		Rectangle2D pumpPipe = new Rectangle2D.Double(0, 0, 0, 0);
		String fileName;
		BufferedImage img;
		try {
			int drawH = 0;
			int drawV = 0;
			if (isVertical) {
				fileName = "pumpBodyV.png";
				img = ImageIO.read(new File(fileName));
				drawH = 32;
				drawV = (int) pipeLength * 8;
				pumpHead = new Rectangle2D.Double(headInitBlockX * 8, headInitBlockY * 8, 32, 16);
			} else {
				fileName = "pumpBodyH.png";
				img = ImageIO.read(new File(fileName));
				drawH = (int) pipeLength * 8;
				drawV = 32;
				pumpHead = new Rectangle2D.Double(headInitBlockX * 8, headInitBlockY * 8, 16, 32);
			}
			g2.drawImage(img, (int) initBlockX * 8, (int) initBlockY * 8, drawH, drawV, null);
		} catch (IOException e) {
			g2.setColor(Color.BLACK);
			g2.fill(pumpPipe);
		}
		g2.setColor(Color.BLACK);
		g2.fill(pumpHead);

	}

	/**
	 * Returns the value of the field called 'lives'.
	 * 
	 * @return Returns the lives.
	 */
	public int getLives() {
		return this.lives;
	}

	/**
	 * if the player intersects the fruit
	 *
	 */
	public void eatFruit() {
		for (Fruit f : this.main.getFruits()) {
			if (this.getHitBox().intersects(new Rectangle2D.Double(f.getxPosition(), f.getyPosition(), 32, 32))) {
				this.main.setScore(this.main.getScore() + f.getPoints());
				f.disappear();
			}
		}
	}
}
