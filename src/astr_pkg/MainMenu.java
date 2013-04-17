package astr_pkg;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Scanner;

//This is the class that handles all of our menus for the game
public class MainMenu extends JFrame {

	private static final ImageIcon background = new ImageIcon(
			"FX/graphics/background_main_menu2.jpg");
	private static final ImageIcon gameOverPic = new ImageIcon(
			"FX/graphics/game_over.jpg");
	// Controls which option in Main Menu should be highlighted
	private static int countMain = 0;
	// Controls which option in the Play Game menu should be highlighted
	private static int countPlay = 0;
	// Controls which option in the Options menu should be highlighted
	private static int countOptions = 0;
	// Controls which option in the game over menu should be highlighted
	private static int countGameOver = 0;
	// Controls which option in high score menu should be highlighted
	private static int countHSDifficulty = 0;

	private static boolean musicVolume = true; // Controls if background music
												// is on or off
	private static boolean sfxVolume = true; // Controls if sound effects are on
												// or off
	private static boolean wasd = true; // Controls if the user has selected
										// WASD or Arrow Keys controls

	private static int difficultyLevel = 1;
	private static Font titleFont; // The custom font for the title of the game
	private static Clip menu_select; // Sound effect for when options are
										// scrolled in the Main Menu
	private static Clip menu_validate; // Menu sound effects
	private static Clip background_music; // background music
	private static Clip menu_music; // menu music
	private static MainMenu menu = new MainMenu();
	private static AsteroidsGame game;
	private static boolean multiplayer = false;

	// These variables are used for entering high scores
	static int letter;
	static int position;
	private char hsFirstLetter, hsSecondLetter, hsThirdLetter;

	public MainMenu() {

		if (!Constants.LINUX) {
			initializeSounds();
		}

		add(Constants.MAIN_MENU_PANEL); // Add panel, which has main menu
										// options, to the frame
		Constants.MAIN_MENU_PANEL.setFocusable(true);// Make this panel able to
														// detect key presses
		Constants.MAIN_MENU_PANEL.requestFocusInWindow(); // from the keyboard

		if (musicVolume && !Constants.LINUX) {
			menu_music.loop(Clip.LOOP_CONTINUOUSLY);
		}

		Constants.MAIN_MENU_PANEL.addKeyListener(new KeyAdapter() { // Keyboard
																	// event
																	// handler
			public void keyPressed(KeyEvent e) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_DOWN: // If down arrow key pressed,
						countMain++; // increment count --> move down the
										// list of options
						if (sfxVolume && !Constants.LINUX) {
							if (menu_select.isRunning()) { // If the clip is
															// still running
								menu_select.stop(); // Stop playback
							}
							menu_select.setFramePosition(0); // Rewind audio
																// to
																// beginning
							menu_select.start(); // Play audio
						}
						break;
					case KeyEvent.VK_UP: // If upward arrow key pressed,
						countMain--; // decrement count --> move up the list
										// of options
						if (countMain < 0) { // Keep value positive as to
												// keep proper track of the
							countMain = 3; // keyboard pointer location.
						}
						if (sfxVolume && !Constants.LINUX) {
							if (menu_select.isRunning()) {
								menu_select.stop();
							}
							menu_select.setFramePosition(0);
							menu_select.start();
						}
						break;
					case KeyEvent.VK_ENTER: // If the Enter key is pressed,
											// depending on the option
											// selected
						if (sfxVolume && !Constants.LINUX) {
							if (menu_validate.isRunning()) {
								menu_validate.stop();
							}
							menu_validate.setFramePosition(0);
							menu_validate.start();
						}
						if (countMain % 4 == 0) { // If first option
													// selected, "Play Game"
							// Show the Play Game sub-menu
							menu.remove(Constants.MAIN_MENU_PANEL); // Remove
																	// the
																	// Main
																	// Menu
							menu.add(Constants.PLAY_GAME_PANEL); // Show the
																	// Play
																	// Game
																	// sub-menu
							menu.getRootPane().revalidate(); // refresh
																// frame
							Constants.PLAY_GAME_PANEL.setFocusable(true);// Make
																			// this
																			// panel
																			// able
																			// to
																			// detect
																			// key
																			// presses
							Constants.PLAY_GAME_PANEL
									.requestFocusInWindow(); // from the
																// keyboard
						} else if (countMain % 4 == 1) { // If 2nd option
															// selected,
															// "Options"
							// Show the Options sub-menu
							menu.remove(Constants.MAIN_MENU_PANEL); // Remove
																	// Main
																	// Menu
							menu.add(Constants.OPTIONS_PANEL); // Show
																// Options
																// sub-menu
							menu.getRootPane().revalidate(); // refresh
																// frame
							Constants.OPTIONS_PANEL.setFocusable(true); // Make
																		// this
																		// panel
																		// able
																		// to
																		// detect
																		// key
																		// presses
							Constants.OPTIONS_PANEL.requestFocusInWindow(); // from
																			// the
																			// keyboard
						} else if (countMain % 4 == 2) { // If 3rd option
															// selected,
															// "High Scores"
							menu.remove(Constants.MAIN_MENU_PANEL); // Remove
																	// Main
																	// Menu
							menu.add(Constants.HIGH_SCORES_PANEL); // Show
																	// High
																	// Scores
																	// sub-menu
								menu.getRootPane().revalidate(); // refresh
																// frame
							
							// Make this panel able to detect key presses
							Constants.HIGH_SCORES_PANEL.setFocusable(true);
							Constants.HIGH_SCORES_PANEL
									.requestFocusInWindow(); // from the
																// keyboard
						} else { // If last option selected, "Exit"
							System.exit(0); // Terminate program
						}
						break;
					}
					repaint(); // repaint canvas every time a key is pressed
				}
			});
		// Code to check for Keyboard events in the Play Game Sub-menu
		Constants.PLAY_GAME_PANEL.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_DOWN: // If down arrow key pressed
					countPlay++; // move down list of options
					if (sfxVolume && !Constants.LINUX) {
						if (menu_select.isRunning()) {
							menu_select.stop();
						}
						menu_select.setFramePosition(0);
						menu_select.start();
					}
					break;
				case KeyEvent.VK_UP: // If up arrow key pressed
					countPlay--; // move up list of options
					if (countPlay < 0) { // Keep value positive as to keep
											// proper track of the
						countPlay = 3; // keyboard pointer location.
					}
					if (sfxVolume && !Constants.LINUX) {
						if (menu_select.isRunning()) {
							menu_select.stop();
						}
						menu_select.setFramePosition(0);
						menu_select.start();
					}
					break;
				case KeyEvent.VK_ENTER:// If enter key pressed
					if (sfxVolume && !Constants.LINUX) {
						if (menu_validate.isRunning()) {
							menu_validate.stop();
						}
						menu_validate.setFramePosition(0);
						menu_validate.start();
					}
					switch (countPlay % 4) {
					case 0:
						difficultyLevel = (difficultyLevel + 1) % 4;
						if (difficultyLevel == 0) {
							difficultyLevel = 1;
						}
						break;
					case 1: // If first option selected "Single Player"
						// Start a Single Player game
						setMultiplayer(false);
						menu.remove(Constants.PLAY_GAME_PANEL);
						game = new AsteroidsGame();
						game.gameReset();
						menu.add(game);
						menu.getRootPane().revalidate();
						if (musicVolume && !Constants.LINUX) {
							menu_music.stop();
							background_music.loop(Clip.LOOP_CONTINUOUSLY);
						}
						game.setFocusable(true);
						game.requestFocusInWindow();
						game.init();
						break;
					case 2: // If second option selected "Multiplayer"
						// Start a Multiplayer game
						setMultiplayer(true);
						menu.remove(Constants.PLAY_GAME_PANEL);
						game = new AsteroidsGame();
						game.gameReset();
						menu.add(game);
						menu.getRootPane().revalidate();
						if (musicVolume && !Constants.LINUX) {
							menu_music.stop();
							background_music.loop(Clip.LOOP_CONTINUOUSLY);
						}
						game.setFocusable(true);
						game.requestFocusInWindow();
						game.init();
						break;
					case 3: // If third option selected "Back to Main Menu"
						menu.remove(Constants.PLAY_GAME_PANEL); // remove the
																// Play Game
																// sub-menu from
																// frame
						menu.add(Constants.MAIN_MENU_PANEL); // Add the Main
																// Menu to frame
						menu.getRootPane().revalidate(); // refresh frame
						Constants.MAIN_MENU_PANEL.setFocusable(true); // Make
																		// Main
																		// Menu
																		// able
																		// to
																		// detect
																		// key
																		// presses
						Constants.MAIN_MENU_PANEL.requestFocusInWindow(); // from
																			// the
																			// keyboard
						countPlay = 0; // reset pointer position in Play Game
										// sub-menu to top option
						break;
					}
					break;
				}
				repaint();// repaint canvas every time a key is pressed
			}
		});

		// Code to check for keyboard events in the Options sub-menu
		Constants.OPTIONS_PANEL.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_DOWN: // If down arrow key pressed
					countOptions++; // move down list of options
					if (sfxVolume && !Constants.LINUX) {
						if (menu_select.isRunning()) {
							menu_select.stop();
						}
						menu_select.setFramePosition(0);
						menu_select.start();
					}
					break;
				case KeyEvent.VK_UP: // If up arrow key pressed
					countOptions--; // Move up list of options
					if (countOptions < 0) { // Keep value positive as to keep
											// proper track of the
						countOptions = 3; // keyboard pointer location
					}
					if (sfxVolume && !Constants.LINUX) {
						if (menu_select.isRunning()) {
							menu_select.stop();
						}
						menu_select.setFramePosition(0);
						menu_select.start();
					}
					break;
				case KeyEvent.VK_ENTER: // If Enter key pressed
					if (sfxVolume && !Constants.LINUX) {
						if (menu_validate.isRunning()) {
							menu_validate.stop();
						}
						menu_validate.setFramePosition(0);
						menu_validate.start();
					}
					switch (countOptions % 4) {
					case 0: // If first option selected
						// Toggle Sound effects volume ON or OFF
						// If sound effects option is on and user presses enter
						if (isSfxOn()) {
							sfxVolume = false; // turn sound effects off
						} else {
							sfxVolume = true; // else turn sound effects on
						}
						break;
					case 1: // If second option selected
						// Toggle Background Music ON or Off
						// If music option is ON and user presses Enter
						if (musicVolume) {
							musicVolume = false; // Turn Music off
							menu_music.stop();
						} else {
							musicVolume = true; // Else turn Music on
							if (!Constants.LINUX) {
								menu_music.loop(Clip.LOOP_CONTINUOUSLY);
							}
						}
						break;
					case 2: // If thrid option selected
						// Toggle Control schemes
						// If WASD option and user presses enter
						if (wasd) {
							wasd = false; // Scheme becomes arrow keys
						} else {
							wasd = true; // Else Scheme is WASD
						}
						break;
					case 3: // If third option selected "Back to Main Menu"
						menu.remove(Constants.OPTIONS_PANEL); // Remove Option
																// sub-menu from
																// frame
						menu.add(Constants.MAIN_MENU_PANEL); // Add/Show Main
																// Menu in the
																// frame
						menu.getRootPane().revalidate(); // Refresh Frame
						Constants.MAIN_MENU_PANEL.setFocusable(true); // Make
																		// Main
																		// Menu
																		// able
																		// to
																		// detect
																		// key
																		// presses
						Constants.MAIN_MENU_PANEL.requestFocusInWindow(); // from
																			// the
																			// keyboard
						countOptions = 0; // reset pointer position in Options
											// sub-menu to top option
						break;
					}
					break;
				}
				repaint(); // repaint canvas every time a key is pressed
			}
		});

		Constants.GAME_OVER_PANEL.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (game.getIsHighScore()) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_DOWN:
						letter--;
						if (letter < 0) {
							letter = 25;
						}
						break;
					case KeyEvent.VK_UP:
						letter++;
						if (letter > 25) {
							letter = 0;
						}
						break;
					case KeyEvent.VK_ENTER:
						if (position == 0) {
							hsFirstLetter = (char) ('A' + letter);
						} else if (position == 1) {
							hsSecondLetter = (char) ('A' + letter);
						} else if (position == 2) {
							hsThirdLetter = (char) ('A' + letter);
						}
						position++;
						letter = 0;
						if (position > 3) {
							File highScoreList = null;
							if (MainMenu.getDifficulty() == 1) {
								highScoreList = new File(
										"src/astr_pkg/hs_easy.csv");
							} else if (MainMenu.getDifficulty() == 2) {
								highScoreList = new File(
										"src/astr_pkg/hs_medium.csv");
							} else if (MainMenu.getDifficulty() == 3) {
								highScoreList = new File(
										"src/astr_pkg/hs_hard.csv");
							}

							try {
								Scanner file = new Scanner(highScoreList);
								File temp = new File("src/astr_pkg/temp.csv");
								PrintWriter fout = new PrintWriter(temp);
								String line = "error";
								String[] fields = null;
								String name, score, hsTime;
								boolean insert = false, done = false;
								while (true) {
									if (!insert) {
										if (!file.hasNext()) {
											break;
										}
										line = file.next();
										fields = line.split(",");
									}
									Integer scoreInt = Integer
											.parseInt(fields[1]);
									if (!done) {
										if (scoreInt > (Asteroid.getPointsP1() + Alien
												.getPointsP1())) {
											name = fields[0];
											score = fields[1];
											hsTime = fields[2];
											fout.println(name + "," + score
													+ "," + hsTime);
										} else {
											name = "" + hsFirstLetter
													+ hsSecondLetter
													+ hsThirdLetter;
											fout.println(name
													+ ","
													+ (Asteroid.getPointsP1() + Alien
															.getPointsP1())
													+ ","
													+ game.getGameLength());
											insert = true;
											done = true;
										}
									} else {
										name = fields[0];
										score = fields[1];
										hsTime = fields[2];
										fout.println(name + "," + score + ","
												+ hsTime);
										insert = false;
									}

								}
								if (!done) {
									name = "" + hsFirstLetter + hsSecondLetter
											+ hsThirdLetter;
									fout.println(name
											+ ","
											+ (Asteroid.getPointsP1() + Alien
													.getPointsP1()) + ","
											+ game.getGameLength());
								}
								fout.close();
								file.close();
								String fileName = highScoreList.getName();
								highScoreList.delete();
								temp.renameTo(new File("src/astr_pkg/"
										+ fileName));
							} catch (FileNotFoundException exception) {
								// TODO Auto-generated catch block
								exception.printStackTrace();
							}
							menu.remove(Constants.GAME_OVER_PANEL); // remove
																	// the Play
																	// Game
																	// sub-menu
																	// from
																	// frame
							menu.add(Constants.HIGH_SCORES_PANEL); // Add the
																	// Main Menu
																	// to frame
							menu.getRootPane().revalidate(); // refresh frame
							Constants.HIGH_SCORES_PANEL.setFocusable(true); // Make
																			// Main
																			// Menu
																			// able
																			// to
																			// detect
																			// key
																			// presses
							Constants.HIGH_SCORES_PANEL.requestFocusInWindow(); // from
																				// the
																				// keyboard
							position = 0; // reset pointer position in Play Game
											// sub-menu to top option
							letter = 0;
							break;
						}
					}

				} else {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_DOWN: // If down arrow key pressed
						countGameOver++; // move down list of options
						if (sfxVolume && !Constants.LINUX) {
							if (menu_select.isRunning()) {
								menu_select.stop();
							}
							menu_select.setFramePosition(0);
							menu_select.start();
						}
						break;
					case KeyEvent.VK_UP: // If up arrow key pressed
						countGameOver--; // move up list of options
						if (countGameOver < 0) { // Keep value positive as to
													// keep proper track of the
							countGameOver = 1; // keyboard pointer location.
						}
						if (sfxVolume && !Constants.LINUX) {
							if (menu_select.isRunning()) {
								menu_select.stop();
							}
							menu_select.setFramePosition(0);
							menu_select.start();
						}
						break;
					case KeyEvent.VK_ENTER:// If enter key pressed
						if (sfxVolume && !Constants.LINUX) {
							if (menu_validate.isRunning()) {
								menu_validate.stop();
							}
							menu_validate.setFramePosition(0);
							menu_validate.start();
						}
						switch (countGameOver % 2) {
						case 0:
							menu.remove(Constants.GAME_OVER_PANEL);
							game = new AsteroidsGame();
							game.gameReset();
							menu.add(game);
							menu.getRootPane().revalidate();
							if (musicVolume && !Constants.LINUX) {
								menu_music.stop();
								background_music.loop(Clip.LOOP_CONTINUOUSLY);
							}
							game.setFocusable(true);
							game.requestFocusInWindow();
							game.init();
							break;
						case 1: // If first option selected "Single Player"
							menu.remove(Constants.GAME_OVER_PANEL); // remove
																	// the Play
																	// Game
																	// sub-menu
																	// from
																	// frame
							menu.add(Constants.MAIN_MENU_PANEL); // Add the Main
																	// Menu to
																	// frame
							menu.getRootPane().revalidate(); // refresh frame
							Constants.MAIN_MENU_PANEL.setFocusable(true); // Make
																			// Main
																			// Menu
																			// able
																			// to
																			// detect
																			// key
																			// presses
							Constants.MAIN_MENU_PANEL.requestFocusInWindow(); // from
																				// the
																				// keyboard
							countGameOver = 0; // reset pointer position in Play
												// Game sub-menu to top option
							break;
						}
						break;
					}
				}
				repaint();// repaint canvas every time a key is pressed
			}
		});

		Constants.HIGH_SCORES_PANEL.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					countHSDifficulty--;
					if (countHSDifficulty < 0) {
						countHSDifficulty = 2;
					}
					break;
				case KeyEvent.VK_RIGHT:
					countHSDifficulty++;
					break;
				case KeyEvent.VK_ENTER:// If enter key pressed
					if (sfxVolume && !Constants.LINUX) {
						if (menu_validate.isRunning()) {
							menu_validate.stop();
						}
						menu_validate.setFramePosition(0);
						menu_validate.start();
					}
					menu.remove(Constants.HIGH_SCORES_PANEL); // remove the Play
																// Game sub-menu
																// from frame
					menu.add(Constants.MAIN_MENU_PANEL); // Add the Main Menu to
															// frame
					menu.getRootPane().revalidate(); // refresh frame
					Constants.MAIN_MENU_PANEL.setFocusable(true); // Make Main
																	// Menu able
																	// to detect
																	// key
																	// presses
					Constants.MAIN_MENU_PANEL.requestFocusInWindow(); // from
																		// the
																		// keyboard
					countHSDifficulty = 0;
					break;
				}

				repaint();// repaint canvas every time a key is pressed
			}
		});
	}

	/*
	 * When the game loop ends in AsteroidsGame class, display the Game Over
	 * screen
	 */
	public void gameOver() {
		menu.remove(game);
		menu.add(Constants.GAME_OVER_PANEL);
		menu.getRootPane().revalidate();
		Constants.GAME_OVER_PANEL.setFocusable(true); // Make Main Menu able to
														// detect key presses
		Constants.GAME_OVER_PANEL.requestFocusInWindow();
		repaint();
	}

	/*
	 * Code to initialize all the audio variables
	 */
	public static void initializeSounds() {
		/*
		 * Code for getting an audio file to be accessible for playback Clip
		 * Class is used for this.
		 */
		try {
			File menuSelection = new File("FX/audio/menu_select.wav");
			AudioInputStream audioIn = AudioSystem
					.getAudioInputStream(menuSelection);
			menu_select = AudioSystem.getClip();
			menu_select.open(audioIn);

			File menuValidation = new File("FX/audio/menu_validate.wav");
			AudioInputStream audioIn2 = AudioSystem
					.getAudioInputStream(menuValidation);
			menu_validate = AudioSystem.getClip();
			menu_validate.open(audioIn2);

			File backgroundMusic = new File("FX/audio/background_music.wav");
			AudioInputStream audioIn3 = AudioSystem
					.getAudioInputStream(backgroundMusic);
			background_music = AudioSystem.getClip();
			background_music.open(audioIn3);

			File menuMusic = new File("FX/audio/menu_music.wav");
			AudioInputStream audioIn4 = AudioSystem
					.getAudioInputStream(menuMusic);
			menu_music = AudioSystem.getClip();
			menu_music.open(audioIn4);

		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/*
	 * Return the MainMenu object
	 */
	public static MainMenu getMenu() {
		return menu;
	}

	/*
	 * Return the AsteroidsGame object
	 */
	public static AsteroidsGame getGame() {
		return game;
	}

	/*
	 * Return the difficulty the user selected
	 */
	public static int getDifficulty() {
		return difficultyLevel;
	}

	/*
	 * Return the image for the Game Over Screen
	 */
	public static ImageIcon getGameOverImage() {
		return gameOverPic;
	}

	// Return the background Image
	public static ImageIcon getBackgroundImage() {
		return background;
	}

	// Return the position of the pointer in the main menu
	public static int getMainMenuPointerPosition() {
		return countMain;
	}

	// Return the position of the pointer in the Play Game sub-menu
	public static int getPlayGamePointerPosition() {
		return countPlay;
	}

	// Return the position of the pointer in the Options sub-menu
	public static int getOptionsPointerPosition() {
		return countOptions;
	}

	// Return the position of the pointer in the Game Over screen
	public static int getGameOverPointerPosition() {
		return countGameOver;
	}

	// Return the position of the pointer in the High Score sub-menu
	public static int getHSDifficulty() {
		return countHSDifficulty;
	}

	// Return true if background music option is "ON"
	public static boolean isMusicOn() {
		return musicVolume;
	}

	// Return true if Sound Effects option is "ON"
	public static boolean isSfxOn() {
		return sfxVolume;
	}

	// Return true if control scheme is WASD
	public static boolean isControlWasd() {
		return wasd;
	}

	// return true if multiplayer game selected
	public static boolean isMultiplayer() {
		return multiplayer;
	}
	
	public static void setMultiplayer(boolean isMulti) {
		multiplayer = isMulti;
	}

	// Return the custom title Font
	public static Font getTitleFont() {
		return titleFont;
	}

	// Method that stops the background music
	public static void stopBackgroundMusic() {
		background_music.stop();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Create and register a custom font
		try {
			titleFont = Font.createFont(Font.TRUETYPE_FONT, new File(
					"FX/fonts/Autobahn.ttf"));
			titleFont = titleFont.deriveFont(Font.PLAIN, 70);
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			ge.registerFont(titleFont);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		menu.setBackground(Color.BLACK); // Set background color of frame(Black)
		menu.setForeground(Color.RED); // Set foreground color of Frame (RED)
		menu.setUndecorated(true); // Hide the toolbar at the top of windows
		menu.setSize(Constants.WIDTH, Constants.HEIGHT); // Set size to 800x600
															// (default)
		menu.setLocationRelativeTo(null); // Center frame
		menu.setVisible(true); // Make frame visible
	}

}

/*
 * This class is used to paint paint the Main Menu onto the screen The Main Menu
 * has the following selections: "Play Game", "Options", "High Scores", and
 * "Exit"
 */
class MainMenuPanel extends JPanel {

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D) g;
			// Make text smooth
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
		// Used to obtain measurements of the Font used in the Main Menu
		FontMetrics metrics = g.getFontMetrics(Constants.MENU_FONT);

		// X and Y positions of the Play Game option calculated
		int playGameX = getWidth() / 2 - metrics.stringWidth("Play Game") / 2; // Center
																				// text
																				// in
																				// panel
		int playGameY = 3 * getHeight() / 5;

		// X and Y positions of the Options selection calculated
		int optionsX = getWidth() / 2 - metrics.stringWidth("Options") / 2; // Center
																			// text
																			// in
																			// panel
		int optionsY = playGameY + metrics.getHeight() + 10; // Y position of
																// text right
																// beneath
																// option above

		// X and Y positions of the High Scores option calculated
		int highScoreX = getWidth() / 2 - metrics.stringWidth("High Scores")
				/ 2; // Center text in panel
		int highScoreY = optionsY + metrics.getHeight() + 10;// Y position of
																// text right
																// beneath
																// option above

		// X and Y positions of the Exit option calculated
		int exitX = getWidth() / 2 - metrics.stringWidth("Exit") / 2; // Center
																		// text
																		// in
																		// panel
		int exitY = highScoreY + metrics.getHeight() + 10;// Y position of text
															// right beneath
															// option above

		// Add the background Image to Panel
		g.drawImage(MainMenu.getBackgroundImage().getImage(), 0, 0, getWidth(),
				getHeight(), this);
		// If the custom title font was successfully accessed
		if (MainMenu.getTitleFont() != null) {
			g.setFont(MainMenu.getTitleFont());
			FontMetrics titleMetrics = g
					.getFontMetrics(MainMenu.getTitleFont());
			int titleLine1X = getWidth() / 2
					- titleMetrics.stringWidth("SUPER") / 2;
			int titleLine1Y = getHeight() / 4;

			int titleLine2X = getWidth() / 2
					- titleMetrics.stringWidth("SMASHTEROIDS") / 2;
			int titleLine2Y = titleLine1Y + titleMetrics.getHeight() + 10;
			if (g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D) g;

				g2d.setPaint(new GradientPaint(titleLine1X, titleLine1Y,
						Color.ORANGE, titleLine1X
								+ titleMetrics.stringWidth("SUPER"),
						titleLine1Y + titleMetrics.getHeight(), Color.RED));
				g2d.setFont(MainMenu.getTitleFont());
				g2d.drawString("SUPER", titleLine1X, titleLine1Y);
				g2d.setPaint(new GradientPaint(titleLine2X, titleLine2Y,
						Color.ORANGE, titleLine2X
								+ titleMetrics.stringWidth("SMASHTEROIDS"),
						titleLine2Y + titleMetrics.getHeight(), Color.RED));
				g2d.drawString("SMASHTEROIDS", titleLine2X, titleLine2Y);
			} else {
				g.setColor(new Color(255, 120, 0));
				g.drawString("SUPER SMASHTEROIDS", getWidth() / 2
						- titleMetrics.stringWidth("SUPER SMASHTEROIDS") / 2,
						getHeight() / 4);
			}
		} else {
			g.setFont(Constants.MENU_FONT);
			g.setColor(Color.ORANGE);
			g.drawString("SUPER SMASHTEROIDS",
					getWidth() / 2 - metrics.stringWidth("SUPER SMASHTEROIDS")
							/ 2, getHeight() / 4);
		}

		g.setFont(Constants.MENU_FONT);

		/*
		 * This set of code is conditions to check where the pointer position is
		 * in the Main Menu. The text is added accordingly with the option
		 * corresponding to the pointer position having a White text color and
		 * the other options remaining grayed out (Gray font color)
		 */
		switch (MainMenu.getMainMenuPointerPosition() % 4) {
		case 0: // Play Game option highlighted
			g.setColor(Color.WHITE);
			g.drawString("Play Game", playGameX, playGameY);
			g.setColor(Color.GRAY);
			g.drawString("Options", optionsX, optionsY);
			g.drawString("High Scores", highScoreX, highScoreY);
			g.drawString("Exit", exitX, exitY);
			break;
		case 1: // Options highlighted
			g.setColor(Color.GRAY);
			g.drawString("Play Game", playGameX, playGameY);
			g.setColor(Color.WHITE);
			g.drawString("Options", optionsX, optionsY);
			g.setColor(Color.GRAY);
			g.drawString("High Scores", highScoreX, highScoreY);
			g.drawString("Exit", exitX, exitY);
			break;
		case 2: // High Scores highlighted
			g.setColor(Color.GRAY);
			g.drawString("Play Game", playGameX, playGameY);
			g.drawString("Options", optionsX, optionsY);
			g.setColor(Color.WHITE);
			g.drawString("High Scores", highScoreX, highScoreY);
			g.setColor(Color.GRAY);
			g.drawString("Exit", exitX, exitY);
			break;
		case 3: // Exit highlighted
			g.setColor(Color.GRAY);
			g.drawString("Play Game", playGameX, playGameY);
			g.drawString("Options", optionsX, optionsY);
			g.drawString("High Scores", highScoreX, highScoreY);
			g.setColor(Color.WHITE);
			g.drawString("Exit", exitX, exitY);
			break;
		}

	}
}

/*
 * Very similar to the MainMenuPanel class. This class will paint/display the
 * Play Game sub-menu which has the "Difficulty", "Single Player",
 * "Multiplayer", and "Back to Main Menu" selections
 */
class PlayGamePanel extends JPanel {
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D) g;
			// Make text smooth
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}

		// Add the background Image to Panel
		g.drawImage(MainMenu.getBackgroundImage().getImage(), 0, 0, getWidth(),
				getHeight(), this);

		// Set the Font
		g.setFont(Constants.MENU_FONT);
		// Used to obtain measurements of the Font used in the Play Game
		// Sub-menu
		FontMetrics metrics = g.getFontMetrics(Constants.MENU_FONT);

		int difficultyX = 10;
		int difficultyY = getHeight() / 5;
		int easyX = getWidth() - metrics.stringWidth("EASY") - 20;
		int normalX = getWidth() - metrics.stringWidth("NORMAL") - 20;
		int hardX = getWidth() - metrics.stringWidth("HARD") - 20;
		// X and Y positions of the Single Player option calculated
		int singlePlayerX = getWidth() / 2
				- metrics.stringWidth("Single Player") / 2; // Center text
		int singlePlayerY = 2 * getHeight() / 5;
		// X and Y positions of the Multiplayer option calculated
		int multiplayerX = getWidth() / 2 - metrics.stringWidth("Multiplayer")
				/ 2; // Center text
		int multiplayerY = 3 * getHeight() / 5;
		// X and Y positions of the Back to Main Menu option calculated
		int mainMenuX = getWidth() / 2
				- metrics.stringWidth("Back to Main Menu") / 2; // Center text
		int mainMenuY = 4 * getHeight() / 5;

		/*
		 * This set of code is conditions to check where the pointer position is
		 * in the Main Menu. The text is added accordingly with the option
		 * corresponding to the pointer position having a White text color and
		 * the other options remaining grayed out (Gray font color)
		 */
		switch (MainMenu.getPlayGamePointerPosition() % 4) {
		case 0:
			g.setColor(Color.WHITE);
			g.drawString("Difficulty", difficultyX, difficultyY);
			if (MainMenu.getDifficulty() == 1) {
				g.drawString("EASY", easyX, difficultyY);
			} else if (MainMenu.getDifficulty() == 2) {
				g.drawString("NORMAL", normalX, difficultyY);
			} else if (MainMenu.getDifficulty() == 3) {
				g.drawString("HARD", hardX, difficultyY);
			}
			g.setColor(Color.GRAY);
			g.drawString("Single Player", singlePlayerX, singlePlayerY);
			g.drawString("Multiplayer", multiplayerX, multiplayerY);
			g.drawString("Back to Main Menu", mainMenuX, mainMenuY);
			break;
		case 1:
			g.setColor(Color.WHITE);
			g.drawString("Single Player", singlePlayerX, singlePlayerY);
			g.setColor(Color.GRAY);
			g.drawString("Difficulty", difficultyX, difficultyY);
			if (MainMenu.getDifficulty() == 1) {
				g.drawString("EASY", easyX, difficultyY);
			} else if (MainMenu.getDifficulty() == 2) {
				g.drawString("NORMAL", normalX, difficultyY);
			} else if (MainMenu.getDifficulty() == 3) {
				g.drawString("HARD", hardX, difficultyY);
			}
			g.drawString("Multiplayer", multiplayerX, multiplayerY);
			g.drawString("Back to Main Menu", mainMenuX, mainMenuY);
			break;
		case 2:
			g.setColor(Color.WHITE);
			g.drawString("Multiplayer", multiplayerX, multiplayerY);
			g.setColor(Color.GRAY);
			g.drawString("Difficulty", difficultyX, difficultyY);
			if (MainMenu.getDifficulty() == 1) {
				g.drawString("EASY", easyX, difficultyY);
			} else if (MainMenu.getDifficulty() == 2) {
				g.drawString("NORMAL", normalX, difficultyY);
			} else if (MainMenu.getDifficulty() == 3) {
				g.drawString("HARD", hardX, difficultyY);
			}
			g.drawString("Single Player", singlePlayerX, singlePlayerY);
			g.drawString("Back to Main Menu", mainMenuX, mainMenuY);
			break;
		case 3:
			g.setColor(Color.WHITE);
			g.drawString("Back to Main Menu", mainMenuX, mainMenuY);
			g.setColor(Color.GRAY);
			g.drawString("Difficulty", difficultyX, difficultyY);
			if (MainMenu.getDifficulty() == 1) {
				g.drawString("EASY", easyX, difficultyY);
			} else if (MainMenu.getDifficulty() == 2) {
				g.drawString("NORMAL", normalX, difficultyY);
			} else if (MainMenu.getDifficulty() == 3) {
				g.drawString("HARD", hardX, difficultyY);
			}
			g.drawString("Single Player", singlePlayerX, singlePlayerY);
			g.drawString("Multiplayer", multiplayerX, multiplayerY);
			break;
		}

	}
}

/*
 * This class is very similar to MainMenuPanel and PlayGamePanel. This class
 * will paint/display the options sub-menu
 */
class OptionsPanel extends JPanel {

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}

		g.drawImage(MainMenu.getBackgroundImage().getImage(), 0, 0, getWidth(),
				getHeight(), this);

		g.setFont(Constants.MENU_FONT);
		FontMetrics metrics = g.getFontMetrics(Constants.SELECTIONS_FONT);

		int optionsX = 10;

		int sfxY = getHeight() / 5;
		int sfxOnX = getWidth() - metrics.stringWidth("ON") - 10;
		int sfxOffX = getWidth() - metrics.stringWidth("OFF") - 10;

		int musicY = 2 * getHeight() / 5;
		int musicOnX = getWidth() - metrics.stringWidth("ON") - 10;
		int musicOffX = getWidth() - metrics.stringWidth("OFF") - 10;

		int controlY = 3 * getHeight() / 5;
		int schemeWasdX = getWidth() - metrics.stringWidth("WASD") - 10;
		int schemeArrowX = getWidth() - metrics.stringWidth("Arrow Keys") - 10;

		int mainMenuY = 4 * getHeight() / 5;

		switch (MainMenu.getOptionsPointerPosition() % 4) {
		case 0:
			g.setColor(Color.WHITE);
			g.drawString("Sound Effects", optionsX, sfxY);
			g.setFont(Constants.SELECTIONS_FONT);
			if (MainMenu.isSfxOn()) {
				g.drawString("ON", sfxOnX, sfxY);
			} else {
				g.drawString("OFF", sfxOffX, sfxY);
			}
			g.setColor(Color.GRAY);
			g.setFont(Constants.MENU_FONT);
			g.drawString("Background Music", optionsX, musicY);
			g.drawString("Control Scheme", optionsX, controlY);
			g.drawString("Back to Main Menu", optionsX, mainMenuY);
			g.setFont(Constants.SELECTIONS_FONT);
			if (MainMenu.isMusicOn()) {
				g.drawString("ON", musicOnX, musicY);
			} else {
				g.drawString("OFF", musicOffX, musicY);
			}
			if (MainMenu.isControlWasd()) {
				g.drawString("WASD", schemeWasdX, controlY);
			} else {
				g.drawString("Arrow Keys", schemeArrowX, controlY);
			}
			break;
		case 1:
			g.setColor(Color.WHITE);
			g.drawString("Background Music", optionsX, musicY);

			g.setFont(Constants.SELECTIONS_FONT);

			if (MainMenu.isMusicOn()) {
				g.drawString("ON", musicOnX, musicY);
			} else {
				g.drawString("OFF", musicOffX, musicY);
			}

			g.setColor(Color.GRAY);
			g.setFont(Constants.MENU_FONT);
			g.drawString("Sound Effects", optionsX, sfxY);
			g.drawString("Control Scheme", optionsX, controlY);
			g.drawString("Back to Main Menu", optionsX, mainMenuY);

			g.setFont(Constants.SELECTIONS_FONT);

			if (MainMenu.isSfxOn()) {
				g.drawString("ON", sfxOnX, sfxY);
			} else {
				g.drawString("OFF", sfxOffX, sfxY);
			}
			if (MainMenu.isControlWasd()) {
				g.drawString("WASD", schemeWasdX, controlY);
			} else {
				g.drawString("Arrow Keys", schemeArrowX, controlY);
			}
			break;
		case 2:
			g.setColor(Color.WHITE);
			g.drawString("Control Scheme", optionsX, controlY);

			g.setFont(Constants.SELECTIONS_FONT);

			if (MainMenu.isControlWasd()) {
				g.drawString("WASD", schemeWasdX, controlY);
			} else {
				g.drawString("Arrow Keys", schemeArrowX, controlY);
			}
			g.setColor(Color.GRAY);

			g.setFont(Constants.MENU_FONT);

			g.drawString("Sound Effects", optionsX, sfxY);
			g.drawString("Background Music", optionsX, musicY);
			g.drawString("Back to Main Menu", optionsX, mainMenuY);

			g.setFont(Constants.SELECTIONS_FONT);

			if (MainMenu.isSfxOn()) {
				g.drawString("ON", sfxOnX, sfxY);
			} else {
				g.drawString("OFF", sfxOffX, sfxY);
			}
			if (MainMenu.isMusicOn()) {
				g.drawString("ON", musicOnX, musicY);
			} else {
				g.drawString("OFF", musicOffX, musicY);
			}
			break;
		case 3:
			g.setColor(Color.WHITE);
			g.drawString("Back to Main Menu", optionsX, mainMenuY);

			g.setColor(Color.GRAY);

			g.setFont(Constants.MENU_FONT);

			g.drawString("Sound Effects", optionsX, sfxY);
			g.drawString("Background Music", optionsX, musicY);
			g.drawString("Control Scheme", optionsX, controlY);

			g.setFont(Constants.SELECTIONS_FONT);

			if (MainMenu.isControlWasd()) {
				g.drawString("WASD", schemeWasdX, controlY);
			} else {
				g.drawString("Arrow Keys", schemeArrowX, controlY);
			}
			if (MainMenu.isSfxOn()) {
				g.drawString("ON", sfxOnX, sfxY);
			} else {
				g.drawString("OFF", sfxOffX, sfxY);
			}
			if (MainMenu.isMusicOn()) {
				g.drawString("ON", musicOnX, musicY);
			} else {
				g.drawString("OFF", musicOffX, musicY);
			}
			break;
		}

	}

}

/*
 * This class will paint/display the High Scores in the High Scores sub-menu.
 */
class HighScoresPanel extends JPanel {
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}

		g.drawImage(MainMenu.getBackgroundImage().getImage(), 0, 0, getWidth(),
				getHeight(), this);

		g.setFont(Constants.MENU_FONT);
		// Used to obtain measurements of the Font used in the Play Game
		// Sub-menu
		FontMetrics metrics = g.getFontMetrics(Constants.MENU_FONT);
		// X and Y positions of the Back to Main Menu option calculated
		int highScoresX = getWidth() / 2
				- (metrics.stringWidth("Back to Main Menu") / 2); // Center
																	// text
		int highScoresY = 9 * getHeight() / 10;

		g.setColor(Color.WHITE);
		g.drawString("Back to Main Menu", highScoresX, highScoresY);

		int i = 1;
		File highScoreList = null;
		/*
		 * Find which difficulty the pointer is on in the High Scores menu. Gray
		 * out the options that are not selected. By default, Enter is to exit
		 * to Main Menu so Back to Main Menu option will always be selected.
		 * Depending on the difficulty, the high score file to be read is
		 * decided on accordingly.
		 */
		switch (MainMenu.getHSDifficulty() % 3) {
		case 0:
			highScoreList = new File("src/astr_pkg/hs_easy.csv");
			g.setColor(Color.WHITE);
			g.drawString("EASY", getWidth() / 8, getHeight() / 5);
			g.setColor(Color.GRAY);
			g.drawString("Medium", 2 * getWidth() / 5, getHeight() / 5);
			g.drawString("HARD", 6 * getWidth() / 8, getHeight() / 5);
			break;
		case 1:
			highScoreList = new File("src/astr_pkg/hs_medium.csv");
			g.setColor(Color.WHITE);
			g.drawString("Medium", 2 * getWidth() / 5, getHeight() / 5);
			g.setColor(Color.GRAY);
			g.drawString("EASY", getWidth() / 8, getHeight() / 5);
			g.drawString("HARD", 6 * getWidth() / 8, getHeight() / 5);
			break;
		case 2:
			highScoreList = new File("src/astr_pkg/hs_hard.csv");
			g.setColor(Color.WHITE);
			g.drawString("HARD", 6 * getWidth() / 8, getHeight() / 5);
			g.setColor(Color.GRAY);
			g.drawString("EASY", getWidth() / 8, getHeight() / 5);
			g.drawString("Medium", 2 * getWidth() / 5, getHeight() / 5);
			break;
		}
		g.setFont(Constants.SELECTIONS_FONT);
		metrics = g.getFontMetrics(Constants.SELECTIONS_FONT);
		g.setColor(Color.WHITE);
		/*
		 * This code is for reading the high score file
		 */
		try {
			Scanner file = new Scanner(highScoreList); // Open file
			String line = "error";
			String[] fields = null;
			String name, score, hsTime;
			while (file.hasNext()) { // While not at the end of file
				line = file.next(); // line is an entire line in the file
				// Since we are using csv files (comma-seperated), we split
				// the string in order to identify the different fields in the
				// file
				fields = line.split(",");
				name = fields[0]; // First string before comma is users name
				score = fields[1]; // Second field after first comma is score
				hsTime = fields[2]; // Third field after second comma is time in
									// seconds
				/*
				 * Draw the player name, score and time in high scores sub menu
				 */
				g.drawString(name, getWidth() / 4,
						(getHeight() / 4) + metrics.getHeight() * i);
				g.drawString(score, getWidth() / 2,
						(getHeight() / 4) + metrics.getHeight() * i);
				g.drawString(hsTime + "s", 3 * getWidth() / 4,
						(getHeight() / 4) + metrics.getHeight() * i);
				i++;
				if (i == 11) { // Once 10 high scores have been displayed, stop
					break;
				}
			}
			file.close(); // close file
		} catch (FileNotFoundException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}

	}
}

/*
 * This class will display the game over screen. Two possible situations: 1.
 * User gets a high score, in this case the user will only be prompted to enter
 * a 3 character name. 2. User does not get a high score, in this case the
 * screen will show the user their score and their time and prompt them to play
 * again or return to Main Menu
 */
class GameOverPanel extends JPanel {
	private char hsFirstChar = 'A', hsSecondChar = 'A', hsThirdChar = 'A';

	protected void paintComponent(Graphics g) {
		super.paintComponents(g);

		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}

		g.drawImage(MainMenu.getGameOverImage().getImage(), 0, 0, this);

		g.setFont(Constants.MENU_FONT);
		g.setColor(Color.WHITE);

		FontMetrics metrics = g.getFontMetrics(Constants.MENU_FONT);

		int playAgainX = getWidth() / 2
				- (metrics.stringWidth("Play Again") / 2);
		int playAgainY = 6 * getHeight() / 8;

		int mainMenuX = getWidth() / 2
				- metrics.stringWidth("Back to Main Menu") / 2; // Center text
		int mainMenuY = playAgainY + metrics.getHeight() + 10;

		if (!MainMenu.getGame().getIsHighScore() || MainMenu.isMultiplayer()) { // If
																				// user
																				// did
																				// not
																				// get
			// highscore
			g.drawString("Score: " + MainMenu.getGame().getScore(), 150, 100);
			g.drawString("Time: " + MainMenu.getGame().getGameLength() / 60
					+ " min " + MainMenu.getGame().getGameLength() % 60 + " s",
					150, 200);
			switch (MainMenu.getGameOverPointerPosition() % 2) {
			case 0:
				g.setColor(Color.WHITE);
				g.drawString("Play Again", playAgainX, playAgainY);
				g.setColor(Color.GRAY);
				g.drawString("Back to Main Menu", mainMenuX, mainMenuY);
				break;
			case 1:
				g.setColor(Color.GRAY);
				g.drawString("Play Again", playAgainX, playAgainY);
				g.setColor(Color.WHITE);
				g.drawString("Back to Main Menu", mainMenuX, mainMenuY);
				break;
			}
		} else {
			int hsFirstX = 2 * getWidth() / 5;
			int hsSecondX = 2 * getWidth() / 4;
			int hsThirdX = 3 * getWidth() / 5;
			int hsY = getHeight() / 3;

			g.setColor(Color.RED);
			g.drawString("__", hsFirstX, hsY);
			g.drawString("__", hsSecondX, hsY);
			g.drawString("__", hsThirdX, hsY);
			switch (MainMenu.position) {
			case 0:
				hsFirstChar = (char) ('A' + MainMenu.letter);
				g.setColor(Color.RED);
				g.drawString("" + hsFirstChar, hsFirstX, hsY - 10);
				break;
			case 1:
				hsSecondChar = (char) ('A' + MainMenu.letter);
				g.setColor(Color.ORANGE);
				g.drawString("" + hsFirstChar, hsFirstX, hsY - 10);
				g.setColor(Color.RED);
				g.drawString("" + hsSecondChar, hsSecondX, hsY - 10);
				break;
			case 2:
				hsThirdChar = (char) ('A' + MainMenu.letter);
				g.setColor(Color.ORANGE);
				g.drawString("" + hsFirstChar, hsFirstX, hsY - 10);
				g.drawString("" + hsSecondChar, hsSecondX, hsY - 10);
				g.setColor(Color.RED);
				g.drawString("" + hsThirdChar, hsThirdX, hsY - 10);
				break;
			case 3:
				g.setColor(Color.ORANGE);
				g.drawString("" + hsFirstChar, hsFirstX, hsY - 10);
				g.drawString("" + hsSecondChar, hsSecondX, hsY - 10);
				g.drawString("" + hsThirdChar, hsThirdX, hsY - 10);
			}
		}
	}
}
