import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

/**
 * Creates the game screen based on a textfile
 *
 * @author mastermk, wangj14, and zous. Created Jan 26, 2018.
 */
public class Map {
	private int sizeX, sizeY;
	private Color[][] map;
	private JFrame frame;
	private static int pixels = 8;
	private Color skyColor;
	private ArrayList<Monster> monsters;
	private Main main;

	/**
	 * Generates the Map
	 *
	 * @param fileName
	 * @param main
	 */
	public Map(String fileName, Main main) {
		this.main = main;
		this.frame = main.getFrame();
		this.skyColor = new Color(126, 207, 221);
		read(fileName);
	}

	/**
	 * Read in from file
	 *
	 * @param fileName
	 */
	public void read(String fileName) {
		FileReader file;
		try {
			file = new FileReader(fileName);
			Scanner readIn = new Scanner(file);
			this.sizeX = Integer.parseInt(readIn.nextLine());
			this.sizeY = Integer.parseInt(readIn.nextLine());
			this.map = new Color[this.sizeX][this.sizeY];

			this.monsters = new ArrayList<Monster>();
			for (int y = 0; y < this.sizeY; y++) {
				char[] currentLine = readIn.nextLine().toCharArray();
				for (int x = 0; x < this.sizeX; x++) {
					if (currentLine[x] == '1') {
						this.monsters.add(new Monster1(x * 8, 80 + y * 8, this.frame, this, this.main));
						this.map[x][y] = this.skyColor;
					} else if (currentLine[x] == '2') {
						this.monsters.add(new Monster2(x * 8, 80 + y * 8, this.frame, this, this.main));
						this.map[x][y] = this.skyColor;
					} else if (currentLine[x] == 'K') {
						this.main.addRock(new Rock(x * 8, 80 + y * 8, this.main));
						this.map[x][y] = areaColor('D');
					} else {
						this.map[x][y] = areaColor(currentLine[x]);
					}
				}
			}
			readIn.close();
			file.close();
		} catch (FileNotFoundException exception) {
			System.out.println(fileName + " is missing");
		} catch (IOException exception) {
			System.out.println("file could not properly close");
		}
	}

	/**
	 * Returns the value of the field called 'monsters'.
	 * 
	 * @return Returns the monsters.
	 */
	public ArrayList<Monster> getMonsters() {
		return this.monsters;
	}

	/**
	 * Draws the sky
	 *
	 * @param g2
	 */
	public void drawSky(Graphics2D g2) {
		int skyHeight = 8 * Map.pixels;
		g2.setColor(this.skyColor);
		g2.fillRect(0, 0, this.frame.getWidth(), skyHeight);
		g2.translate(0, skyHeight);
	}

	/**
	 * draw the map on the screen
	 * 
	 * @param g2
	 *
	 */
	public void drawMap(Graphics2D g2) {
		drawSky(g2);
		for (int y = 0; y < this.map[0].length; y++) {
			for (int x = 0; x < this.map.length; x++) {
				g2.setColor(this.map[x][y]);
				Rectangle2D.Double currentSquare = new Rectangle2D.Double(0, 0, Map.pixels, Map.pixels);
				g2.fill(currentSquare);
				g2.translate(Map.pixels, 0);
			}
			g2.translate(-1 * this.frame.getWidth(), Map.pixels);
		}
		g2.translate(0, -1 * (this.frame.getHeight() - 3 * Map.pixels));
	}

	/**
	 * Returns the color that corresponds to each letter
	 *
	 * @param c
	 * @return Color of the square the character represents
	 */
	public Color areaColor(char c) {
		int random = (int) (Math.random() * 3);
		switch (c) {
		case 'D': // semi-random dirt
			switch (random) {
			case 0:
				return new Color(61, 43, 6);

			case 1:
				return new Color(73, 59, 36);

			case 2:
				return new Color(58, 43, 19);
			default:
				break;
			}
			break;

		case 'S': // semi-random sand
			switch (random) {
			case 0:
				return new Color(168, 145, 33);

			case 1:
				return new Color(163, 138, 47);

			case 2:
				return new Color(163, 118, 47);
			default:
				break;
			}
			break;

		case ' ': // empty space
			return this.skyColor;

		case 'G': // semi-random grass
			switch (random) {
			case 0:
				return new Color(6, 61, 10);

			case 1:
				return new Color(35, 91, 39);

			case 2:
				return new Color(40, 66, 39);
			default:
				break;
			}
			break;

		case 'R': // semi-random gravel
			switch (random) {
			case 0:
				return new Color(120, 125, 132);

			case 1:
				return new Color(182, 186, 183);

			case 2:
				return new Color(57, 58, 57);
			default:
				break;
			}
			break;
		default:
			break;
		}

		// should only be black squares if we made a mistake
		return new Color(0, 0, 0);

	}

	/**
	 * Prints out the map as a test to insure it was read in correct
	 *
	 */
	public void printMap() {
		for (int y = 0; y < this.sizeY; y++) {
			for (int x = 0; x < this.sizeX; x++) {
				System.out.print(this.map[x][y]);
			}
			System.out.println();
		}
	}

	/**
	 * Returns how many pixels per block
	 *
	 * @return pixels per one block
	 */
	public int getPixel() {
		return Map.pixels;
	}

	/**
	 * Set the block color
	 *
	 * @param x
	 * @param y
	 * @param c
	 */
	public void setBlock(int x, int y, char c) {
		this.map[x][y] = areaColor(c);

	}

	/**
	 * Returns the value of the field called 'map'.
	 * 
	 * @return Returns the map.
	 */
	public Color[][] getMap() {
		return this.map;
	}

	/**
	 * Returns true if the block is empty
	 *
	 * @param x
	 * @param y
	 * @return if the block is empty
	 */
	public boolean isEmpty(int x, int y) {
		return this.map[x][y].equals(this.skyColor);
	}

	/**
	 * check if 4 by 4 block is empty
	 * 
	 * takes in blocks
	 * 
	 * @param x
	 * @param y
	 * @return if all squares are empty
	 */
	public boolean fourByFourIsEmpty(int x, int y) {
		for (int i = 0; i < 4; i++) {
			for (int k = 0; k < 4; k++) {
				if (!isEmpty(x + k, y + i)) {
					return false;
				}
			}
		}
		return true;
	}

}
