package astr_pkg;

import java.awt.Font;
import java.io.File;

//constants for our entire build
public interface Constants {

	static final Font MENU_FONT = new Font("Arial", Font.BOLD, 36);// main menu font
	static final Font SELECTIONS_FONT = new Font("Dialog", Font.BOLD, 24);// selections font
	static final MainMenuPanel MAIN_MENU_PANEL = new MainMenuPanel();// main menu panel
	static final PlayGamePanel PLAY_GAME_PANEL = new PlayGamePanel();// play game panel
	static final OptionsPanel OPTIONS_PANEL = new OptionsPanel();// options panel
	static final HighScoresPanel HIGH_SCORES_PANEL = new HighScoresPanel();// highscores panel
	static final Ship SHIP = new Ship(400, 300, 0, .35, .98, .1);// player 1 ship
	static final int WIDTH = 800;//sets width
	static final int HEIGHT = 600;// sets height
	static final int DEFAULT_RESPAWN_WAIT = 40;// sets frame rate
	static final boolean LINUX = false;// detects if linux
	static final Ship P2SHIP = new Ship(380, 300, Math.PI, 0.35, 0.98, .1);// player 2 ship
	static final GameOverPanel GAME_OVER_PANEL = new GameOverPanel();// game over panel
}