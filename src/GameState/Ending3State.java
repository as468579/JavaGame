package GameState;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Audio.AudioPlayer;
import Entity.PlayerSave;
import Main.GamePanel;
import TileMap.Background;

public class Ending3State extends LevelState{
	
	public Ending3State(GameStateManager gsm){
		super(gsm);
	}

	@Override
	public void init() {
		System.out.println("ending3");
		
		bg = new Background("/Backgrounds/ending_bg3.gif",0.5);
		bg.setPosition(0, 0);
		
		// background music
		levelName = "Ending3";
		AudioPlayer.load("/Music/level1Music.mp3", levelName, AudioPlayer.BGMUSIC);
		
		storyColor = new Color(255,100,255);
		storyFont = new Font(
				"Microsoft JhengHei",
				Font.BOLD,
				20);
		basicColor = new Color(255, 255, 255);
		basicFont = new Font(
				"Microsoft JhengHei",
				Font.PLAIN,
				12);
		
		tb = new ArrayList<Rectangle>();
		eventStart = true;
		eventStart();
	}
	
	public void reset() {
		player.reset();
		blockInput = true;
		eventCount = 0;
		eventStart = true;
	}
	
	@Override
	public void update() {
		bg.update();
		
	}
	
	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
		g.setColor(new Color(50, 50, 50));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		if(currentDialog >= 4) {
			g.setColor(storyColor);
			g.setFont(storyFont);
			g.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 40));
			g.drawString("THE END", 70, GamePanel.HEIGHT/2-10);
			
			g.setColor(basicColor);
			g.setFont(basicFont);
			g.drawString("Press 'esc' to return menu", 0, GamePanel.HEIGHT);
		}else {
			g.setColor(basicColor);
			g.setFont(basicFont);
			g.drawString("<Press X>", GamePanel.WIDTH-60, GamePanel.HEIGHT-5);
			
			g.setColor(storyColor);
			g.setFont(storyFont);
			if(currentDialog >= 0) {
				g.drawString("真結局", GamePanel.WIDTH/2 - 50, 30);
			}
			if(currentDialog >= 1) {
				g.drawString("宗文在下水道翻遍了所有角落", 5, 60);
				g.drawString("幾乎把裡面的所有金幣都拿走", 5, 90);
			}
			if(currentDialog >= 2) {
				g.drawString("回到地表", 5, 140);
				g.drawString("他終於不再是個靠爸爸的人", 5, 170);
				g.drawString("利用這些金幣成為了真正的億萬富翁", 0, 200);
			}
			if(currentDialog >= 3) {
				g.drawString("Rich Rich~~~", 50, 230);
			}
		}
	}
	@Override
	public void keyPressed(int k) {
		if( k == KeyEvent.VK_ESCAPE) {
			eventFinish(GameStateManager.MENUSTATE);
		}
		if( k == KeyEvent.VK_SPACE || k == KeyEvent.VK_X) {
			currentDialog++;
		}
	}
	
	@Override
	public void keyReleased(int k) {
		
	}
	
	@Override
	public void eventFinish(int nextState) {
		PlayerSave.init();
		AudioPlayer.close(levelName, AudioPlayer.BGMUSIC);
		gsm.setStates(nextState);
	}

}
