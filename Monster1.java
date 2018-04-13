import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * Creates the first variant of the monster
 *
 * @author mastermk, wangj14, and zous. Created Feb 7, 2018.
 */
public class Monster1 extends Monster {
	private int xSpecialStart, ySpecialStart, specialWidth, specialLength;
	private boolean vertical = false;
	private boolean specialAttacking;
	private static final int attackDuration = 25;
	private int attackDurationCount;

	/**
	 * Creates a green fire breathing monster
	 *
	 * @param x
	 * @param y
	 * @param frame
	 * @param map
	 * @param main
	 */
	public Monster1(int x, int y, JFrame frame, Map map, Main main) {
		super(x, y, frame, map, main);
		this.xSpecialStart = 0;
		this.ySpecialStart = 0;
		this.specialLength = 96;
		this.specialWidth = 32;
		this.attackDurationCount = 0;
	}

	@Override
	public void specialAttack() {
		int tempNum = (int) (Math.random() * 200);
		if (super.ghost || this.attackDurationCount >= Monster1.attackDuration) {
			this.specialAttacking = false;
			this.attackDurationCount = 0;
		} else if (tempNum == 0) {
			this.specialAttacking = true;
			this.attackDurationCount++;
		} else {
			this.attackDurationCount++;
		}
		if (this.specialAttacking) {
			Rectangle2D.Double flameBox = new Rectangle2D.Double();
			switch (this.getDirection()) {
			case 'u':
				this.xSpecialStart = this.getX();
				this.ySpecialStart = this.getY() - (this.specialLength);
				this.vertical = true;
				break;
			case 'd':
				this.xSpecialStart = this.getX();
				this.ySpecialStart = this.getY() + 32;
				this.vertical = true;
				break;
			case 'l':
				this.xSpecialStart = this.getX() - (this.specialLength);
				this.ySpecialStart = this.getY();
				this.vertical = false;
				break;
			case 'r':
				this.xSpecialStart = this.getX() + 32;
				this.ySpecialStart = this.getY();
				this.vertical = false;
				break;
			default:
				break;
			}
			if (this.vertical) {
				flameBox = new Rectangle2D.Double(this.xSpecialStart, this.ySpecialStart, this.specialWidth,
						this.specialLength);
			} else {
				flameBox = new Rectangle2D.Double(this.xSpecialStart, this.ySpecialStart, this.specialLength,
						this.specialWidth);
			}
			if (this.main.getHero().getHitBox().intersects(flameBox)) {
				this.main.getHero().die();
				this.specialAttacking = false;
			}
		}
	}

	@Override
	public void drawSpecialAttack(Graphics2D g2) {
		if (this.specialAttacking) {
			String direction = ("" + super.getDirection()).toUpperCase();
			String fileName = "flame" + direction + ".png";
			BufferedImage img;
			try {
				img = ImageIO.read(new File(fileName));
				if (this.vertical) {
					g2.drawImage(img, this.xSpecialStart, this.ySpecialStart, this.specialWidth, this.specialLength,
							null);
				} else {
					g2.drawImage(img, this.xSpecialStart, this.ySpecialStart, this.specialLength, this.specialWidth,
							null);
				}
			} catch (IOException e) {
				g2.setColor(Color.RED);
				Rectangle2D.Double monsterGraphics = new Rectangle2D.Double(super.getX(), super.getY(), 32, 32);
				g2.fill(monsterGraphics);
			}
		}
	}

	@Override
	public void alterMovement() {
	}

}
