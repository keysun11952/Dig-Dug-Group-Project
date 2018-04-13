import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JOptionPane;

/**
 * Listen to keyboard input
 *
 * @author mastermk, wangj14, and zous. Created Feb 1, 2018.
 */
public class keyboardListener implements KeyListener {

	private Hero hero;
	private Main main;

	private DrawComponent draw;

	/**
	 * Constructs the key listener
	 *
	 * @param main
	 */
	public keyboardListener(Main main) {
		this.main = main;
		this.draw = this.main.getDraw();
		this.hero = main.getHero();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int heroDx = 0;
		int heroDy = 0;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			heroDx = -1;
			break;
		case KeyEvent.VK_RIGHT:
			heroDx = 1;
			break;
		case KeyEvent.VK_UP:
			heroDy = 1;
			break;
		case KeyEvent.VK_DOWN:
			heroDy = -1;
			break;
		case KeyEvent.VK_U:
			this.main.handleChangeLevel(1);
			break;
		case KeyEvent.VK_D:
			this.main.handleChangeLevel(-1);
			break;
		case KeyEvent.VK_SPACE:
			this.hero.updateAttack();
			break;
		case KeyEvent.VK_ENTER:
			System.out.println("Select");
			break;
		case KeyEvent.VK_P:
			this.main.triggerPause();
			if (this.main.isPaused()) {
				JOptionPane.showMessageDialog(this.main.getFrame(),
						"Game Paused" + "\nPress 'ENTER' and 'P' to continue");
			}
			break;
		case KeyEvent.VK_H:
			this.main.triggerPause();
			if (this.main.isPaused()) {
				JOptionPane.showMessageDialog(this.main.getFrame(),
						"Game Help" + "\n'↑' : Move Upward" + "\n'↓' : Move Downward" + "\n'←' : Move Left"
								+ "\n'→' : Move Right" + "\n'U' : Game Level Up" + "\n'D' : Game Level Down"
								+ "\n'SPACE' : Attack" + "\nPress 'ENTER' and 'P' to continue");
			}
			break;
		default:
			break;
		}
		if (!this.main.isPaused()) {
			if (this.hero.getHitting()) {
				this.hero.setTarget(null);
				this.hero.setHitting(false);
				this.hero.setHitBlock(new Rectangle2D.Double(0, 0, 0, 0));
				this.hero.setLength(0);
				this.hero.setAttacking(false);
				this.hero.setSpeed(8);
			} else {
				this.hero.move(heroDx * this.hero.getSpeed(), heroDy * this.hero.getSpeed());
			}
		}
		this.draw.draw();
		this.main.handleUpdateMap();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Not Used
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Not Used

	}

}
