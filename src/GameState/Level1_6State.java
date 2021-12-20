package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import Entity.Dialog;
import Entity.Explosion;
import Entity.PlayerSave;
import Entity.Title;
import Entity.Object.Enemies.Alligator;
import Entity.Object.Enemies.DarkKnight;
import Entity.Object.Enemies.IronCannon;
import Entity.Object.Enemies.Slugger;
import Entity.Object.Enemies.Snake;
import Entity.Object.Enemies.Thief;
import Entity.Object.Items.Bomb;
import Entity.Object.Items.Coin;
import Entity.Object.Items.Treasurebox;
import Entity.Object.Items.Wings;
import Entity.Object.Enemy;
import Entity.Object.Item;
import Main.GamePanel;
import TileMap.Background;

public class Level1_6State extends LevelState {

	private int currentDialog2;
	private int currentDialog3;
	
	private DarkKnight dk;
	private Thief t;
	private boolean meetBoss;
	private boolean bossStoryEnd;
	private boolean wingsStoryEnd;

	public Level1_6State(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		super.init();
		
		System.out.println("level 1-6");

		levelTileY = 160;
		levelTileHeight = 32;

		bg = new Background("/Backgrounds/background_1_6.gif", 0.5);
		
		// background music
		levelName = "level6";
		AudioPlayer.load("/Music/level6Music.mp3", levelName, AudioPlayer.BGMUSIC);
		AudioPlayer.setVolume();
		
		dialogFrame = new Dialog();
		
		enemies = new ArrayList<Enemy>();
		explosions = new ArrayList<Explosion>();
		items = new ArrayList<Item>();
		
		populateEnemies();
		populateItems();
		if(player.hasWings()) {
			populateEnemies2Wave();
			populateBoss();
		}
		
		
		// load subtitle
		try {
			subtitleImg = ImageIO.read(
				getClass().getResourceAsStream("/HUD/level6.png")
			);
			subtitle = new Title(subtitleImg);
			subtitle.setY(85);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		if(player.hasWings()) {
			populateEnemies2Wave();
			populateRealBoss();
		}
		meetBoss = false;
		bossStoryEnd = false;
		wingsStoryEnd = false;
		currentDialog2 = 0;
		currentDialog3 = 0;
		
		shakeSize = 10;
		
		//check player is flying up
		if(PlayerSave.isFlying()) player.setPosition(2000, 2800);
	}

	private void populateEnemies() {

		// populate enemy
		enemies.clear();

		Alligator a;
		Snake sn;
		IronCannon ic;
		
		Point[] posSnake = new Point[] {};
		Point[] posAlligator = new Point[] {new Point(380, 2940), };
		Point[] posIronCannon = new Point[] {new Point(200, 2895), new Point(200, 2831), new Point(200, 2767), };
		Point posDarkKnight = new Point(1400, 2910);
		
		for (int i = 0; i < posSnake.length; i++) {
			sn = new Snake(tileMap);
			sn.setPosition(posSnake[i].x, posSnake[i].y);
			enemies.add(sn);
		}
		for (int i = 0; i < posAlligator.length; i++) {
			a = new Alligator(tileMap);
			a.setPosition(posAlligator[i].x, posAlligator[i].y);
			enemies.add(a);
		}
		for (int i = 0; i < posIronCannon.length; i++) {
			ic = new IronCannon(tileMap);
			ic.setPosition(posIronCannon[i].x, posIronCannon[i].y);
			enemies.add(ic);
		}
		
		dk = new DarkKnight(tileMap);
		dk.setPosition(posDarkKnight.x, posDarkKnight.y);
		
	}

	private void populateItems() {

		// populate items
		items.clear();

		Wings w;
		Bomb b;
		Coin c;
		
		
		Point[] posCoin = new Point[] { new Point(2545, 2950), };
		Point[] posBomb = new Point[] {};
		Point posWings = new Point(2030, 2770);

		for (int i = 0; i < posCoin.length; i++) {
			c = new Coin(tileMap);
			c.setPosition(posCoin[i].x, posCoin[i].y);
			items.add(c);
		}
		for (int i = 0; i < posBomb.length; i++) {
			b = new Bomb(tileMap);
			b.setPosition(posBomb[i].x, posBomb[i].y);
			items.add(b);
		}
		w = new Wings(tileMap);
		w.setPosition(posWings.x, posWings.y);
		items.add(w);
	}
	
	private void populateEnemies2Wave() {

		// populate enemy
		Slugger sl;
		Alligator a;
		Snake sn;
		Point[] posSnake = new Point[] {};
		Point[] posAlligator = new Point[] {};
		Point[] posSlugger = new Point[] {new Point(700, 2930), new Point(500, 2930), new Point(300, 2930), };

		for (int i = 0; i < posSnake.length; i++) {
			sn = new Snake(tileMap);
			sn.setPosition(posSnake[i].x, posSnake[i].y);
			enemies.add(sn);
		}
		for (int i = 0; i < posAlligator.length; i++) {
			a = new Alligator(tileMap);
			a.setPosition(posAlligator[i].x, posAlligator[i].y);
			enemies.add(a);
		}
		for (int i = 0; i < posSlugger.length; i++) {
			sl = new Slugger(tileMap);
			sl.setPosition(posSlugger[i].x, posSlugger[i].y);
			enemies.add(sl);
		}
	}
	
	private void populateRealBoss() {
		Point posThief = new Point(1500, 2910);
		t = new Thief(tileMap);
		t.setPosition(posThief.x, posThief.y);
		enemies.add(t);
	}
	
	private void populateBoss() {
		Point posDarkKnight = new Point(1500, 2910);
		dk = new DarkKnight(tileMap);
		dk.setPosition(posDarkKnight.x, posDarkKnight.y);
		enemies.add(dk);
	}
	
	public void reset() {
		// reset enemies
		populateEnemies();
		
		if(player.hasWings()) { // get wings
			populateEnemies2Wave();
			populateBoss();
			player.setPosition(2000, 2800);
		}
		else { 
			if(meetBoss) { // meet boss
				player.setPosition(PlayerSave.getX(), PlayerSave.getY());
				populateRealBoss();
			}
			else 
				player.setPosition(130, 2700); 
			
		}

		super.reset();
	}
	
	@Override 
	public void eventStart() {
		if(meetBoss) {
			if(player.hasWings()) {
				setFocus(dk);
			}else {
				setFocus(t);
			}
		}
		super.eventStart();
		if(eventStart == false)
			setFocus(player);
	}

	@Override
	public void update() {
		super.update();

		if (player.getY() < (levelTileY-1) * 16) {
			PlayerSave.setLvl1_6(true);
			
			// eventFinish
			eventFinish(GameStateManager.LEVEL1_5STATE);
		}

		if (player.getY() > (levelTileY + levelTileHeight) * 16) {
			PlayerSave.setLvl1_6(true);
			
			// eventFinish
			eventFinish(GameStateManager.LEVEL1_7STATE);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		// set once
		if(player.getX() > 1160 && !meetBoss && !tileMap.isShaking()) { //Boss appeared!!!
			PlayerSave.setX(1040);
			PlayerSave.setY(2930);
			populateRealBoss();
			setFocus(t);
			meetBoss = true;
		}
		
		//run dialog
		if(!PlayerSave.enteredLvl1_6()) {
			if(meetBoss && !bossStoryEnd) { //Boss appeared!!!
				storyBoss();
			}
			else story1();
			
			//TODO
			// get wings
		
			if(player.hasWings() && !wingsStoryEnd) {
				storyEscape();
				player.setWings(true);
			}
		}
		else {
			dialogFrame.setDialog1_6(new String[] {});
			dialogFrame.setDialog1_6_1(new String[] {});
			dialogFrame.setDialog1_6_2(new String[] {});
			dialogFrame.end();
		}
	}
	
	public void story1() {
		dialogFrame.start();
		String[] dialog1_6 = dialogFrame.getDialog1_6();
		int storyLength = dialog1_6.length;
		dialogFrame.setColor(new Color(0, 0, 0));
		if(currentDialog < storyLength) {
			dialogFrame.setContent(dialog1_6[currentDialog]);
		}
		else {
			dialogFrame.end();
			dialogFrame.setDialog1_6(new String[] {});
		}
	}

	public void storyBoss() {
		dialogFrame.start();
		String[] dialog1_6_1 = dialogFrame.getDialog1_6_1();
		int storyLength = dialog1_6_1.length;
		if(currentDialog2 < storyLength) {
			dialogFrame.setContent(dialog1_6_1[currentDialog2]);
		}
		else {
			dialogFrame.end();
			dialogFrame.setDialog1_6_1(new String[] {});
			setFocus(player);
			bossStoryEnd = true;
		}
	}
	
	public void storyEscape() {
		dialogFrame.setDialog1_6(new String[] {});
		dialogFrame.setDialog1_6_1(new String[] {});
		dialogFrame.start();
		String[] dialog1_6_2 = dialogFrame.getDialog1_6_2();
		int storyLength = dialog1_6_2.length;
		dialogFrame.setColor(new Color(0, 0, 0));
		if(currentDialog3 < storyLength) {
			//player can not moving while entering story
			blockInput = true;
			player.stop();
			
			// start shaking map
			if(currentDialog3 >= 2) tileMap.setShaking(true);
			dialogFrame.setContent(dialog1_6_2[currentDialog3]);
		}
		else {
			dialogFrame.end();
			dialogFrame.setDialog1_6_2(new String[] {});
			wingsStoryEnd = true;
			blockInput = false;
		}
	}
	

	@Override
	public void keyPressed(int k) {
		super.keyPressed(k);
		
		if( k == KeyEvent.VK_X && meetBoss && !bossStoryEnd) {
			currentDialog2++;
		}
		else if( k == KeyEvent.VK_X) {
			currentDialog++;
		}
		//TODO
		//get wings
		if( k == KeyEvent.VK_X && player.hasWings() && !wingsStoryEnd) {
			currentDialog3++;
		}
	}

	@Override
	public void keyReleased(int k) {
		super.keyReleased(k);
	}
}
