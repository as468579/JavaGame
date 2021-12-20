package GameState;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;

import Audio.AudioPlayer;
import Entity.Animation;
import Entity.PlayerSave;
import Entity.Object.Player;
import Main.GamePanel;
import Main.GamePanelManager;
import TileMap.Background;

public class SettingState extends GameState{
	
	private Background bg;
	
	// title
	private Color titleColor;
	private Font titleFont;
	
	private Animation[] roles;
	private int currentRole;
	private Font optionFont;
	private int currentChoice = 0;
	private static final String[] OPTIONS = {
			"選擇角色",
			"畫面大小",
			"背景音樂",
			"音效",
			"返回"
		};
	
	public SettingState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	@Override
	public void init() {
		
		try {
			bg = new Background("/Backgrounds/story_bg.gif",1);
			bg.setVector(-0.1, 0);
			bg.setPosition(0, 0);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		titleColor = new Color(0, 0, 0);
		titleFont = new Font(
				"Microsoft JhengHei",
				Font.BOLD,
				28);
		optionFont = new Font("Microsoft JhengHei", Font.BOLD, 13);
		// AudioPlayer.loop("menu", AudioPlayer.BGMUSIC);
		
		// initialize role choosing
		roles = new Animation[2];
		roles[0] = new Animation();
		roles[0].setFrames(LoadState.Human[0]);
		roles[0].setDelay(50);
		roles[0].setLoop(true);
		
		
		roles[1] = new Animation();
		roles[1].setFrames(LoadState.Dragon[0]);
		roles[1].setDelay(50);
		roles[1].setLoop(true);
		

		currentRole = 0;
		
	}

	@Override
	public void update() {
		
		bg.update();
		
		// animation update
		roles[currentRole].update();
		
	}
	
	private void drawOptions(Graphics2D g) {
		
		// draw options
		g.setFont(optionFont);
		for(int i = 0; i < OPTIONS.length; i++){
			if(i == currentChoice){
				g.setColor(Color.RED);
			}
			else{
				g.setColor(Color.BLACK);
			}
			g.drawString(OPTIONS[i], 50, 70 + i * 35);
		}
	}
	
	private void drawVolumes(Graphics2D g) {
		
		int bgVolume = AudioPlayer.getVolumeValue(AudioPlayer.BGMUSIC);
		int sfxVolume = AudioPlayer.getVolumeValue(AudioPlayer.SFX);
		
		// draw volume boxes
		g.setColor(Color.GREEN);
		for(int i = 0; i < bgVolume; i++) {
			g.fillRect(120 + i * 18, 133, 15, 5);
		}
		for(int i = 0; i < sfxVolume; i++) {
			g.fillRect(120 + i * 18, 168, 15, 5);
		}
		
		// draw volume boxes bounds
		g.setColor(Color.BLACK);
		for(int i = 0; i < AudioPlayer.maxVolume; i++) {
			g.drawRect(120 + i * 18, 133, 15, 5);
			g.drawRect(120 + i * 18, 168, 15, 5);
		}
	}

	public void drawScales(Graphics2D g) {
		
		int scale = GamePanel.getScale();
		
		// draw scale boxes
		g.setColor(Color.GREEN);
		for(int i = 0; i < scale; i++) {
			g.fillRect(120 + i * 35, 98, 30, 5);
		}
		
		// draw scale boxes bounds
		g.setColor(Color.BLACK);
		for(int i = 0; i < GamePanel.MAXSCALE; i++) {
			g.drawRect(120 + i * 35, 98, 30, 5);
		}
	}
	
	public void drawRoleAnimation(Graphics2D g) {
		
		int margin = 5;
		int thickness = 2;
		Stroke oldStroke = g.getStroke();
		
		g.setStroke(new BasicStroke(thickness));
		
		for(int i = 0; i < roles.length; i++) {
			
			int width = roles[i].getImage().getWidth();
			int height = roles[i].getImage().getHeight();
			int x = (int)(160 + (width + margin) * i); // let x be the center of image
			int y = 63;                                // let y be the center of image
			
			g.drawImage(
					roles[i].getImage(),
					x - width / 2,
					y - height / 2,
					null
				);
			
			if(i == currentRole) {
				g.setColor(Color.RED);
				g.drawRect(
						x- width / 2,
						y - height / 2,
						width,
						height
					);
			}
		}
		
		// resume thickNess
		g.setStroke(oldStroke);
	}
	
	@Override
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		// draw title
		// g.setColor(titleColor);
		// g.setFont(titleFont);
		// g.drawString("設定", 130, 70);
		
		// draw role's animation
		drawRoleAnimation(g);
		
		// draw options
		drawOptions(g);
		
		// drawScales
		drawScales(g);
		
		// drawVolumes
		drawVolumes(g);
		
		
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void keyReleased(int k) {
		
		int bgVolume = AudioPlayer.getVolumeValue(AudioPlayer.BGMUSIC);
		int sfxVolume = AudioPlayer.getVolumeValue(AudioPlayer.SFX);
		int scale = GamePanel.getScale();
		
		if(k == KeyEvent.VK_ENTER){
			PlayerSave.setCurrentRole(currentRole);
			gsm.setStates(GameStateManager.MENUSTATE);
		}
		else if(k == KeyEvent.VK_UP){
			currentChoice--;
			if(currentChoice == -1)
			{
				currentChoice = OPTIONS.length - 1;
			}
		}
		else if(k == KeyEvent.VK_DOWN){
			currentChoice++;
			if(currentChoice == OPTIONS.length)
			{
				currentChoice = 0;
			}
		}
		else if(k == KeyEvent.VK_LEFT)
		{
			if(currentChoice == 0) {
				roles[currentRole].setCurrentFrame(0);
				currentRole--;
				if(currentRole == -1) {
					currentRole = roles.length - 1;
				}
			}
			else if(currentChoice == 1) {
				GamePanel.setScale(scale - 1);
				// GamePanelManager.window.pack();
			}
			else if(currentChoice == 2) {
				AudioPlayer.setVolumeValue(bgVolume - 1, AudioPlayer.BGMUSIC);
			}
			else if(currentChoice == 3){
				AudioPlayer.setVolumeValue(sfxVolume - 1, AudioPlayer.SFX);
			}
		}
		else if(k == KeyEvent.VK_RIGHT)
		{
			if(currentChoice == 0) {
				roles[currentRole].setCurrentFrame(0);
				currentRole++;
				if(currentRole == roles.length) {
					currentRole = 0;
				}
			}
			else if(currentChoice == 1) {
				GamePanel.setScale(scale + 1);
			}
			else if(currentChoice == 2) {
				AudioPlayer.setVolumeValue(bgVolume + 1, AudioPlayer.BGMUSIC);
			}
			else if(currentChoice == 3){
				AudioPlayer.setVolumeValue(sfxVolume + 1, AudioPlayer.SFX);
			}
		}	
	}
	

}
