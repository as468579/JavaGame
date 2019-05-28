package GameState;

import java.awt.Color;
import java.awt.Font;

/*
 * player enter sewer
 * */

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Entity.Enemy;
import Entity.Explosion;
import Entity.Player;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Story1_2State extends LevelState{
	
	
	private int current = 1;
	private boolean part[] = {true, false, false, false};
	
	private Font basicFont;
	private Font storyFont;
	
	private Color basicColor;
	private Color storyColor;
	
	
	public Story1_2State(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	@Override
	public void init() {
		
		System.out.println("sotry 2");
		levelTileY = 0;
		levelTileHeight = 27;
		
		bg = new Background("/Backgrounds/background_1_1.gif",0.5);
		
		tileMap.setTween(1);
		player.setPosition(2200, 390);
		
		basicFont = new Font(Font.DIALOG, Font.PLAIN, 10);
		storyFont = new Font(Font.SANS_SERIF, Font.PLAIN, 15);
		
		basicColor = new Color(0, 0, 0);
		storyColor = new Color(0, 0, 255);
		
		//let player can't move
		player.setLeft(false);
		player.setRight(false);
		player.setUp(false);
		player.setDown(false);
		player.setJumping(false);
		player.setGliding(false);
	}
	
	@Override
	public void update() {
		player.update();
		tileMap.setPosition(
				GamePanel.WIDTH / 2 - player.getX(),
				GamePanel.HEIGHT / 2 - player.getY()
		);
		bg.setPosition(tileMap.getX(), tileMap.getY());
		
		autoMove();
		
		
		if(player.getY() > (levelTileY + levelTileHeight)*16 ) {
			endStory();
		}
		
	}
	
	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);
		tileMap.draw(g, levelTileY, levelTileHeight);
		player.draw(g);
		tileMap.drawTransparent(g, levelTileY, levelTileHeight);
		
		
		g.setFont(basicFont);
		g.setColor(basicColor);
		g.drawString("'space'", GamePanel.WIDTH - 40, GamePanel.HEIGHT-10);
		
		g.setFont(storyFont);
		g.setColor(storyColor);
		
		if(part[0]) {
			g.drawString("沒想到遇到恐龍妹", GamePanel.WIDTH/2 - 70, 30);
		}
		else if(part[1]) {
			g.drawString("專門搶別人恐龍的那種", GamePanel.WIDTH/2 - 70, 30);
		}
		else if(part[2]) {
			g.drawString("恐龍被搶，還被踢到下水道", GamePanel.WIDTH/2 - 70, 30);
		}
	}
	
	@Override
	public void keyPressed(int k) {
		if( k == KeyEvent.VK_ENTER) {
			endStory();
		}
		if( k == KeyEvent.VK_SPACE) {
			goAnotherPart();
		}
	}
	
	private void endStory() {
		player.setLeft(false);
		player.setRight(false);
		player.setUp(false);
		player.setDown(false);
		player.setJumping(false);
		player.setGliding(false);
		
		
		player.setPosition(2410, 500);
		tileMap.setPosition(
				GamePanel.WIDTH / 2 - player.getX(),
				GamePanel.HEIGHT / 2 - player.getY()
		);
		gsm.setStates(GameStateManager.LEVEL1_2STATE);
	}
	
	private void autoMove() {
		//player auto move
		if(player.getX() <= 2400) {
			player.setRight(true);
		}
		
		if(player.getX() >= 2360 && player.getX() <= 2400){
			player.setJumping(true);
		}else {
			player.setJumping(false);
			if(player.getX() >= 2500) {
				player.setRight(false);
			}
		}
		
		if(part[2]) {
			if(player.getX() >= 2410) {
				player.setLeft(true);
			}
			else player.setLeft(false);
		}
	}
	
	private void goAnotherPart() {
		if(current == 3) {
			endStory();
			return;
		}
		if(current >= 1) {
			part[current - 1] = false;
		}
		part[current] = true;
		System.out.println(current);
		current++;
		return;
		
	}
}
