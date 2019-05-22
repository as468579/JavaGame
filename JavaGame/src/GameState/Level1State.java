package GameState;

import java.awt.Graphics2D;
import java.awt.Point;

import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Entity.Enemies.Slugger;
import Main.GamePanel;
import TileMap.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level1State extends GameState{
	
	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	
	public Level1State(GameStateManager gsm){
		this.gsm = gsm;
		init();
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
		
		bg = new Background("/Backgrounds/grassbg1.gif",0.5);
		player = new Player(tileMap);
		player.setPosition(100, 100);
		tileMap.setTween(0.07);
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
	}

	private void populateEnemies() {
		
		// populate enemy
		enemies = new ArrayList<Enemy>();
		
		Slugger s;
		Point[] points = new Point[] {
			new Point(200, 200),
			new Point(860, 200),
			new Point(1525, 200),
			new Point(1680, 200),
			new Point(1800, 200)
		};
		
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
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
		
		// update all explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		// draw tilemap
		tileMap.draw(g);
		
		// draw player
		player.draw(g);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);		
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
