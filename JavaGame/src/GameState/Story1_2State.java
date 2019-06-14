package GameState;

import java.awt.Color;
import java.awt.Font;

/*
 * player enter sewer
 * */

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.Explosion;
import Entity.NPC;
import Entity.Player;
import Entity.PlayerSave;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Story1_2State extends LevelState{
	
	private NPC npc;
	
	public Story1_2State(GameStateManager gsm) {
		super(gsm);
	}
	
	@Override
	public void init() {
		super.init();
		
		System.out.println("sotry 2");
		levelTileY = 0;
		levelTileHeight = 27;
		
		bg = new Background("/Backgrounds/background_1_1.gif",0.5);
		
		tileMap.setTween(0.07);
		player.setPosition(2200, 390);
		
		//let player can't move
		player.stop();
		blockInput = true;
		
		storyPart = new boolean[] {true, false, false, false, false, false};
		basicFont = new Font("Microsoft JhengHei", Font.PLAIN, 11);
		storyFont = new Font("Microsoft JhengHei", Font.PLAIN, 16);
		
		basicColor = new Color(0, 0, 0);
		storyColor = new Color(0, 0, 0);
		
		populateNPC();
	
	}
	
	private void populateNPC() {
		npc = new NPC(tileMap);
		npc.setPosition(2600, 300);
	}
	
	@Override
	public void update() {
		player.update();
		
		// update NPC 
		npc.update();
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
	
	public void reset() {
		// reset player
		player.reset();
		player.setPosition(2200, 390);
		
		// reset Enemies
		// populateEnemies();
		
		blockInput = true;
		eventCount = 0;
		eventStart = true;
	}
	
	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);
		tileMap.draw(g, levelTileY, levelTileHeight);
		player.draw(g);
		
		// draw NPC
		npc.draw(g);
		
		
		tileMap.drawTransparentBlocks(g, levelTileY, levelTileHeight);
		
		
		g.setFont(basicFont);
		g.setColor(basicColor);
		g.drawString("<SPACE>", GamePanel.WIDTH - 50, GamePanel.HEIGHT-10);
		
		g.setFont(storyFont);
		g.setColor(storyColor);
		
		
		
		if(storyPart[0]) {
			g.drawString("到了相約的地點後，宗文看了四周", 40, 50);
		}
		if(storyPart[1]) {
			g.drawString("這裡除了一位恐龍妹，沒有其他人", 40, 50);
		}
		if(storyPart[2]) {
			g.drawString("只能說他與恐龍真的很有緣", 60, 50);
		}
		if(storyPart[3]) {
			g.drawString("在宗文準備偷偷離開的時候，", 60, 50);
			g.drawString("恐龍妹發現了他", 100, 70);
		}
		if(storyPart[4]) {
			g.drawString("還沒來的及逃跑，宗文就被抓住了...", 40, 50);
		}
		if(storyPart[5]) {
			g.drawString("不但被劫色，還被踢到下水道", 50, 50);
		}
		
		
	}
	
	@Override
	public void keyPressed(int k) {
		if( k == KeyEvent.VK_ENTER) {
			endStory();
		}
		if( k == KeyEvent.VK_SPACE) {
			if(!hasAnotherStoryPart(storyPart, storyPart.length))
				endStory();
		}
	}
	
	
	
	private void endStory() {
		
		player.setPosition(2410, 600);
		tileMap.setPosition(
				GamePanel.WIDTH / 2 - player.getX(),
				GamePanel.HEIGHT / 2 - player.getY()
		);
		
		// eventFinish
		eventFinish(GameStateManager.LEVEL1_2STATE);
	}
	
	// finish story
	@Override
	public void eventFinish(int nextState) {
		
		// Save player status
		PlayerSave.setHealth(player.getHealth());
		PlayerSave.setMoney(player.getMoney());
		PlayerSave.setWings(player.hasWings());
		
		PlayerSave.setX(player.getX());
		PlayerSave.setY(player.getY());
		//PlayerSave.setTime(player.getTime());
		
		PlayerSave.setFlying(player.isUp());
		
		gsm.setStates(nextState);
	}
	
	private void autoMove() {
		//player auto move
		if(player.getX() <= 2410) {
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
		if(storyPart[3]) {
			// player move
			player.setRight(false);
			if(player.getX() >= 2490) {
				player.setLeft(true);
			}
			else player.setLeft(false);
			
			// npc move
			if(npc.getX() >= 2600) {
				npc.setLeft(true);
//				npc.setScratching(true);
			}
			else {
				npc.setLeft(false);
//				npc.stop();
//				npc.setScratching(false);
			}
			
		}
		
		if(storyPart[4]) {
			// player move
			player.setRight(false);
			if(player.getX() >= 2440) {
				player.setLeft(true);
			}
			else player.setLeft(false);
			
			// npc move
			npc.setRight(false);
			if(npc.getX() >= 2480) {
				npc.setLeft(true);
			}
			else npc.setLeft(false);
			
		}
		if(storyPart[5]) {
			npc.setScratching();
			player.hit(0);
			player.setRight(false);
			if(player.getX() >= 2420) {
				player.setLeft(true);
			}
			else player.setLeft(false);
		}
	}
	
	
}
