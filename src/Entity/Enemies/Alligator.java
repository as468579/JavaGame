package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;

public class Alligator extends Enemy{
	private BufferedImage[] sprites;
	
	public Alligator(TileMap tm) {
		super(tm);
		
		moveSpeed = 0.7;
		maxSpeed = 0.7;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 48;
		height = 48;
		cwidth = 35;
		cheight = 25;
		
		health = maxHealth = 40;
		damage = 2;
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/alligator.png"
				)
			);
			
			sprites = new BufferedImage[3];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
											i * width,
											0,
											width,
											height
										);
			}
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);
		
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
	}
	
	@Override
	public void draw(Graphics2D g) {
		
		// let slugger.xmap = tileMap.x and slugger.ymap = tileMap.y
		// update the map information saved in slugger
		setMapPosition();
		
		// test
		//if(notOnScreen()) return;
		
		
		// cuz alligator is facingLeft in the begining
		if(facingRight) {
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2 + width), // let x be the center of image
					(int)(y + ymap - height /2),         // let y be the center of image
					-width,
					height,
					null
				);
		}
		else{
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2),  // let x be the center of image
					(int)(y + ymap - height / 2), // let y be the center of image
					null
				);
		}
		
		// draw for test
		drawCollisionBox(g);
	}
}
