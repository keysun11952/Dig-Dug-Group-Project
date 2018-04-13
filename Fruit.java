import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Build fruit objects in game.
 *
 * @author mastermk, wangj14, and zous. Created Jan 26, 2018.
 */

public class Fruit extends Item {
	private int points = 1000;

	/**
	 * Constructs a fruit object
	 *
	 * @param x
	 * @param y
	 * @param points
	 */
	public Fruit(int x, int y) {
		super(x, y, false);
		appear();
	}

	/**
	 * returns the points
	 *
	 * @return points for eating
	 */
	public int getPoints() {
		return this.points;
	}

	@Override
	public void drawOn(Graphics2D g2) {
		String fileName = "fruit.png";
		BufferedImage img;
		try {
			img = ImageIO.read(new File(fileName));
			g2.drawImage(img, super.getxPosition(), super.getyPosition(), 32, 32, null);
		} catch (IOException e) {
			g2.setColor(Color.PINK);
			Rectangle2D.Double heroGraphics = new Rectangle2D.Double(super.getxPosition(), super.getyPosition(), 32,
					32);
			g2.fill(heroGraphics);
		}

	}
}
