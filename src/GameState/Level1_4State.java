package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Audio.AudioPlayer;
import Entity.Dialog;
import Entity.Enemy;
import Entity.Explosion;
import Entity.Item;
import Entity.PlayerSave;
import Entity.Enemies.Alligator;
import Entity.Enemies.Slugger;
import Entity.Enemies.Snake;
import Entity.Items.Bomb;
import Entity.Items.Coin;
import Entity.Items.Treasurebox;
import Main.GamePanel;
import TileMap.Background;

public class Level1_4State extends LevelState {
	
	private int currentDialog2;

	public Level1_4State(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		super.init();
		System.out.println("level 1-4");

		levelTileY = 96;
		levelTileHeight = 32;

		bg = new Background("/Backgrounds/background_1_4.gif", 0.5);
		
		// background music
		bgMusic = new AudioPlayer("/Music/Level1Music.mp3");
		
		dialogFrame = new Dialog();
	
		enemies = new ArrayList<Enemy>();
		explosions = new ArrayList<Explosion>();
		items = new ArrayList<Item>();
		
		populateEnemies();
		populateItems();
		
		currentDialog = 0;
		currentDialog2 = 0;
		
		shakeSize = 20;
		
		//check player is flying up
		if(PlayerSave.isFlying()) player.setPosition(1300, 1900);
	}

	private void populateEnemies() {

		// populate enemy
		enemies.clear();

		Slugger sl;
		Alligator a;
		Snake sn;
		Point[] posSnake = new Point[] { new Point(1750, 1800), new Point(2000, 1650), };
		Point[] posAlligator = new Point[] {new Point(1840, 1800), new Point(2000, 1800), new Point(300, 1920), new Point(400, 1920), 
				new Point(500, 1920), new Point(600, 1920), new Point(700, 1920), new Point(800, 1920), };
		Point[] posSlugger = new Point[] {};

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
		Point[] posCoin = new Point[] { new Point(2240, 1635), new Point(2280, 1635), new Point(2640, 1920), new Point(2700, 1920),
				new Point(2080, 1680), new Point(875, 1690),};
		Point[] posBomb = new Point[] {};
		Point[] posTreasure = new Point[] { new Point(2205, 1635), new Point(2750, 1920),};

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

	@Override
	public void update() {
		super.update();

		if (player.getY() < (levelTileY-1) * 16) {
			dialogFrame.setDialog1_4(new String[] {});
			PlayerSave.setLvl1_4(true);
			// eventFinish
			eventFinish(GameStateManager.LEVEL1_3STATE);
		}

		if (player.getY() > (levelTileY + levelTileHeight) * 16) {
			PlayerSave.setLvl1_4(true);
			// eventFinish
			eventFinish(GameStateManager.LEVEL1_5STATE);
		}
	}
	
	public void reset() {

		if(!player.hasWings()) player.setPosition(2360, 1900); 
		else player.setPosition(1300, 1900);	//get wings
		
		populateEnemies();
		
		super.reset();
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		//run dialog
		if(!PlayerSave.enteredLvl1_4()) {
			if(!tileMap.isShaking()) story();
			else storyEscape();
		}
		else {
			dialogFrame.end();
		}
		
		
	}
	
	private void story() {
		dialogFrame.start();
		String[] dialog1_4 = dialogFrame.getDialog1_4();
		int storyLength = dialog1_4.length;
		if(currentDialog < storyLength) {
			if(currentDialog == 0) dialogFrame.setColor(new Color(0, 100, 255));
			else dialogFrame.setColor(new Color(0, 0, 0));
			
			if(currentDialog == 2) {
				dialogFrame.setFont(new Font("Microsoft JhengHei", Font.BOLD, 30));
				dialogFrame.setContentY(170);
			}
			else {
				dialogFrame.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 15));
				dialogFrame.setContentY(155);
			}
			
			dialogFrame.setContent(dialog1_4[currentDialog]);
		}
		else {
			dialogFrame.end();
			dialogFrame.setDialog1_4(new String[] {});
		}
	}
	
	private void storyEscape() {
		dialogFrame.start();
		String[] dialog1_4_1 = dialogFrame.getDialog1_4_1();
		int storyLength = dialog1_4_1.length;
		if(currentDialog2 < storyLength) {
			dialogFrame.setContent(dialog1_4_1[currentDialog2]);
		}
		else {
			dialogFrame.end();
			dialogFrame.setDialog1_4_1(new String[] {});
		}
	}

	@Override
	public void keyPressed(int k) {
		super.keyPressed(k);
		if( k == KeyEvent.VK_SPACE) {
			currentDialog++;
		}
		if( tileMap.isShaking() && k == KeyEvent.VK_SPACE) {
			currentDialog2++;
		}
	}

	@Override
	public void keyReleased(int k) {
		super.keyReleased(k);
	}
}
