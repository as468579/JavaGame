package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Panel;
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
import Entity.Enemies.Bat;
import Entity.Enemies.IronCannon;
import Entity.Enemies.Slugger;
import Entity.Enemies.Snake;
import Entity.Items.Bomb;
import Entity.Items.Coin;
import Entity.Items.Treasurebox;
import Main.GamePanel;
import TileMap.Background;

public class Level1_3State extends LevelState {

	public Level1_3State(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		super.init();
		
		System.out.println("level 1-3");

		levelTileY = 64;
		levelTileHeight = 32;

		bg = new Background("/Backgrounds/background_1_3.gif", 0.5);

		// background music
		bgMusic = new AudioPlayer("/Music/Level1Music.mp3");
		
		dialogFrame = new Dialog();
		
		enemies = new ArrayList<Enemy>();
		items = new ArrayList<Item>();
		explosions = new ArrayList<Explosion>();
		
		populateEnemies();
		populateItems();
		if(player.hasWings()) populateEnemies2Wave();
		
		currentDialog = 0;
		
		shakeSize = 25;
		
		//check player is flying up
		if(PlayerSave.isFlying()) player.setPosition(2430, 1410);

	}

	private void populateEnemies() {

		// populate enemy
		enemies.clear();

		Slugger sl;
		Alligator a;
		Snake sn;
		Point[] posSnake = new Point[] { new Point(300, 1400), new Point(700, 1400), };
		Point[] posAlligator = new Point[] {new Point(820, 1400), new Point(2040, 1120), };
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
		Point[] posCoin = new Point[] { new Point(1340, 1190), new Point(200, 1055),};
		Point[] posBomb = new Point[] { };
		Point[] posTreasure = new Point[] {new Point(930, 1380), new Point(2080, 1360), };

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

	private void populateEnemies2Wave() {
		Bat b;
		IronCannon ic;
		Alligator a;
		
		Point[] posBat = new Point[] { new Point(150, 1400), new Point(390, 1200), new Point(660, 1300),};
		Point[] posIronCannon = new Point[] { new Point(840, 1100), };
		Point[] posAlligator = new Point[] { new Point(2180, 1150), new Point(2430, 1200), new Point(2180, 1250), 
				new Point(2430, 1300), new Point(2180, 1340), };
		
		for (int i = 0; i < posBat.length; i++) {
			b = new Bat(tileMap);
			b.setPosition(posBat[i].x, posBat[i].y);
			enemies.add(b);
		}
		for (int i = 0; i < posIronCannon.length; i++) {
			ic = new IronCannon(tileMap);
			ic.setPosition(posIronCannon[i].x, posIronCannon[i].y);
			enemies.add(ic);
		}
		for (int i = 0; i < posAlligator.length; i++) {
			a = new Alligator(tileMap);
			a.setPosition(posAlligator[i].x, posAlligator[i].y);
			enemies.add(a);
		}
		
	}
	
	
	@Override
	public void update() {
		super.update();

		if (player.getY() < (levelTileY-1) * 16) {
			PlayerSave.setLvl1_3(true);
			// eventFinish
			eventFinish(GameStateManager.LEVEL1_2STATE);
		}

		if (player.getY() > (levelTileY + levelTileHeight) * 16) {
			PlayerSave.setLvl1_3(true);
			// eventFinish
			eventFinish(GameStateManager.LEVEL1_4STATE);
		}
	}
	
	public void reset() {
		
		if(!player.hasWings()) player.setPosition(110, 1410); 
		else player.setPosition(2430, 1410);	//get wings
		
		
		populateEnemies();
		if(player.hasWings()) populateEnemies2Wave();	// get wings
		
		super.reset();
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		//run dialog
		if(!PlayerSave.enteredLvl1_3())
			story();
		else {
			dialogFrame.end();
		}
	}
	
	private void story() {
		dialogFrame.start();
		String[] dialog1_3 = dialogFrame.getDialog1_3();
		int storyLength = dialog1_3.length;
		dialogFrame.setColor(new Color(0, 0, 0));
		if(currentDialog < storyLength) {
			if(currentDialog == 2) {
				dialogFrame.setFont(new Font("Microsoft JhengHei", Font.BOLD, 28));
				dialogFrame.setContentY(170);
			}
			else {
				dialogFrame.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 15));
				dialogFrame.setContentY(155);
			}
			dialogFrame.setContent(dialog1_3[currentDialog]);
		}
		else {
			dialogFrame.end();
			dialogFrame.setDialog1_3(new String[] {});
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
