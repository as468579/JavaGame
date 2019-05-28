package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.event.KeyEvent;

import Main.GamePanel;
import TileMap.Background;

public class Ending3State extends GameState{
	
	private Background bg;
	private Color titleColor;
	private Color escColor;
	private Font titleFont;
	private Font escFont;
	
	public Ending3State(GameStateManager gsm){
		this.gsm = gsm;
		init();
	}

	@Override
	public void init() {
		System.out.println("ending3");
		
		bg = new Background("/Backgrounds/ending_bg3.gif",0.5);
		bg.setPosition(0, 0);
		
		titleColor = new Color(100,250,100);
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
		g.drawString("真結局", GamePanel.WIDTH/2 - 50, 30);
		g.drawString("宗文在下水道發現神秘了綠色植物", 0, 60);
		g.drawString("，意外發現大家都很喜歡它，", 0, 90);
		g.drawString("於是靠著販賣植物成為了億萬富翁", 0, 120);
		g.drawString("Rich Rich~~~", 0, 150);
	}
	@Override
	public void keyPressed(int k) {
		if( k == KeyEvent.VK_ESCAPE) gsm.setStates(GameStateManager.MENUSTATE);
	}
	
	@Override
	public void keyReleased(int k) {
		
	}
	

}
