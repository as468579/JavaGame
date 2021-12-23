package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

import Audio.AudioPlayer;
import Entity.PlayerSave;
import TileMap.Background;

public class MenuState extends GameState{
	
	private Background bg;
	private AudioPlayer bgMusic;
	
	private int currentChoice = 0;
	private String[] options = {
		"Start",
		"Store", 
		"Setting",
		"Quit"
	};
	private Color  titleColor;
	private Font   titleFont;
	private int titleX;
	private int titleY;
	
	private Font optionFont;
	private int  optionX;
	private int  optionY;
	private int  optionInterval;
	
	public MenuState(GameStateManager gsm){
		this.gsm = gsm;
		init();
		
		try {

			bg = new Background("/Backgrounds/morning.jpg", 1);
			bg.setVector(-0.5, 0);
			bg.setPosition(0, 0);
			
			titleColor = new Color(128,0,0);
			titleFont  = new Font(
					"Century Gothic",
					Font.BOLD,
					70);
			titleX     = 80;
			titleY     = 250;
			
			optionFont = new Font(
					"Arial",
					Font.BOLD,
					50);
			optionX        = 600;
			optionY        = 450;
			optionInterval = 80;
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void init() {
		//bgMusic = new AudioPlayer("/Music/MenuMusic.mp3");
		AudioPlayer.load("/Music/menuMusic.mp3", "menu", AudioPlayer.BGMUSIC);
		AudioPlayer.setVolume();
		AudioPlayer.loop("menu", AudioPlayer.BGMUSIC);
	}

	@Override
	public void update() {
		bg.update();
	}

	@Override
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Zong Wen's Facinated Adventure", titleX, titleY);
		
		// draw menu options
		g.setFont(optionFont);
		for(int i = 0; i < options.length; i++){
			if(i == currentChoice){
				g.setColor(Color.RED);
			}
			else{
				g.setColor(Color.BLACK);
			}
			g.drawString(options[i], optionX - (options[i].length()/2 * 23), optionY + i * optionInterval);
		}
	}
	
	private void select(){
		if(currentChoice == 0){
			
			if(PlayerSave.getCurrentRole() == PlayerSave.DRAGON) { //dragon type
				gsm.setStates(GameStateManager.STORY1_1_2STATE);
			}
			else if(PlayerSave.getCurrentRole() == PlayerSave.HUMAN) { // human type
				gsm.setStates(GameStateManager.STORY1_1_1STATE);
			}
		}
		else if(currentChoice == 1){
			gsm.setStates(GameStateManager.SETTINGSTATE);
		}
		else if(currentChoice == 2){
			System.exit(0);
		}
	}

	@Override
	public void keyPressed(int k) {

	}

	@Override
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_ENTER){
			select();
			AudioPlayer.close("menu", AudioPlayer.BGMUSIC);
		}
		else if(k == KeyEvent.VK_UP){
			currentChoice--;
			if(currentChoice == -1)
			{
				currentChoice = options.length - 1;
			}
		}
		else if(k == KeyEvent.VK_DOWN){
			currentChoice++;
			if(currentChoice == options.length)
			{
				currentChoice = 0;
			}
		}
	}
	
}
