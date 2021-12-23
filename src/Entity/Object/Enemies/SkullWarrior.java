package Entity.Object.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Object.Enemy;
import GameState.LoadState;
import TileMap.TileMap;

public class SkullWarrior extends Enemy{
	private BufferedImage[] sprites;
	
	public SkullWarrior(TileMap tm) {
		super(tm);
		
		moveSpeed = 0.7;
		maxSpeed = 0.7;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 100;
		height = 100;
		cwidth = 60;
		cheight = 75;
		
		health = maxHealth = 40;
		damage = 2;
		
		// load sprites
		sprites = LoadState.SkullWarrior[0];
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
		// sprite direction
		correctSpriteDirection = false;
		
		// when the alligator start up, it is going to face left
		right = false;
		left = true;
		facingRight = false;
		
	}
	
	private void getNextPosition() {
		
		// cuz a alligator only move left or right
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
	
}
