import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Creates a rock that the player can not move through, and falls when there is
 * no dirt under it
 *
 * @author mastermk, wangj14, and zous. Created Feb 5, 2018.
 */

public class Rock extends Item {
	private Main main;
	private boolean supported;
	private long timeLastSupported;
	private int timeTillFall = 1000;

	/**
	 * Creates a rock at the specified point
	 *
	 * @param x
	 * @param y
	 * @param main
	 */
	public Rock(int x, int y, Main main) {
		super(x, y, true);
		this.main = main;
		this.supported = true;
		this.timeLastSupported = 0;
	}

	@Override
	public void drawOn(Graphics2D g2) {
		if (this.supported) {
			checkSupport();
		}
		fall();
		String fileName = "rock.png";
		BufferedImage img;
		try {
			img = ImageIO.read(new File(fileName));
			g2.drawImage(img, super.getxPosition(), super.getyPosition(), 32, 32, null);
		} catch (IOException e) {
			g2.setColor(Color.ORANGE);
			Rectangle2D.Double heroGraphics = new Rectangle2D.Double(super.getxPosition(), super.getyPosition(), 32,
					32);
			g2.fill(heroGraphics);
		}
	}

	/**
	 * checks if the rock is supported
	 *
	 */
	public void checkSupport() {
		if (checkAllBelowEmpty()) {
			this.supported = false;
			this.timeLastSupported = System.currentTimeMillis();
		}
	}

	/**
	 * Falls
	 *
	 */
	public void fall() {
		long currentTime = System.currentTimeMillis();
		if (!this.supported && currentTime - this.timeLastSupported >= this.timeTillFall) {
			if (checkAllBelowEmpty()) {
				setyPosition(getyPosition() + 8);
				checkIfKilledAnything();
			} else {
				disappear();
				this.main.rockFell();
			}
		}
	}

	/**
	 * Checks if the falling rock has killed anything
	 *
	 */
	private void checkIfKilledAnything() {
		Rectangle2D.Double rockBox = new Rectangle2D.Double(this.getxPosition(), this.getyPosition(), 32, 32);
		for (Monster m : this.main.getMonsters()) {
			if (m.getHitBox().intersects(rockBox)) {
				m.die();
			}
		}
		if (this.main.getHero().getHitBox().intersects(rockBox)) {
			this.main.getHero().die();
			disappear();
		}

	}

	/**
	 * Check if all blocks below are empty
	 *
	 * @return if they are all empty
	 */
	public boolean checkAllBelowEmpty() {
		for (int i = 0; i < 4; i++) {
			boolean check = (this.main.getCurrentMap().isEmpty(getxPosition() / 8 + i, getyPosition() / 8 - 6));
			if (check == false) {
				return false;
			}
		}
		return true;
	}

}
