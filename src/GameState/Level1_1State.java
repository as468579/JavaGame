package GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.Explosion;
import Entity.Item;
import Entity.Player;
import Entity.Enemies.Alligator;
import Entity.Enemies.Slugger;
import Entity.Enemies.Snake;
import Entity.Items.Bomb;
import Entity.Items.Treasurebox;
import TileMap.Background;

public class Level1_1State extends LevelState {

	private Rectangle story;

	public Level1_1State(GameStateManager gsm) {
		super(gsm);
		init();
	}

	@Override
	public void init() {
		System.out.println("level 1-1");

		levelTileY = 0;
		levelTileHeight = 27;

		bg = new Background("/Backgrounds/background_1_1.gif", 0.5);

		populateEnemies();
		populateItems();
		populateStory();

		explosions = new ArrayList<Explosion>();

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

	private void populateStory() {

		// add story for Story1_2State
		story = new Rectangle(2200, 100, 400, 300);
	}

	@Override
	public void update() {
		super.update();

		// story check
		if (story.contains(player.getX(), player.getY())) { // enter story 2
			gsm.setStates(GameStateManager.STORY1_2STATE);
		}

		// position check
		if (player.getY() > (levelTileY + levelTileHeight) * 16) {
			gsm.setStates(GameStateManager.LEVEL1_2STATE);
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
