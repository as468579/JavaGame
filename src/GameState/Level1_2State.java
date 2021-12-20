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
import Entity.Object.Enemies.Bat;
import Entity.Object.Enemies.Slugger;
import Entity.Object.Enemies.Snake;
import Entity.Object.Items.Bomb;
import Entity.Object.Items.Coin;
import Entity.Object.Items.Treasurebox;
import Entity.Object.Enemy;
import Entity.Object.Item;
import Main.GamePanel;
import TileMap.Background;

public class Level1_2State extends LevelState {

	private int currentDialog2 = 0;
	
	public Level1_2State(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		super.init();
		System.out.println("level 1-2");

		levelTileY = 32;
		levelTileHeight = 32;
		
		bg = new Background("/Backgrounds/background_1_2.gif", 0.5);
		
		// background music
		levelName = "level2";
		AudioPlayer.load("/Music/level2Music.mp3", levelName, AudioPlayer.BGMUSIC);
		AudioPlayer.setVolume();
		
		dialogFrame = new Dialog();

		enemies = new ArrayList<Enemy>();
		items = new ArrayList<Item>();
		explosions = new ArrayList<Explosion>();
		
		populateEnemies();
		populateItems();
		if(player.hasWings()) populateEnemies2Wave();
		
		// load subtitle
		try {
			subtitleImg = ImageIO.read(
				getClass().getResourceAsStream("/HUD/level2.png")
			);
			subtitle = new Title(subtitleImg);
			subtitle.setY(85);
		}catch(Exception e) {
			e.printStackTrace();
		}
		currentDialog = 0;
		
		shakeSize = 30;
		
		//check player is flying up
		if(PlayerSave.isFlying()) player.setPosition(60, 910);
	}

	private void populateEnemies() {

		// populate enemy
		enemies.clear();

		Slugger sl;
		Alligator a;
		Snake sn;
		Point[] posSnake = new Point[] { new Point(1630, 910), new Point(1500, 650), new Point(1690, 720), 
				new Point(1300, 850), new Point(1250, 850), new Point(1345, 850), new Point(1200, 850), };
		Point[] posAlligator = new Point[] {};
		Point[] posSlugger = new Point[] { new Point(330, 900), };

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
		Point[] posCoin = new Point[] { new Point(1420, 980), new Point(1390, 980), new Point(1360, 980), new Point(1800, 970),};
		Point[] posBomb = new Point[] { new Point(1090, 910), };
		Point[] posTreasure = new Point[] {new Point(1325, 980), };

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
		
		Point[] posBat = new Point[] { new Point(1575, 600),  new Point(1624, 616), new Point(1670, 632), new Point(1720, 648), };
		
		for (int i = 0; i < posBat.length; i++) {
			b = new Bat(tileMap);
			b.setPosition(posBat[i].x, posBat[i].y);
			enemies.add(b);
		}
		
	}

	@Override
	public void update() {
		super.update();

		// check enter story

		if (player.getY() < (levelTileY-1) * 16) {
			PlayerSave.setLvl1_2(true);
			tileMap.setShaking(false);
			// eventFinish
			if (player.getMoney() >= 20000) { // 達到真結局條件
				eventFinish(GameStateManager.ENDING_STATE_3);
			} else {
				eventFinish(GameStateManager.ENDING_STATE_1);
			}
		}

		if (player.getY() > (levelTileY + levelTileHeight) * 16) {
			PlayerSave.setLvl1_2(true);
			
			// eventFinish
			eventFinish(GameStateManager.LEVEL1_3STATE);
		}
	}
	
	public void reset() {

		if(!player.hasWings()) player.setPosition(2410, 910); 
		else player.setPosition(60, 910);	//get wings

		populateEnemies();
		if(player.hasWings()) populateEnemies2Wave();		// get wings
		
		super.reset();
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		//run dialog
		if(!PlayerSave.enteredLvl1_2()) {
			if(player.getX() < 1650) story2(g);
			else story1(g);
		}
		else {
			dialogFrame.setDialog1_2(new String[] {});
			dialogFrame.setDialog1_2_1(new String[] {});
			dialogFrame.end();
		}
	}
	
	public void story1(Graphics2D g) {
		dialogFrame.start();
		String[] dialog1_2 = dialogFrame.getDialog1_2();
		int storyLength = dialog1_2.length;
		if(currentDialog < storyLength) {
			if(currentDialog == 0) dialogFrame.setColor(new Color(255, 0, 0));
			else dialogFrame.setColor(new Color(0, 0, 0));
			dialogFrame.setContent(dialog1_2[currentDialog]);
		}
		else {
			dialogFrame.end();
			dialogFrame.setDialog1_2(new String[] {});
		}
	}

	public void story2(Graphics2D g) {
		dialogFrame.start();
		String[] dialog1_2_1 = dialogFrame.getDialog1_2_1();
		int storyLength = dialog1_2_1.length;
		if(currentDialog2 < storyLength) {
			dialogFrame.setContent(dialog1_2_1[currentDialog2]);
		}
		else {
			dialogFrame.end();
			dialogFrame.setDialog1_2_1(new String[] {});
		}
	}
	
	
	@Override
	public void keyPressed(int k) {
		super.keyPressed(k);
		if(player.getX() < 1650 && k == KeyEvent.VK_X ) {
			currentDialog2++;
		}
		else if( k == KeyEvent.VK_X) {
			currentDialog++;
		}
		
		
	}

	@Override
	public void keyReleased(int k) {
		super.keyReleased(k);
	}
	
	
}
