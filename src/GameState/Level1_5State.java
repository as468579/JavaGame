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
import Entity.Object.Enemies.Slugger;
import Entity.Object.Enemies.Snake;
import Entity.Object.Items.Bomb;
import Entity.Object.Items.Coin;
import Entity.Object.Items.Treasurebox;
import Entity.Object.Enemy;
import Entity.Object.Item;
import Main.GamePanel;
import TileMap.Background;

public class Level1_5State extends LevelState {

	public Level1_5State(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		super.init();
		System.out.println("level 1-5");

		levelTileY = 128;
		levelTileHeight = 32;

		bg = new Background("/Backgrounds/background_1_5.gif", 0.5);
		
		// background music
		levelName = "level5";
		AudioPlayer.load("/Music/level5Music.mp3", levelName, AudioPlayer.BGMUSIC);
		AudioPlayer.setVolume();
		
		dialogFrame = new Dialog();
		
		enemies = new ArrayList<Enemy>();
		explosions = new ArrayList<Explosion>();
		items = new ArrayList<Item>();
		
		populateEnemies();
		populateItems();
		
		// load subtitle
		try {
			subtitleImg = ImageIO.read(
				getClass().getResourceAsStream("/HUD/level5.png")
			);
			subtitle = new Title(subtitleImg);
			subtitle.setY(85);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		currentDialog = 0;
		
		shakeSize = 10;
		
		//check player is flying up
		if(PlayerSave.isFlying()) player.setPosition(200, 2460);
	}

	private void populateEnemies() {

		// populate enemy
		enemies.clear();

		Slugger sl;
		Alligator a;
		Snake sn;
		Point[] posSnake = new Point[] {new Point(710, 2240), };
		Point[] posAlligator = new Point[] { new Point(1510, 2160), };
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
		Point[] posCoin = new Point[] { new Point(2735, 2460), new Point(2780, 2360), new Point(650, 2395),};
		Point[] posBomb = new Point[] { new Point(1760, 2230), new Point(2630, 2150), new Point(890, 2310),
				new Point(30, 2180), new Point(370, 2180),};
		Point[] posTreasure = new Point[] {new Point(155, 2210), };

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
			dialogFrame.setDialog1_5(new String[] {});
			PlayerSave.setLvl1_5(true);
			// eventFinish
			eventFinish(GameStateManager.LEVEL1_4STATE);
		}

		if (player.getY() > (levelTileY + levelTileHeight) * 16) {
			dialogFrame.setDialog1_5(new String[] {});
			PlayerSave.setLvl1_5(true);
			// eventFinish
			eventFinish(GameStateManager.LEVEL1_6STATE);
		}
	}
	
	public void reset() {

		if(!player.hasWings()) player.setPosition(1375, 2150); 
		else player.setPosition(200, 2460);	//get wings
	
		populateEnemies();
		super.reset();
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		//run dialog
		if(!PlayerSave.enteredLvl1_5())
			story();
		else {
			dialogFrame.setDialog1_5(new String[] {});
			dialogFrame.end();
		}
	}
	
	private void story() {
		dialogFrame.start();
		String[] dialog1_5 = dialogFrame.getDialog1_5();
		int storyLength = dialog1_5.length;
		if(currentDialog < storyLength) {
			if(currentDialog == 3) dialogFrame.setColor(new Color(255, 0, 0));
			else dialogFrame.setColor(new Color(255, 255, 255));
			
			dialogFrame.setContent(dialog1_5[currentDialog]);
		}
		else {
			dialogFrame.end();
			dialogFrame.setDialog1_5(new String[] {});
		}
	}

	@Override
	public void keyPressed(int k) {
		super.keyPressed(k);
		if( k == KeyEvent.VK_X) {
			currentDialog++;
		}
	}

	@Override
	public void keyReleased(int k) {
		super.keyReleased(k);
	}
}
