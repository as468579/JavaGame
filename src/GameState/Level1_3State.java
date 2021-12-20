package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Panel;
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
import Entity.Object.Enemies.DarkKnight;
import Entity.Object.Enemies.IronCannon;
import Entity.Object.Enemies.Slugger;
import Entity.Object.Enemies.Snake;
import Entity.Object.Items.Bomb;
import Entity.Object.Items.Coin;
import Entity.Object.Items.Shield;
import Entity.Object.Items.Treasurebox;
import Entity.Object.Enemy;
import Entity.Object.Item;
import Main.GamePanel;
import TileMap.Background;

public class Level1_3State extends LevelState {
	private int currentDialog2;
	private int currentDialog3;
	private DarkKnight dk;
	private boolean meetBoss;
	private boolean bossStoryEnd;
	private boolean shieldStoryEnd;

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
		levelName = "level3";
		AudioPlayer.load("/Music/level3Music.mp3", levelName, AudioPlayer.BGMUSIC);
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
				getClass().getResourceAsStream("/HUD/level3.png")
			);
			subtitle = new Title(subtitleImg);
			subtitle.setY(85);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		meetBoss = false;
		bossStoryEnd = false;
		shieldStoryEnd = false;
		currentDialog = 0;
		currentDialog2 = 0;
		currentDialog3 = 0;
		
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
		Point[] posAlligator = new Point[] {new Point(820, 1380), new Point(2040, 1100), };
		

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
		
	}
	
	private void populateBoss() {
		Point posDarkKnight = new Point(2080, 1330);
		dk = new DarkKnight(tileMap);
		dk.setPosition(posDarkKnight.x, posDarkKnight.y);
		enemies.add(dk);
	}

	private void populateItems() {

		// populate items
		items.clear();

		Treasurebox tr;
		Coin c;
		Shield sh;
		
		Point[] posCoin = new Point[] { new Point(1340, 1190), new Point(200, 1055),};
		Point[] posTreasure = new Point[] {new Point(930, 1380), };
		Point posShield = new Point(2270, 1360);

		for (int i = 0; i < posCoin.length; i++) {
			c = new Coin(tileMap);
			c.setPosition(posCoin[i].x, posCoin[i].y);
			items.add(c);
		}
		for (int i = 0; i < posTreasure.length; i++) {
			tr = new Treasurebox(tileMap);
			tr.setPosition(posTreasure[i].x, posTreasure[i].y);
			items.add(tr);
		}
		
		sh = new Shield(tileMap);
		sh.setPosition(posShield.x, posShield.y);
		if(!player.hasShield()) items.add(sh);
		
	}
	
	private void populateEnemies2Wave() {
		Bat b;
		IronCannon ic;
		Alligator a;
		
		Point[] posBat = new Point[] { new Point(150, 1400), new Point(390, 1200), new Point(660, 1300),};
		Point[] posIronCannon = new Point[] { new Point(840, 1100), };
		Point[] posAlligator = new Point[] { new Point(1950, 1100), new Point(2150, 1100), new Point(2250, 1100), };
		
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
		
		// reset enemies
		populateEnemies();
		if(meetBoss)populateBoss();
		
		if(player.hasWings()) {	// get wings
			populateEnemies2Wave();
			player.setPosition(2430, 1410);
		}
		else {
			if(meetBoss) { // meet boss
				player.setPosition(PlayerSave.getX(), PlayerSave.getY());
				populateBoss();
			}
			else {
				player.setPosition(110, 1410); 
			}
		}
		
		super.reset();
	}
	
	@Override 
	public void eventStart() {
		if(meetBoss) {
			setFocus(dk);
		}
		super.eventStart();
		if(eventStart == false)
			setFocus(player);
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		// set once
		if(player.getX() > 1900 && !meetBoss && !tileMap.isShaking()) { //Boss appeared!!!
			PlayerSave.setX(1900);
			PlayerSave.setY(1330);
			populateBoss();
			setFocus(dk);
			meetBoss = true;
		}
		
		//run dialog
		if(!PlayerSave.enteredLvl1_3()) {
			
			if(meetBoss && !bossStoryEnd) { //Boss appeared!!!
				storyBoss();
			}
			else {
				story();
			}
		}
		else {
			dialogFrame.setDialog1_3(new String[] {});
			dialogFrame.setDialog1_3_1(new String[] {});
			dialogFrame.end();
		}
		
		if(player.hasShield() && !shieldStoryEnd) { // get shield
			storyShield();
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
	
	private void storyBoss() {
		dialogFrame.start();
		String[] dialog1_3_1 = dialogFrame.getDialog1_3_1();
		int storyLength = dialog1_3_1.length;
		dialogFrame.setColor(new Color(0, 0, 0));
		if(currentDialog2 < storyLength) {
			dialogFrame.setContent(dialog1_3_1[currentDialog2]);
		}
		else {
			dialogFrame.end();
			dialogFrame.setDialog1_3_1(new String[] {});
			setFocus(player);
			bossStoryEnd = true;
		}
	}
	
	// after running story, set shieldStoryEnd = true
	private void storyShield() {
		dialogFrame.start();
		String[] dialog1_3_2 = dialogFrame.getDialog1_3_2();
		int storyLength = dialog1_3_2.length;
		dialogFrame.setColor(new Color(0, 0, 0));
		if(currentDialog3 < storyLength) {
			dialogFrame.setContent(dialog1_3_2[currentDialog3]);
		}
		else {
			dialogFrame.end();
			dialogFrame.setDialog1_3_2(new String[] {});
			shieldStoryEnd = true;
		}
	}

	@Override
	public void keyPressed(int k) {
		super.keyPressed(k);
		if( k == KeyEvent.VK_X && meetBoss && !bossStoryEnd) { // meeting boss
			currentDialog2++;
		}
		else if( k == KeyEvent.VK_X && player.hasShield() && !shieldStoryEnd) { //getting shield
			currentDialog3++;
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
