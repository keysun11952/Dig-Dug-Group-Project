import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Updates the board every 10 milliseconds
 *
 * This code was based off part of the MoreEventBasedProgramming assignment from
 * class
 * 
 * @author Instructors of CSSE 220 edited by: mastermk, wangj14, and zous
 *         Created Feb 6, 2018.
 */
public class GameAdvanceListener implements ActionListener {

	private DrawComponent gameComponent;
	private Main main;

	/**
	 * Creates the new game listener
	 *
	 * @param draw
	 * @param main
	 */
	public GameAdvanceListener(DrawComponent draw, Main main) {
		this.gameComponent = draw;
		this.main = main;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		advanceOneTick();
	}

	/**
	 * Advances the game
	 *
	 */
	public void advanceOneTick() {
		for (Monster m : this.main.getMonsters()) {
			m.updateLocation();
		}

		this.main.killMonsterToDie();
		// update screen
		this.gameComponent.draw();
		this.main.handleUpdateMap();
	}
}
