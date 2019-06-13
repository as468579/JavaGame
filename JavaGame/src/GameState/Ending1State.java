package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.PlayerSave;
import Main.GamePanel;
import TileMap.Background;

public class Ending1State extends LevelState{
	
	
	public Ending1State(GameStateManager gsm){
		super(gsm);
	}

	@Override
	public void init() {
		System.out.println("ending");
		
		bg = new Background("/Backgrounds/grassbg1.gif",0.5);
		bg.setPosition(0, 0);
		bg.setVector(-0.1, 0);
		
		storyColor = new Color(0,0,0);
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
		if(currentDialog >= 3) {
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
			g.drawString("<SPACE>", GamePanel.WIDTH-55, GamePanel.HEIGHT-5);
			
			g.setColor(storyColor);
			g.setFont(storyFont);
			if(currentDialog >= 0) {
				g.drawString("一般結局", GamePanel.WIDTH/2 - 50, 30);
			}
			if(currentDialog >= 1) {
				g.drawString("宗文得到了神秘的翅膀，", 5, 60);
				g.drawString("飛出危險的地下道。", 5, 90);
				
			}
			if(currentDialog >= 2) {
				g.drawString("經過這次可怕的冒險，", 5, 120);
				g.drawString("他決定成為為民除害的水電工，", 5, 150);
				g.drawString("幫助大家處理下水道中邪惡的生物。", 5, 180);
			}
		}
		
		
		
		
		
	}

	@Override
	public void keyPressed(int k) {
		if( k == KeyEvent.VK_ESCAPE) {
			PlayerSave.init();
			gsm.setStates(GameStateManager.MENUSTATE);
		}
		if( k == KeyEvent.VK_SPACE) {
			currentDialog++;
		}
	}
	
	@Override
	public void keyReleased(int k) {
		
	}
	
		
}
