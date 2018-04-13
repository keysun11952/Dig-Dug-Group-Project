import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * The main class for this arcade game.
 *
 * @author mastermk, wangj14, and zous.
 */
public class Main {
	private Hero hero;
	private Map currentMap;
	private int score;
	private JFrame frame;
	private int currentLevel;
	private DrawComponent draw;
	private int mapNum = 3; // number of maps we current have in use
	private ArrayList<Monster> monsters;
	private ArrayList<Item> items;
	private Monster toDie;
	private JPanel scoreBoard;
	private JLabel showScore;
	private int digIncrease = 1;
	private int monsterKillIncrease = 1000;
	private int rocksDropped;
	private Fruit itemToAdd;
	private Timer timer;
	private boolean paused;
	/**
	 * Delay between the screen updating
	 */
	public static final int DELAY = 75;

	/**
	 * The size that the game initially appears
	 */
	public static final Dimension GAME_SIZE = new Dimension(800, 800);

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {

		Main main = new Main();
	}

	/**
	 * Constructor that allows for non-static methods
	 *
	 */
	public Main() {

		playMusic();

		this.score -= 16;
		this.rocksDropped = 0;
		this.itemToAdd = null;
		this.monsters = new ArrayList<Monster>();
		this.toDie = null;
		this.items = new ArrayList<Item>();

		this.draw = new DrawComponent(this);
		this.frame = new JFrame();

		this.frame.setSize(GAME_SIZE);
		this.frame.setResizable(false);

		this.frame.setTitle("Dig Dug (Press 'P' to pause. Press 'H' for help.)");

		this.currentLevel = 1;
		handleCreateMap("level1.txt");

		this.frame.add(this.draw, BorderLayout.CENTER);

		this.hero = new Hero(this.frame, this);

		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setVisible(true);

		this.frame.addKeyListener(new keyboardListener(this));
		this.monsters = this.currentMap.getMonsters();

		GameAdvanceListener advanceListener = new GameAdvanceListener(this.draw, this);

		this.timer = new Timer(DELAY, advanceListener);
		this.timer.start();
		this.paused = false;
		this.scoreBoard = new JPanel();
		this.scoreBoard.setBackground(this.currentMap.areaColor(' '));
		this.showScore = new JLabel("Score: " + this.score);
		Font scoreFont = new Font("TimesRoman ", Font.BOLD, 24);
		this.showScore.setFont(scoreFont);
		this.scoreBoard.add(this.showScore);
		this.frame.add(this.scoreBoard, BorderLayout.NORTH);
	}

	/**
	 * Returns the value of the field called 'draw'.
	 * 
	 * @return Returns the draw.
	 */
	public DrawComponent getDraw() {
		return this.draw;
	}

	/**
	 * Returns the value of the field called 'gameSize'.
	 * 
	 * @return Returns the gameSize.
	 */
	public static Dimension getGameSize() {
		return GAME_SIZE;
	}

	/**
	 * Returns the value of the field called 'hero'.
	 * 
	 * @return Returns the hero.
	 */
	public Hero getHero() {
		return this.hero;
	}

	/**
	 * Adds new rock
	 * 
	 * @param rock
	 *
	 */
	public void addRock(Rock rock) {
		this.items.add(rock);
	}

	/**
	 * returns all rocks in the map
	 *
	 * @return rocks
	 */
	public ArrayList<Rock> getRocks() {
		ArrayList<Rock> rocks = new ArrayList<Rock>();
		for (Item i : this.items) {
			if (i.getClass().getSimpleName().equals("Rock")) {
				rocks.add((Rock) i);
			}
		}
		return rocks;
	}

	/**
	 * Returns a list of all fruits
	 *
	 * @return array list of the fruits
	 */
	public ArrayList<Fruit> getFruits() {
		ArrayList<Fruit> fruits = new ArrayList<Fruit>();
		for (Item i : this.items) {
			if (i.getClass().getSimpleName().equals("Fruit")) {
				fruits.add((Fruit) i);
			}
		}
		return fruits;
	}

	/**
	 * Returns the value of the field called 'currentMap'.
	 * 
	 * @return Returns the currentMap.
	 */
	public Map getCurrentMap() {
		return this.currentMap;
	}

	/**
	 * Returns the value of the field called 'score'.
	 * 
	 * @return Returns the score.
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * Sets the field called 'score' to the given value.
	 * 
	 * @param score
	 *            The score to set.
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * Returns the value of the field called 'frame'.
	 * 
	 * @return Returns the frame.
	 */
	public JFrame getFrame() {
		return this.frame;
	}

	/**
	 * Returns the current Level
	 *
	 * @return current Level
	 */
	public int getLevel() {
		return this.currentLevel;
	}

	/**
	 * Creates a new map from the file
	 * 
	 * @param filename
	 *
	 */
	public void handleCreateMap(String filename) {
		String temp = filename.replace("level", "");
		temp = temp.replace(".txt", "");
		this.currentLevel = Integer.parseInt(temp);
		this.currentMap = new Map(filename, this);
		this.monsters = this.currentMap.getMonsters();
	}

	/**
	 * Updates the map as the player moves
	 *
	 */
	public void handleUpdateMap() {
		int x = this.hero.getBlockX();
		int y = this.hero.getBlockY();

		removePlayerSpace(x, y);
		modifyItems();
		this.hero.eatFruit();

		this.hero.attack();

		if (this.monsters.size() == 0) {
			handleChangeLevel(1);
		}
		this.showScore.setText("Score: " + this.score);
		this.frame.repaint();
	}

	/**
	 * Removes the area the hero is in
	 *
	 * @param x
	 * @param y
	 * @return if the block is within the player
	 */
	public boolean checkPlayerSpace(int x, int y) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (x == this.hero.getBlockX() + i && y == this.hero.getBlockY() + j) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Removes the area the hero is in
	 *
	 * @param x
	 * @param y
	 */
	public void removePlayerSpace(int x, int y) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (!this.currentMap.getMap()[x + i][y + j].equals(this.currentMap.areaColor(' '))) {
					this.score += this.digIncrease;
				}
				this.currentMap.setBlock(x + i, y + j, ' ');
			}
		}
	}

	/**
	 * Changes levels when the player chooses to switch mid play
	 * 
	 * @param num
	 *
	 */
	public void handleChangeLevel(int num) {
		this.items = new ArrayList<Item>();
		if (this.currentLevel <= 1 && num < 1) {
			handleCreateMap("level1.txt");
			System.out.println("No lower levels are available");
			this.frame.repaint();
		} else if (this.currentLevel + num > this.mapNum) {
			handleCreateMap("level" + this.mapNum + ".txt");
			System.out.println("No higher levels are available");
			this.frame.repaint();
		} else if (this.currentLevel >= 1 || num > 0) {
			handleCreateMap("level" + (this.currentLevel + num) + ".txt");
			this.frame.repaint();
		}
		this.hero.setX(Main.GAME_SIZE.width / 2);
		this.hero.setY(Main.GAME_SIZE.height / 2 - 40);
		this.score -= 16;
	}

	/**
	 * Returns a list of monsters
	 *
	 * @return monsters
	 */
	public ArrayList<Monster> getMonsters() {
		return this.monsters;
	}

	/**
	 * Record which monsters need to die
	 *
	 * @param m
	 */
	public void addMonsterToDie(Monster m) {
		this.toDie = m;
	}

	/**
	 * Kills the monster scheduled to die
	 *
	 */
	public void killMonsterToDie() {
		if (this.toDie != null) {
			this.score += this.monsterKillIncrease;
		}
		this.monsters.remove(this.toDie);
		this.toDie = null;
	}

	/**
	 * Returns a list of ALL items
	 *
	 * @return a list of items
	 */
	public ArrayList<Item> getItems() {
		return this.items;
	}

	/**
	 * Removes items
	 *
	 */
	public void modifyItems() {
		ArrayList<Item> toRemove = new ArrayList<Item>();
		for (Item item : this.getItems()) {
			if (!item.isVisible()) {
				toRemove.add(item);
			}
		}
		for (Item item : toRemove) {
			if (!item.isVisible()) {
				this.items.remove(item);
			}
		}
		if (this.itemToAdd != null) {
			this.items.add(this.itemToAdd);
			this.itemToAdd = null;
		}

	}

	/**
	 * Method that reacts to each time a rock falls
	 *
	 */
	public void rockFell() {
		this.rocksDropped++;
		if (this.rocksDropped >= 2) {
			this.rocksDropped = 0;
			this.itemToAdd = new Fruit(this.frame.getWidth() / 2, this.frame.getHeight() / 2 - 40);
		}
	}

	/**
	 * Pauses and unpauses the game
	 *
	 */
	public void triggerPause() {
		if (this.paused) {
			this.timer.start();
		} else {
			this.timer.stop();
		}
		this.paused = !this.paused;
	}

	/**
	 * Returns the value of the field called 'paused'.
	 * 
	 * @return Returns the paused.
	 */
	public boolean isPaused() {
		return this.paused;
	}

	/**
	 * Plays the music
	 * 
	 * Music composed by Eric Liobis for the game Red Shift Blue Shift for the
	 * Greatest floor 2018.
	 *
	 */
	@SuppressWarnings({ "resource" })
	public void playMusic() {
		try {
			AudioInputStream audiostream = AudioSystem.getAudioInputStream(new File("Music.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audiostream);
			clip.start();
		} catch (Exception e) {
			System.out.println("Audio File Not Found");
		}
	}
}
