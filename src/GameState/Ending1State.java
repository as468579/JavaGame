package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.event.KeyEvent;

import Main.GamePanel;
import TileMap.Background;

public class Ending1State extends GameState{
	
	private Background bg;
	private Color titleColor;
	private Color escColor;
	private Font titleFont;
	private Font escFont;
	
	public Ending1State(GameStateManager gsm){
		this.gsm = gsm;
		init();
	}

	@Override
	public void init() {
		System.out.println("ending");
		
		bg = new Background("/Backgrounds/grassbg1.gif",0.5);
		bg.setPosition(0, 0);
		
		titleColor = new Color(0,0,0);
		escColor = new Color(255, 255, 255);
		escFont = new Font(
				"Century Gothic",
				Font.PLAIN,
				10);
		titleFont = new Font(
				"utf-8",
				Font.BOLD,
				20);
		
	}
	
	@Override
	public void update() {
		bg.update();
		
	}
	
	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);
		
		g.setColor(escColor);
		g.setFont(escFont);
		g.drawString("Press 'esc' to return menu", 0, GamePanel.HEIGHT);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("一般結局", GamePanel.WIDTH/2 - 50, 30);
		g.drawString("宗文得到了神秘的翅膀，飛出危險", 0, 60);
		g.drawString("的地下道，經過這次可怕的冒險，", 0, 90);
		g.drawString("他決定成為為民除害的水電工，", 0, 120);
		g.drawString("幫助大家處理下水道中邪惡的生物。", 0, 150);
	}
	@Override
	public void keyPressed(int k) {
		if( k == KeyEvent.VK_ESCAPE) gsm.setStates(GameStateManager.MENUSTATE);
	}
	
	@Override
	public void keyReleased(int k) {
		
	}
	

}
