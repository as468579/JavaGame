package GameState;

import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;

import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Item;
import Entity.Player;
import Entity.Enemies.Alligator;
import Entity.Enemies.Slugger;
import Entity.Enemies.Snake;
import Entity.Items.Bomb;
import Entity.Items.Treasurebox;
import Main.GamePanel;
import TileMap.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Audio.AudioPlayer;
public abstract class LevelState extends GameState{
	
	protected static TileMap tileMap;
	protected static Player player;
	protected static HUD hud;
	
	protected Background bg;
	
	

	protected ArrayList<Enemy> enemies;//
	protected ArrayList<Explosion> explosions;//
	protected ArrayList<Item> items;//
	
	
	protected AudioPlayer bgMusic;
	
	protected int levelTileY;
	protected int levelTileHeight;
	

	
	public LevelState(GameStateManager gsm){
		this.gsm = gsm;
	}

	@Override
	public void init() {
		tileMap = new TileMap(16);
		
		// load tile
		tileMap.loadTile("/Tilesets/mario_tilesets.png", 2, 2);
		tileMap.loadTile("/Tilesets/underground_tiles.png", 1, 0);
		
		// load tileMap
		tileMap.loadMap("res/Maps/Map.json");
		
		tileMap.setPosition(0, 0);
		tileMap.setTween(0.07);
		
		player = new Player(tileMap);
		
		bg = new Background("/Backgrounds/background.jpg", 0.5);
		
		levelTileY = 0;
		levelTileHeight = tileMap.getHeight()/tileMap.getTileSize();
		
		
		
		hud = new HUD(player);
	}

	
	
	@Override
	public void update() {
		// update player
		player.update();
		
		// show tileMap accroding to play's coordinate 
		tileMap.setPosition(
				GamePanel.WIDTH / 2 - player.getX(),
				GamePanel.HEIGHT / 2 - player.getY()
		);
		
		// set background ( or let background scroll while player move)
		bg.setPosition(tileMap.getX(), tileMap.getY());
		
		
		// attack enemies
		player.checkAttack(enemies);
		
		// chcek items
		player.checkTouch(items);
		
		// update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(
						// 因為 e已經不在enemies中所以不會被畫出來，但e仍存在所以可以執行methods
						new Explosion(e.getX(),e.getY())
				);
			}
		}
		
		// update all items
		for(int i = 0; i< items.size(); i++) {
			items.get(i).update();
			if(items.get(i).shouldRemove()) {
				items.remove(i);
				i--;
			}
		}
		
		// update all explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		
		//check all blocks
		checkBlockType();
		
	}
	
	private void checkBlockType() {
		
		//check function block
		int functionBlockBottom = tileMap.getFunctionBlock(player.getY()/tileMap.getTileSize(), player.getX()/tileMap.getTileSize());
		int functionBlockHead = tileMap.getFunctionBlock(player.getY()/tileMap.getTileSize()-1, player.getX()/tileMap.getTileSize());
		if(functionBlockBottom == FunctionBlockType.COIN || functionBlockHead == FunctionBlockType.COIN){
			//coin
			System.out.println("coin!!");
		}
		else if(functionBlockBottom == FunctionBlockType.COIN2 || functionBlockHead == FunctionBlockType.COIN2){
			//silver coin
			System.out.println("more coin!!");
		}
		else if(functionBlockBottom == FunctionBlockType.VINE || functionBlockHead == FunctionBlockType.VINE){
			//vine
			System.out.println("I want to climb it!!");
		}
		
		//check hazard block
		int hazardBlockBottom = tileMap.getHazardBlock(player.getY()/tileMap.getTileSize(), player.getX()/tileMap.getTileSize());
		int hazardBlockHead = tileMap.getHazardBlock(player.getY()/tileMap.getTileSize()-1, player.getX()/tileMap.getTileSize());
		if(hazardBlockBottom == HazardBlockType.WATER || hazardBlockHead == HazardBlockType.WATER){
			//water
			System.out.println("I hate water!!");
		}
		else if(hazardBlockBottom == HazardBlockType.DIRTY_WATER || hazardBlockHead == HazardBlockType.DIRTY_WATER){
			//dirty water
			System.out.println("Ewwwwww!! that stinks!!!");
		}
		else if(hazardBlockBottom == HazardBlockType.LAVA || hazardBlockHead == HazardBlockType.LAVA){
			//lava
			System.out.println("Hot Hot Hot!!");
		}
		else if(hazardBlockBottom == HazardBlockType.SPIKE || hazardBlockHead == HazardBlockType.SPIKE){
			//spike
			System.out.println("Ouch!! It hurts!!");
		}
	}

	@Override
	public void draw(Graphics2D g) {
		
		//draw background
		bg.draw(g);
		
		// draw tilemap
		tileMap.draw(g, levelTileY, levelTileHeight);
		
		// draw player
		player.draw(g);
		
		tileMap.drawTransParentBlock(g, levelTileY, levelTileHeight);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);		
		}
		
		// draw items
		for(int i = 0; i < items.size(); i++) {
			items.get(i).draw(g);
		}
		
		// draw explosion
		for(int i = 0; i < explosions.size(); i++) {
			
			// cuz no tileMap in explosion so we can not setMapPosition in Explosion class 
			explosions.get(i).setMapPosition((int)tileMap.getX(), (int)tileMap.getY());
			explosions.get(i).draw(g);
		}
		// draw hud
		hud.draw(g);
	}

	@Override
	public void keyPressed(int k) {
		if( k == KeyEvent.VK_LEFT) player.setLeft(true);
		if( k == KeyEvent.VK_RIGHT) player.setRight(true);
		if( k == KeyEvent.VK_UP) player.setUp(true);
		if( k == KeyEvent.VK_DOWN) player.setDown(true);
		if( k == KeyEvent.VK_W) player.setJumping(true);
		if( k == KeyEvent.VK_E) player.setGliding(true);
		if( k == KeyEvent.VK_R) player.setScratching();
		if( k == KeyEvent.VK_F) player.setFiring();
		if(k == KeyEvent.VK_ESCAPE) gsm.setStates(GameStateManager.MENUSTATE);
	}

	@Override
	public void keyReleased(int k) {
		if( k == KeyEvent.VK_LEFT) player.setLeft(false);
		if( k == KeyEvent.VK_RIGHT) player.setRight(false);
		if( k == KeyEvent.VK_UP) player.setUp(false);
		if( k == KeyEvent.VK_DOWN) player.setDown(false);
		if( k == KeyEvent.VK_W) player.setJumping(false);
		if( k == KeyEvent.VK_E) player.setGliding(false);
		if( k == KeyEvent.VK_R) player.setScratching();
		if( k == KeyEvent.VK_F) player.setFiring();
	}
}
