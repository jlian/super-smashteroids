package astr_pkg;

import java.awt.Font;
import java.io.File;

public interface Constants {

	static final Font MENU_FONT = new Font("Arial", Font.BOLD, 36);
	static final Font SELECTIONS_FONT = new Font("Dialog", Font.BOLD, 24);
	static final MainMenuPanel MAIN_MENU_PANEL = new MainMenuPanel();
	static final PlayGamePanel PLAY_GAME_PANEL = new PlayGamePanel();
	static final OptionsPanel OPTIONS_PANEL = new OptionsPanel();
	static final HighScoresPanel HIGH_SCORES_PANEL = new HighScoresPanel();
	static final Ship SHIP = new Ship(400, 300, 0, .35, .98, .1);
	static final int WIDTH = 800;
	static final int HEIGHT = 600;
	static final int DEFAULT_RESPAWN_WAIT = 40;
	static final boolean LINUX = true;
	static final Ship P2SHIP = new Ship(380, 300, Math.PI, 0.35, 0.98, .1);
	static final GameOverPanel GAME_OVER_PANEL = new GameOverPanel();
//	static final File HIGH_SCORE_EASY = new File("src/astr_pkg/hs_easy.csv");
//	static final File HIGH_SCORE_MEDIUM = new File("src/astr_pkg/hs_medium.csv");
//	static final File HIGH_SCORE_HARD = new File("src/astr_pkg/hs_hard.csv");

}