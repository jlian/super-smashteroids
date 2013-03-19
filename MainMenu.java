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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainMenu extends JFrame{
	
	private static final ImageIcon background = new ImageIcon("src/astr_pkg/background_main_menu2.jpg");
	private static int countMain = 0; //Controls which option in Main Menu should be highlighted
	private static int countPlay = 0; //Controls which option in the Play Game menu should be highlighted
	private static int countOptions = 0; //Controls which option in the Options menu should be highlighted
	private static boolean musicVolume = true; //Controls if background music is on or off
	private static boolean sfxVolume = true; // Controls if sound effects are on or off
	private static boolean wasd = true; //Controls if the user has selected WASD or Arrow Keys controls
	private static Font titleFont; //The custom font for the title of the game
	private static Clip clip; //Sound effect for when options are scrolled in the Main Menu
	
	public MainMenu(){
		
		/*
		 * Code for getting an audio file to be accessible for playback
		 * Clip Class is used for this.
		 */
//		try {
//			File menuSelection = new File("src/com/team3/asteroids/menu_sound_1.wav");
//			AudioInputStream audioIn = AudioSystem.getAudioInputStream(menuSelection);
//			clip = AudioSystem.getClip();
//			clip.open(audioIn);
//		} catch (UnsupportedAudioFileException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (LineUnavailableException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		add(Constants.MAIN_MENU_PANEL); //Add panel, which has main menu options, to the frame
		Constants.MAIN_MENU_PANEL.setFocusable(true);//Make this panel able to detect key presses
		Constants.MAIN_MENU_PANEL.requestFocusInWindow(); //from the keyboard
		
		Constants.MAIN_MENU_PANEL.addKeyListener(new KeyAdapter(){ //Keyboard event handler
			public void keyPressed(KeyEvent e){
				switch(e.getKeyCode()){
				case KeyEvent.VK_DOWN: //If down arrow key pressed,
					countMain++; //increment count --> move down the list of options
//					if(clip.isRunning()){ //If the clip is still running
//						clip.stop(); //Stop playback
//					}
//					clip.setFramePosition(0); //Rewind audio to beginning
//					clip.start(); //Play audio
					break;
				case KeyEvent.VK_UP: //If upward arrow key pressed,
					countMain--; //decrement count --> move up the list of options
					if (countMain < 0){ //Keep value positive as to keep proper track of the
						countMain = 3; //keyboard pointer location.				
					}
//					if(clip.isRunning()){ //If clip is still running
//						clip.stop(); //Stop playback
//					}
//					clip.setFramePosition(0); //Rewind audio to beginning
//					clip.start(); //Play audio
					break;
				case KeyEvent.VK_ENTER: //If the Enter key is pressed, depending on the option selected
					if(countMain % 4 == 0){ // If first option selected, "Play Game"
						//Show the Play Game sub-menu
						remove(Constants.MAIN_MENU_PANEL); //Remove the Main Menu
						add(Constants.PLAY_GAME_PANEL); //Show the Play Game sub-menu
						revalidate(); //refresh frame
						Constants.PLAY_GAME_PANEL.setFocusable(true);//Make this panel able to detect key presses
						Constants.PLAY_GAME_PANEL.requestFocusInWindow(); //from the keyboard
					}else if (countMain % 4 == 1){ //If 2nd option selected, "Options"
						//Show the Options sub-menu
						remove(Constants.MAIN_MENU_PANEL); //Remove Main Menu
						add(Constants.OPTIONS_PANEL); //Show Options sub-menu
						revalidate(); //refresh frame
						Constants.OPTIONS_PANEL.setFocusable(true); //Make this panel able to detect key presses
						Constants.OPTIONS_PANEL.requestFocusInWindow(); //from the keyboard
					}else if(countMain % 4 == 2){ //If 3rd option selected, "High Scores"
						System.out.println("High Scores Menu Pops Up"); //Show High Scores Menu
					}else{ //If last option selected, "Exit"
						System.exit(0); //Terminate program
					}
					break;
				}
				repaint(); //repaint canvas every time a key is pressed
			}
		});
		
		
		//Code to check for Keyboard events in the Play Game Sub-menu 
		Constants.PLAY_GAME_PANEL.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				switch(e.getKeyCode()){
				case KeyEvent.VK_DOWN: //If down arrow key pressed
					countPlay++; // move down list of options
					break;
				case KeyEvent.VK_UP: //If up arrow key pressed
					countPlay--; // move up list of options
					if(countPlay < 0){ //Keep value positive as to keep proper track of the
						countPlay = 2; //keyboard pointer location.	
					}
					break;
				case KeyEvent.VK_ENTER: //If enter key pressed
					switch(countPlay % 3){
					case 0: //If first option selected "Single Player"
						//Start a Single Player game
						System.out.println("Single Player Game Selected");
						remove(Constants.PLAY_GAME_PANEL);
						AsteroidsGame game = new AsteroidsGame();
						add(game);
						revalidate();
						game.setFocusable(true);
						game.requestFocusInWindow();
						game.init();
						break;
					case 1: //If second option selected "Multiplayer"
						//Start a Multiplayer game
						System.out.println("Multiplayer Game Selected");
						break;
					case 2: //If third option selected "Back to Main Menu"
						remove(Constants.PLAY_GAME_PANEL); //remove the Play Game sub-menu from frame
						add(Constants.MAIN_MENU_PANEL); //Add the Main Menu to frame
						revalidate(); //refresh frame
						Constants.MAIN_MENU_PANEL.setFocusable(true); //Make Main Menu able to detect key presses
						Constants.MAIN_MENU_PANEL.requestFocusInWindow(); //from the keyboard
						countPlay = 0; //reset pointer position in Play Game sub-menu to top option
						break;
					}break;
				}
				repaint();//repaint canvas every time a key is pressed
			}
		});
		
		//Code to check for keyboard events in the Options sub-menu
		Constants.OPTIONS_PANEL.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				switch(e.getKeyCode()){
				case KeyEvent.VK_DOWN: //If down arrow key pressed
					countOptions++; //move down list of options
					break;
				case KeyEvent.VK_UP: //If up arrow key pressed
					countOptions--; //Move up list of options
					if(countOptions < 0){ //Keep value positive as to keep proper track of the
						countOptions = 3; //keyboard pointer location
					}
					break;
				case KeyEvent.VK_ENTER: //If Enter key pressed
					switch(countOptions % 4){
					case 0: //If first option selected
						//Toggle Sound effects volume ON or OFF
						System.out.println("Sound Effects ON/OFF");
						//If sound effects option is on and user presses enter
						if(isSfxOn()){ 
							sfxVolume = false; //turn sound effects off
						}else{
							sfxVolume = true; //else turn sound effects on
						}
						break;
					case 1: //If second option selected
						//Toggle Background Music ON or Off
						System.out.println("Background Music ON/OFF");
						//If music option is ON and user presses Enter
						if(musicVolume){
							musicVolume = false; //Turn Music off
						}else{
							musicVolume = true; //Else turn Music on
						}
						break;
					case 2: //If thrid option selected
						//Toggle Control schemes
						System.out.println("Control Scheme");
						//If WASD option and user presses enter
						if(wasd){
							wasd = false; //Scheme becomes arrow keys
						}else{
							wasd = true; //Else Scheme is WASD
						}
						break;
					case 3: //If third option selected "Back to Main Menu"
						remove(Constants.OPTIONS_PANEL); //Remove Option sub-menu from frame
						add(Constants.MAIN_MENU_PANEL); //Add/Show Main Menu in the frame
						revalidate(); //Refresh Frame
						Constants.MAIN_MENU_PANEL.setFocusable(true); //Make Main Menu able to detect key presses
						Constants.MAIN_MENU_PANEL.requestFocusInWindow(); //from the keyboard
						countOptions = 0; //reset pointer position in Options sub-menu to top option
						break;
					}break;
				}
				repaint(); //repaint canvas every time a key is pressed
			}
		});
	}
	
	//Return the background Image
	public static ImageIcon getBackgroundImage(){
		return background;
	}
	
	//Return the position of the pointer in the main menu
	public static int getMainMenuPointerPosition(){
		return countMain;
	}
	
	//Return the position of the pointer in the Play Game sub-menu
	public static int getPlayGamePointerPosition(){
		return countPlay;
	}
	
	//Return the position of the pointer in the Options sub-menu
	public static int getOptionsPointerPosition(){
		return countOptions;
	}
	
	//Return true if background music option is "ON"
	public static boolean isMusicOn(){
		return musicVolume;
	}
	
	//Return true if Sound Effects option is "ON"
	public static boolean isSfxOn(){
		return sfxVolume;
	}
	
	//Return true if control scheme is WASD
	public static boolean isControlWasd(){
		return wasd;
	}
	
	//Return the custom title Font
	public static Font getTitleFont(){
		return titleFont;
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args){
		// TODO Auto-generated method stub
		try {
			//Create and register a custom font
			titleFont = Font.createFont(Font.TRUETYPE_FONT, 
					new File("src/astr_pkg/Autobahn.ttf"));
			titleFont = titleFont.deriveFont(Font.PLAIN, 70);
			GraphicsEnvironment ge = 
					GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(titleFont);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MainMenu menu = new MainMenu();
		//N.N = not necessary / not visible
		menu.setBackground(Color.BLACK); //Set background color of frame(Black) N.N
		menu.setForeground(Color.RED); //Set foreground color of Frame (RED) N.N
		menu.setUndecorated(true); //Hide the toolbar at the top of windows
		menu.setSize(800, 600); //Set size to 800x600
		menu.setLocationRelativeTo(null); //Center frame
		menu.setVisible(true); //Make frame visible
	}

}

class MainMenuPanel extends JPanel{
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		
		if(g instanceof Graphics2D){
			Graphics2D g2 = (Graphics2D) g;
			//Make text smooth
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
		//Used to obtain measurements of the Font used in the Main Menu
		FontMetrics metrics = g.getFontMetrics(Constants.MENU_FONT);
		
		//X and Y positions of the Play Game option calculated
		int playGameX = getWidth() / 2 - metrics.stringWidth("Play Game") / 2; //Center text in panel
		int playGameY = 3 * getHeight() / 5 ;
		
		//X and Y positions of the Options selection calculated
		int optionsX = getWidth() / 2 - metrics.stringWidth("Options") / 2; //Center text in panel
		int optionsY = playGameY + metrics.getHeight() + 10; //Y position of text right beneath option above
		
		//X and Y positions of the High Scores option calculated
		int highScoreX = getWidth() / 2 - metrics.stringWidth("High Scores") / 2; //Center text in panel
		int highScoreY = optionsY + metrics.getHeight() + 10;//Y position of text right beneath option above
		
		//X and Y positions of the Exit option calculated
		int exitX = getWidth() / 2 - metrics.stringWidth("Exit") / 2; //Center text in panel
		int exitY = highScoreY + metrics.getHeight() + 10;//Y position of text right beneath option above
		
		//Add the background Image to Panel
		g.drawImage(MainMenu.getBackgroundImage().getImage(), 0, 0, getWidth(), getHeight(), this);
		//If the custom title font was successfully accessed
		if(MainMenu.getTitleFont() != null){
			g.setFont(MainMenu.getTitleFont());
			FontMetrics titleMetrics = g.getFontMetrics(MainMenu.getTitleFont());
			int titleLine1X = getWidth() / 2 - titleMetrics.stringWidth("SUPER") / 2;
			int titleLine1Y = getHeight() / 4;
			
			int titleLine2X = getWidth() / 2 - titleMetrics.stringWidth("SMASHTEROIDS") / 2;
			int titleLine2Y = titleLine1Y + titleMetrics.getHeight() + 10;
			if(g instanceof Graphics2D){
				Graphics2D g2d = (Graphics2D) g;
//				g2d.setPaint(new GradientPaint(10, getHeight()/4, Color.ORANGE,
//						titleMetrics.stringWidth("SUPER SMASTHEROIDS"), getHeight()/4 + titleMetrics.getHeight(), Color.RED));
				g2d.setPaint(new GradientPaint(titleLine1X, titleLine1Y, Color.ORANGE,
						titleLine1X + titleMetrics.stringWidth("SUPER"), titleLine1Y + titleMetrics.getHeight(), Color.RED));
				g2d.setFont(MainMenu.getTitleFont());
				g2d.drawString("SUPER", titleLine1X, titleLine1Y);
				g2d.setPaint(new GradientPaint(titleLine2X, titleLine2Y, Color.ORANGE,
						titleLine2X + titleMetrics.stringWidth("SMASHTEROIDS"), titleLine2Y + titleMetrics.getHeight(), Color.RED));
				g2d.drawString("SMASHTEROIDS", titleLine2X, titleLine2Y);
			}else{
				g.setColor(new Color(255, 120, 0));
				g.drawString("SUPER SMASHTEROIDS", getWidth()/2 - titleMetrics.stringWidth("SUPER SMASHTEROIDS") / 2, getHeight()/4);
			}
		}else{
			g.setFont(Constants.MENU_FONT);
			g.setColor(Color.ORANGE);
			g.drawString("SUPER SMASHTEROIDS", getWidth()/2 - metrics.stringWidth("SUPER SMASHTEROIDS") / 2, getHeight()/4);
		}
			
		
		g.setFont(Constants.MENU_FONT);
		
		/*
		 * This set of code is conditions to check where the pointer position
		 * is in the Main Menu. The text is added accordingly with the option
		 * corresponding to the pointer position having a White text color and
		 * the other options remaining grayed out (Gray font color)
		 */
		switch(MainMenu.getMainMenuPointerPosition() % 4){ 
		case 0: //Play Game option highlighted
			g.setColor(Color.WHITE);
			g.drawString("Play Game", playGameX, playGameY);
			g.setColor(Color.GRAY);
			g.drawString("Options", optionsX, optionsY);
			g.drawString("High Scores", highScoreX, highScoreY);
			g.drawString("Exit", exitX, exitY);
			break;
		case 1: //Options highlighted
			g.setColor(Color.GRAY);
			g.drawString("Play Game", playGameX, playGameY);
			g.setColor(Color.WHITE);
			g.drawString("Options", optionsX, optionsY);
			g.setColor(Color.GRAY);
			g.drawString("High Scores", highScoreX, highScoreY);
			g.drawString("Exit", exitX, exitY);
			break;
		case 2: //High Scores highlighted
			g.setColor(Color.GRAY);
			g.drawString("Play Game", playGameX, playGameY);
			g.drawString("Options", optionsX, optionsY);
			g.setColor(Color.WHITE);
			g.drawString("High Scores", highScoreX, highScoreY);
			g.setColor(Color.GRAY);
			g.drawString("Exit", exitX, exitY);
			break;
		case 3: //Exit highlighted
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

class PlayGamePanel extends JPanel{
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(g instanceof Graphics2D){
			Graphics2D g2 = (Graphics2D) g;
			//Make text smooth
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
		
		//Add the background Image to Panel
		g.drawImage(MainMenu.getBackgroundImage().getImage(), 0, 0, getWidth(), getHeight(), this);
		
		//Set the Font
		g.setFont(Constants.MENU_FONT);
		//Used to obtain measurements of the Font used in the Play Game Sub-menu
		FontMetrics metrics = g.getFontMetrics(Constants.MENU_FONT);
		//X and Y positions of the Single Player option calculated
		int singlePlayerX = getWidth()/2 - metrics.stringWidth("Single Player")/2; //Center text
		int singlePlayerY = getHeight() / 4;
		//X and Y positions of the Multiplayer option calculated
		int multiplayerX = getWidth() / 2 - metrics.stringWidth("Multiplayer") / 2; //Center text
		int multiplayerY = 2 * getHeight() / 4;
		//X and Y positions of the Back to Main Menu option calculated
		int mainMenuX = getWidth() / 2 - metrics.stringWidth("Back to Main Menu") / 2; //Center text
		int mainMenuY = 3 * getHeight() / 4;
		
		/*
		 * This set of code is conditions to check where the pointer position
		 * is in the Main Menu. The text is added accordingly with the option
		 * corresponding to the pointer position having a White text color and
		 * the other options remaining grayed out (Gray font color)
		 */
		switch(MainMenu.getPlayGamePointerPosition() % 3){
		case 0:
			g.setColor(Color.WHITE);
			g.drawString("Single Player", singlePlayerX, singlePlayerY);
			g.setColor(Color.GRAY);
			g.drawString("Multiplayer", multiplayerX, multiplayerY);
			g.drawString("Back to Main Menu", mainMenuX, mainMenuY);
			break;
		case 1:
			g.setColor(Color.WHITE);
			g.drawString("Multiplayer", multiplayerX, multiplayerY);
			g.setColor(Color.GRAY);
			g.drawString("Single Player", singlePlayerX, singlePlayerY);
			g.drawString("Back to Main Menu", mainMenuX, mainMenuY);
			break;
		case 2:
			g.setColor(Color.WHITE);
			g.drawString("Back to Main Menu", mainMenuX, mainMenuY);
			g.setColor(Color.GRAY);
			g.drawString("Single Player", singlePlayerX, singlePlayerY);
			g.drawString("Multiplayer", multiplayerX, multiplayerY);
			break;
		}
		
	}
}

class OptionsPanel extends JPanel{
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(g instanceof Graphics2D){
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
		
		g.drawImage(MainMenu.getBackgroundImage().getImage(), 0, 0, getWidth(), getHeight(), this);
		
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
		
		switch(MainMenu.getOptionsPointerPosition() % 4){
		case 0:
			g.setColor(Color.WHITE);
			g.drawString("Sound Effects", optionsX, sfxY);
			g.setFont(Constants.SELECTIONS_FONT);
			if(MainMenu.isSfxOn()){
				g.drawString("ON", sfxOnX, sfxY);
			}else{
				g.drawString("OFF", sfxOffX, sfxY);
			}
			g.setColor(Color.GRAY);
			g.setFont(Constants.MENU_FONT);
			g.drawString("Background Music", optionsX, musicY);
			g.drawString("Control Scheme", optionsX, controlY);
			g.drawString("Back to Main Menu", optionsX, mainMenuY);
			g.setFont(Constants.SELECTIONS_FONT);
			if(MainMenu.isMusicOn()){
				g.drawString("ON", musicOnX, musicY);
			}else{
				g.drawString("OFF", musicOffX, musicY);
			}
			if(MainMenu.isControlWasd()){
				g.drawString("WASD", schemeWasdX, controlY);
			}else{
				g.drawString("Arrow Keys", schemeArrowX, controlY);
			}
			break;
		case 1:
			g.setColor(Color.WHITE);
			g.drawString("Background Music", optionsX, musicY);
			
			g.setFont(Constants.SELECTIONS_FONT);
			
			if(MainMenu.isMusicOn()){
				g.drawString("ON", musicOnX, musicY);
			}else{
				g.drawString("OFF", musicOffX, musicY);
			}
			
			g.setColor(Color.GRAY);
			g.setFont(Constants.MENU_FONT);
			g.drawString("Sound Effects", optionsX, sfxY);
			g.drawString("Control Scheme", optionsX, controlY);
			g.drawString("Back to Main Menu", optionsX, mainMenuY);
			
			g.setFont(Constants.SELECTIONS_FONT);
			
			if(MainMenu.isSfxOn()){
				g.drawString("ON", sfxOnX, sfxY);
			}else{
				g.drawString("OFF", sfxOffX, sfxY);
			}
			if(MainMenu.isControlWasd()){
				g.drawString("WASD", schemeWasdX, controlY);
			}else{
				g.drawString("Arrow Keys", schemeArrowX, controlY);
			}
			break;
		case 2:
			g.setColor(Color.WHITE);
			g.drawString("Control Scheme", optionsX, controlY);
			
			g.setFont(Constants.SELECTIONS_FONT);
			
			if(MainMenu.isControlWasd()){
				g.drawString("WASD", schemeWasdX, controlY);
			}else{
				g.drawString("Arrow Keys", schemeArrowX, controlY);
			}
			g.setColor(Color.GRAY);
			
			g.setFont(Constants.MENU_FONT);
			
			g.drawString("Sound Effects", optionsX, sfxY);
			g.drawString("Background Music", optionsX, musicY);
			g.drawString("Back to Main Menu", optionsX, mainMenuY);
			
			g.setFont(Constants.SELECTIONS_FONT);
			
			if(MainMenu.isSfxOn()){
				g.drawString("ON", sfxOnX, sfxY);
			}else{
				g.drawString("OFF", sfxOffX, sfxY);
			}
			if(MainMenu.isMusicOn()){
				g.drawString("ON", musicOnX, musicY);
			}else{
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
			
			if(MainMenu.isControlWasd()){
				g.drawString("WASD", schemeWasdX, controlY);
			}else{
				g.drawString("Arrow Keys", schemeArrowX, controlY);
			}
			if(MainMenu.isSfxOn()){
				g.drawString("ON", sfxOnX, sfxY);
			}else{
				g.drawString("OFF", sfxOffX, sfxY);
			}
			if(MainMenu.isMusicOn()){
				g.drawString("ON", musicOnX, musicY);
			}else{
				g.drawString("OFF", musicOffX, musicY);
			}
			break;
		}
		
	}
}