package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import GameState.LoadState;
import TileMap.TileMap;

public class Slugger extends Enemy{

	private BufferedImage[] sprites;
	
	public Slugger(TileMap tm) {
		super(tm);
		
		moveSpeed = 0.3;
		maxSpeed = 0.3;
		//moveSpeed = 3;
		//maxSpeed = 3;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 60;
		height = 60;
		cwidth = 40;
		cheight = 40;
		
		health = maxHealth = 2;
		damage = 1;
		
		// load sprites
		sprites = LoadState.Slugger[0];
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
		// when the slugger start up, it is going to face right
		right = true;
		facingRight = true;
		
	}
	
	private void getNextPosition() {
		
		// cuz a slugger only move left or right
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		
		// falling
		if(falling) {
			dy += fallSpeed;
		}
	}
	
	@Override
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check flinching
		if(flinching) {
			long elasped = 
					(System.nanoTime() - flinchTimer) / 1000000;
			if(elasped > 400) {
				flinching = false;
			}
		}
		
		// if it hits a wall, go the other direction
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if(left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
		
		// update animation
		animation.update();
		
		if(dead) remove = true;
	}
	
	@Override
	public void draw(Graphics2D g) {
		
		// let slugger.xmap = tileMap.x and slugger.ymap = tileMap.y
		// update the map information saved in slugger
		setMapPosition();
		
		// test
		//if(notOnScreen()) return;
		
		super.draw(g);
	}
}
