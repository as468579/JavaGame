package GameState;

import java.awt.Graphics2D;
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
public class Level1State extends GameState{
	
	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	private ArrayList<Item> items;
	
	private HUD hud;
	private AudioPlayer bgMusic;

	
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
		tileMap.setTween(0.07);
		
		bg = new Background("/Backgrounds/grassbg1.gif",0.5);
		player = new Player(tileMap);
		player.setPosition(100, 100);
		
		populateEnemies();
		populateItems();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		
		bgMusic = new AudioPlayer("/Music/level1-1.mp3");
		bgMusic.play();
	}

	private void populateEnemies() {
		
		// populate enemy
		enemies = new ArrayList<Enemy>();
		
		Slugger sl;
		Alligator a;
		Snake sn;
		Point[] points = new Point[] {
			new Point(200, 200),
		};
		
		for(int i = 0; i < points.length; i++) {
			a = new Alligator(tileMap);
			a.setPosition(points[i].x, points[i].y);
			enemies.add(a);
		}
		for(int i = 0; i < points.length; i++) {
			sn = new Snake(tileMap);
			sn.setPosition(points[i].x, points[i].y);
			enemies.add(sn);
		}
	}
	
	private void populateItems() {
		
		// populate items
		items = new ArrayList<Item>();
		
		Treasurebox tr;
		Bomb b;
		Point[] points = new Point[] {
			new Point(170, 200),
		};
		
		for(int i = 0; i < points.length; i++) {
			b = new Bomb(tileMap);
			b.setPosition(points[i].x, points[i].y);
			items.add(b);
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
