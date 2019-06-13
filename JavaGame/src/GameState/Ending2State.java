package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.event.KeyEvent;

import Entity.PlayerSave;
import Main.GamePanel;
import TileMap.Background;

public class Ending2State extends LevelState{
	
	
	public Ending2State(GameStateManager gsm){
		super(gsm);
		init();
	}

	@Override
	public void init() {
		System.out.println("ending2");
		
		bg = new Background("/Backgrounds/background_1_7.gif",0.5);
		bg.setPosition(0, 0);
		bg.setVector(-0.1, 0);
		
		storyColor = new Color(200,0,0);
		basicColor = new Color(255, 255, 255);
		basicFont = new Font(
				"Microsoft JhengHei",
				Font.PLAIN,
				12);
		storyFont = new Font(
				"Microsoft JhengHei",
				Font.BOLD,
				20);
		
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
				g.drawString("壞結局", GamePanel.WIDTH/2 - 50, 30);
			}
			if(currentDialog >= 1) {
				g.drawString("宗文掉入神秘的地下世界，", 10, 60);
				g.drawString("掉落的途中翅膀也壞了，", 10, 90);
				
			}
			if(currentDialog >= 2) {
				g.drawString("為了逃離可怕的下水道，", 10, 120);
				g.drawString("他只能繼續前進，", 10, 150);
				g.drawString("尋找其他出口QQ。", 10, 180);
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
