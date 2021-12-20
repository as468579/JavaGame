package GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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
import Entity.Items.Treasurebox;
import Main.GamePanel;
import TileMap.Background;

public class Level1_7State extends LevelState {
	
	public Level1_7State(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		super.init();
		
		System.out.println("level 1-7");

		levelTileY = 192;
		levelTileHeight = 32;

		bg = new Background("/Backgrounds/background_1_7.gif", 0.5);

		// background music
		bgMusic = new AudioPlayer("/Music/Level1Music.mp3");
		
		dialogFrame = new Dialog();
		
		enemies = new ArrayList<Enemy>();
		explosions = new ArrayList<Explosion>();
		items = new ArrayList<Item>();
		
		populateEnemies();
		populateItems();
		
		shakeSize = 0;
		
		
		//check player is flying up
		if(PlayerSave.isFlying()) player.setPosition(2720, 3200); 
	}

	@Override
	public void update() {
		super.update();

		if (player.getY() < (levelTileY-1) * 16) {
			PlayerSave.setLvl1_7(true);
			// eventFinish
			eventFinish(GameStateManager.LEVEL1_6STATE);
		}

		if (player.getY() > (levelTileY + levelTileHeight) * 16) {
			PlayerSave.setLvl1_7(true);
			// eventFinish
			eventFinish(GameStateManager.ENDING_STATE_2);
		}
	}
	
	public void reset() {

		player.setPosition(2720, 3200); 

		if(true)populateEnemies();
		else;		// get wings
		
		super.reset();
	}

	private void populateEnemies() {

		// populate enemy
		enemies.clear();
	}

	private void populateItems() {

		// populate items
		items.clear();
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		dialogFrame.end();
	}

	@Override
	public void keyPressed(int k) {
		super.keyPressed(k);
	}

	@Override
	public void keyReleased(int k) {
		super.keyReleased(k);
	}
}
