package astr_pkg;

import java.awt.Font;

public interface Constants {
	
	static final Font MENU_FONT = new Font("Times-Roman", Font.BOLD, 36);
	static final Font SELECTIONS_FONT = new Font("Dialog", Font.BOLD, 24);
	static final MainMenuPanel MAIN_MENU_PANEL = new MainMenuPanel();
	static final PlayGamePanel PLAY_GAME_PANEL = new PlayGamePanel();
	static final OptionsPanel OPTIONS_PANEL = new OptionsPanel();
	static final Ship SHIP = new Ship(400, 300, 0, .35, .98, .1);

}
