package GameState;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;

import Entity.Explosion;
import Entity.HUD;
import Entity.MapObject;
import Entity.PlayerSave;
import Entity.Title;
import Entity.Dialog;
import Entity.Object.Enemies.Alligator;
import Entity.Object.Enemies.DarkKnight;
import Entity.Object.Enemies.Slugger;
import Entity.Object.Enemies.Snake;
import Entity.Object.Enemies.Thief;
import Entity.Object.Items.Bomb;
import Entity.Object.Items.Treasurebox;
import Entity.Object.Enemy;
import Entity.Object.Item;
import Entity.Object.Player;
import Main.GamePanel;
import TileMap.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
public abstract class LevelState extends GameState{
	
	protected TileMap tileMap;
	protected Player player;
	protected HUD hud;
	protected Dialog dialogFrame;
	
	protected Background bg;
	
	protected ArrayList<Enemy> enemies;//
	protected ArrayList<Explosion> explosions;//
	protected ArrayList<Item> items;//
	
	protected int levelTileY;
	protected int levelTileHeight;
	
	// events
	protected boolean blockInput = false;
	protected int eventCount = 0;
	protected boolean eventStart;
	protected ArrayList<Rectangle>tb;
	protected boolean eventFinish;
	protected boolean eventDead;
	
	//story
	protected Font basicFont;
	protected Font storyFont;
	protected Color basicColor;
	protected Color storyColor;
	protected boolean storyPart[];
	protected int currentStory = 0;
	protected int currentDialog = 0;
	
	// title
	private Title title;
	protected Title subtitle;
	private BufferedImage titleImg;
	protected BufferedImage subtitleImg;
	
	//map shaking
	protected int shakeSize = 0;
	
	// main role
	protected MapObject mainRole;
	
	protected String levelName;
	
	
	public LevelState(GameStateManager gsm){
		this.gsm = gsm;
		init();
	}

	@Override
	public void init() {
		
		// tileMap
		tileMap = gsm.getLoadState().getTileMap();
		
		// player
		player = new Player(tileMap);
		player.setHealth(PlayerSave.getHealth());
		player.setMoney(PlayerSave.getMoney());
		player.setWings(PlayerSave.hasWings());
		player.setShield(PlayerSave.hasShield());
		
		player.setPosition(PlayerSave.getX(), PlayerSave.getY());
		// player.setTime(PlayerSave.getTime());
		
		// set main role
		setFocus(player);
		
		// hud
		hud = new HUD(player);
	
		
		// load title
		try {
			titleImg = ImageIO.read(
				getClass().getResourceAsStream("/HUD/title.png")
			);
			title = new Title(titleImg);
			title.setY(60);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		// transition box
		tb = new ArrayList<Rectangle>();
		
		// event start 
		eventCount = 0;
		eventStart = true;
	}

	public void setFocus(MapObject mo) {
		mainRole = mo;
	}
	
	@Override
	public void update() {
		
		// check if player dead event should start
		if(player.getHealth() == 0 || player.getY() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}
		
		// play event
		if(eventStart) eventStart();
		else if(eventDead) eventDead();
		
		// update player
		player.update();
		
		// move title and subtitle
		if(title != null) {
			title.update();
			if(title.shouldRemove()) title = null;
		}
		
		if(subtitle != null) {
			subtitle.update();
			if(subtitle.shouldRemove()) subtitle = null;
		}
		
		tileMap.setPosition(
				GamePanel.WIDTH / 2 - mainRole.getX(),
				GamePanel.HEIGHT / 2 - mainRole.getY()
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
			if(e.shouldRemove()) {
				if(
					!(e instanceof DarkKnight) &&
					!(e instanceof Thief)
				) {
					explosions.add(
							new Explosion(e.getX(),e.getY())
					);
				}
				enemies.remove(i);
				i--;
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
		
		if(tileMap.isShaking()) tileMap.setShake(shakeSize);
		
	}
	
	
	@Override
	public void draw(Graphics2D g) {
		//TODO
//		System.out.println("x:"+player.getX()+" y:"+player.getY());
		
		//draw background
		bg.draw(g);
		
		// draw tilemap
		tileMap.draw(g, levelTileY, levelTileHeight);
		
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
		
		//draw transparent block
		tileMap.drawTransparentBlocks(g, levelTileY, levelTileHeight);
		
		// draw hud
		hud.draw(g);
		
		// draw title
		if(title != null) title.draw(g);
		if(subtitle != null) subtitle.draw(g);
		
		dialogFrame.draw(g);
		
		// draw transition boxes
		g.setColor(Color.BLACK);
		if(tb == null) System.out.println(this);
		for(Rectangle r : tb) {
			g.fill(r);
		}
		
	}

	@Override
	public void keyPressed(int k) {
		if(!blockInput) {
			if( k == KeyEvent.VK_LEFT) player.setLeft(true);
			if( k == KeyEvent.VK_RIGHT) player.setRight(true);
			if( k == KeyEvent.VK_UP) player.setUp(true);
			if( k == KeyEvent.VK_DOWN) player.setDown(true);
			if( k == KeyEvent.VK_SPACE) player.setJumping(true);
			if( k == KeyEvent.VK_W) player.setGliding(true);
			if( k == KeyEvent.VK_E) player.setScratching();
			if( k == KeyEvent.VK_R) player.setFiring();
			if( k == KeyEvent.VK_F) player.setDefending(true);
			if(k == KeyEvent.VK_ESCAPE) {
				eventFinish(GameStateManager.MENUSTATE);
			}
		}
	}

	@Override
	public void keyReleased(int k) {
		if(!blockInput) {
			if( k == KeyEvent.VK_LEFT) player.setLeft(false);
			if( k == KeyEvent.VK_RIGHT) player.setRight(false);
			if( k == KeyEvent.VK_UP) player.setUp(false);
			if( k == KeyEvent.VK_DOWN) player.setDown(false);
			if( k == KeyEvent.VK_SPACE) player.setJumping(false);
			if( k == KeyEvent.VK_W) player.setGliding(false);
			if( k == KeyEvent.VK_F) player.setDefending(false);
		}
	}
	
	// reset level
	public void reset(){	
		player.reset();
		blockInput = true;
		eventCount = 0;
		eventStart = true;
		
		title = new Title(titleImg);
		title.setY(60);
		subtitle = new Title(subtitleImg);
		subtitle.setY(85);
	}
	
	// level started
	public void eventStart() {
		eventCount++;
		if(eventCount == 1) {
			AudioPlayer.loop(levelName, AudioPlayer.BGMUSIC);
			tb.clear(); // remove all elements in tb [Note] clear() is faster than removeAll()
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			tb.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
		}
		if(eventCount > 1 && eventCount < 60) {
			tb.get(0).height -= 16;
			tb.get(1).width -= 24;
			tb.get(2).y += 16;
			tb.get(3).x += 24;
		}
		if(eventCount == 30) { title.begin(); }
		if(eventCount == 60) {
			eventStart = blockInput = false;
			subtitle.begin();
			eventCount = 0;
			tb.clear(); // remove all elements in the ArrayList
		}
	}
		
	// player has died
	public void eventDead() {
		eventCount++;
		if(eventCount == 1) {
			player.setDead();
			player.stop();
			AudioPlayer.stop(levelName, AudioPlayer.BGMUSIC);
		}
		else if(eventCount == 60) {
			tb.clear();
			tb.add(new Rectangle
				(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		}
		else if(eventCount >= 60) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
		}
		if(eventCount >= 120) {
			eventDead = blockInput = false;
			eventCount = 0;
			reset();
		}
	}
	
	// finish game
	public void eventFinish(int nextState) {
		
		// Save player status
		PlayerSave.setHealth(player.getHealth());
		PlayerSave.setMoney(player.getMoney());
		PlayerSave.setWings(player.hasWings());
		PlayerSave.setShield(player.hasShield());
		
		PlayerSave.setX(player.getX());
		PlayerSave.setY(player.getY());
		//PlayerSave.setTime(player.getTime());
		
		PlayerSave.setFlying(player.isUp());
		
		AudioPlayer.close(levelName, AudioPlayer.BGMUSIC);
		gsm.setStates(nextState);
		
	}
		
	
	public boolean hasAnotherStoryPart(boolean[] storyPart, int partNumber) {
		System.out.println(storyPart+" "+currentStory);
		
		storyPart[currentStory] = false;
		currentStory++;
		if(currentStory == partNumber) return false;
		storyPart[currentStory] = true;
		return true;
	}
	
	
}

