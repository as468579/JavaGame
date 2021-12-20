package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import Audio.AudioPlayer;
import Entity.Dialog;
import Entity.Enemy;
import Entity.Explosion;
import Entity.Item;
import Entity.Player;
import Entity.PlayerSave;
import Entity.Enemies.Alligator;
import Entity.Enemies.Bat;
import Entity.Enemies.DarkKnight;
import Entity.Enemies.IronCannon;
import Entity.Enemies.Slugger;
import Entity.Enemies.Snake;
import Entity.Items.Bomb;
import Entity.Items.Coin;
import Entity.Items.Treasurebox;
import TileMap.Background;

public class Level1_1State extends LevelState {

	private Rectangle story;
	private boolean firstTimeRun = true;
	
	public Level1_1State(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		super.init();
		
		System.out.println("level 1-1");

		levelTileY = 0;
		levelTileHeight = 32;
		
		// set background image
		bg = new Background("/Backgrounds/background_1_1.gif", 0.5);
		
		// background music
		bgMusic = new AudioPlayer("/Music/Level1Music.mp3");
				
		dialogFrame = new Dialog();
				
		enemies = new ArrayList<Enemy>();
		items = new ArrayList<Item>();
		explosions = new ArrayList<Explosion>();
		
		populateEnemies();
		populateItems();
		populateStory();
		
		currentDialog = 0;
		
		shakeSize = 30;
		
		//TODO
//		player.setMoney(19999);
//		player.setWings(true);
	}

	private void populateEnemies() {

		// populate enemy
		enemies.clear();

		Slugger sl;
		Alligator a;
		Snake sn;
		
		Point[] posSnake = new Point[] {};
		Point[] posAlligator = new Point[] {};
		Point[] posSlugger = new Point[] { new Point(1625, 370) };

 		if(tileMap == null) System.exit(0);
 		
		//setFocus(b);
		
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

	private void populateItems() {

		// populate items
		items.clear();
		
		Treasurebox tr;
		Bomb b;
		Coin c;

		Point[] posCoin = new Point[] { new Point(1625, 275), };
		Point[] posBomb = new Point[] {};
		Point[] posTreasure = new Point[] {new Point(2450, 35), };
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
		for (int i = 0; i < posTreasure.length; i++) {
			tr = new Treasurebox(tileMap);
			tr.setPosition(posTreasure[i].x, posTreasure[i].y);
			items.add(tr);
		}
		
	}

	private void populateStory() {

		// add story for Story1_2State
		story = new Rectangle(2200, 100, 400, 300);
	}

	@Override
	public void update() {
		super.update();

		// story check
		if (story.contains(player.getX(), player.getY())) { // enter story 2
			dialogFrame.setDialog1_1(new String[] {});
			PlayerSave.setLvl1_1(true);
			// eventFinish
			eventFinish(GameStateManager.STORY1_2STATE);
		}

		// position check
		if (player.getY() > (levelTileY + levelTileHeight) * 16) {
			PlayerSave.setLvl1_1(true);
			// eventFinish
			eventFinish(GameStateManager.LEVEL1_2STATE);
		}
		
	}
	
	public void reset() {
	
		if(!player.hasWings()) player.setPosition(100, 200); 
		else player.setPosition(2450, 200);	//get wings
		
		populateEnemies();
		
		super.reset();
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		
		//run dialog
		if(!PlayerSave.enteredLvl1_1()) 
			story();
		else {
			dialogFrame.end();
		}
		
	}
	
	private void story() {
		dialogFrame.start();
		String[] dialog1_1 = dialogFrame.getDialog1_1();
		int storyLength = dialog1_1.length;
		dialogFrame.setColor(new Color(0, 0, 0));
		if(currentDialog < storyLength) {
			dialogFrame.setContent(dialog1_1[currentDialog]);
		}
		else {
			dialogFrame.end();
			dialogFrame.setDialog1_1(new String[] {});
		}
	}

	@Override
	public void keyPressed(int k) {
		super.keyPressed(k);
		if( k == KeyEvent.VK_SPACE) {
			currentDialog++;
		}
	}

	@Override
	public void keyReleased(int k) {
		super.keyReleased(k);
	}
}
