import java.awt.*;

import javax.swing.JComponent;

/**
 * Draw all elements we need for the game
 *
 * @author mastermk, wangj14, and zous. Created Feb 1, 2018.
 */
public class DrawComponent extends JComponent {
	private Main main;

	/**
	 * Constructor for the drawComponent
	 *
	 * @param main
	 */
	public DrawComponent(Main main) {
		this.main = main;
	}

	/**
	 * Redraws everything
	 *
	 */
	public void draw() {
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		// draw map
		this.main.getCurrentMap().drawMap(g2);
		// draw items
		for (Item item : this.main.getItems()) {
			item.drawOn(g2);
		}

		// draw hero
		this.main.getHero().drawOn(g2);
		// draw monsters
		for (Monster m : this.main.getMonsters()) {
			m.drawOn(g2);
		}

	}
}
