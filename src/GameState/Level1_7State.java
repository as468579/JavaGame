package GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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
import Entity.Object.Items.Treasurebox;
import Entity.Object.Enemy;
import Entity.Object.Item;
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
		levelName = "level7";
		AudioPlayer.load("/Music/level7Music.mp3", levelName, AudioPlayer.BGMUSIC);
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
				getClass().getResourceAsStream("/HUD/level7.png")
			);
			subtitle = new Title(subtitleImg);
			subtitle.setY(85);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
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
			tileMap.setShaking(false);
			// eventFinish
			eventFinish(GameStateManager.ENDING_STATE_2);
		}
	}
	
	public void reset() {

		//TODO
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
