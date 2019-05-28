package GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.Explosion;
import Entity.Item;
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
		init();
	}

	@Override
	public void init() {
		System.out.println("level 1-7");

		levelTileY = 161;
		levelTileHeight = 27;

		bg = new Background("/Backgrounds/background_1_7.gif", 0.5);

		populateEnemies();
		populateItems();

		explosions = new ArrayList<Explosion>();

	}

	@Override
	public void update() {
		super.update();

		if (player.getY() < (levelTileY) * 16) {
			gsm.setStates(GameStateManager.LEVEL1_6STATE);
		}

		if (player.getY() > (levelTileY + levelTileHeight) * 16) {
			gsm.setStates(GameStateManager.ENDING_STATE_2);
		}
	}

	private void populateEnemies() {

		// populate enemy
		enemies = new ArrayList<Enemy>();

		Slugger sl;
		Alligator a;
		Snake sn;
		Point[] points = new Point[] { new Point(200, 200), };

		for (int i = 0; i < points.length; i++) {
			a = new Alligator(tileMap);
			a.setPosition(points[i].x, points[i].y);
			enemies.add(a);
		}
		for (int i = 0; i < points.length; i++) {
			sn = new Snake(tileMap);
			sn.setPosition(points[i].x, points[i].y);
			enemies.add(sn);
		}
	}

	private void populateItems() {

		// populate items
		items = new ArrayList<Item>();

		Treasurebox tr;
		Bomb b;
		Point[] points = new Point[] { new Point(170, 200), };

		for (int i = 0; i < points.length; i++) {
			b = new Bomb(tileMap);
			b.setPosition(points[i].x, points[i].y);
			items.add(b);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
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
