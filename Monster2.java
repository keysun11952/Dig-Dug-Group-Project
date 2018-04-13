import java.awt.Graphics2D;

import javax.swing.JFrame;

/**
 * Creates the second type of monster
 *
 * @author mastermk, wangj14, and zous. Created Feb 7, 2018.
 */
public class Monster2 extends Monster {

	/**
	 * Creates the brick style monster
	 *
	 * @param x
	 * @param y
	 * @param frame
	 * @param map
	 * @param main
	 */
	public Monster2(int x, int y, JFrame frame, Map map, Main main) {
		super(x, y, frame, map, main);
	}

	@Override
	public void specialAttack() {
	}

	@Override
	public void drawSpecialAttack(Graphics2D g2) {
	}

	@Override
	public void alterMovement() {
		// randomly teleports to center
		int numTeleportCheck = (int) (Math.random() * 100);
		if (numTeleportCheck == 0) {
			setX(400);
			setY(360);
		}
	}

}
