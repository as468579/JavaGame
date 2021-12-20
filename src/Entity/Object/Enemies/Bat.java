package Entity.Object.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Object.Enemy;
import GameState.LoadState;
import TileMap.TileMap;

public class Bat extends Enemy{
	private BufferedImage[] sprites;
	
	public Bat(TileMap tm) {
		super(tm);
		
		moveSpeed = 1.0;
		maxSpeed = 1.0;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 50;
		height = 30;
		cwidth = 40;
		cheight = 26;
		
		health = maxHealth = 10;
		damage = 1;
		
		// load sprites
		sprites = LoadState.Bat[0];
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(100);
		
		// sprite direction
		correctSpriteDirection = true;
		
		// when the alligator start up, it is going to face left
		up = false;
		down = true;
		falling = false;
		facingRight = false;
		
	}
	
	private void getNextPosition() {
		
		// cuz a alligator only move left or right
		// movement
		if(down) {
			dy -= moveSpeed;
			if(dy < -maxSpeed) {
				dy = -maxSpeed;
			}
		}
		else if(up) {
			dy += moveSpeed;
			if(dy > maxSpeed) {
				dy = maxSpeed;
			}
		}
	}
	
	@Override
 	public void checkTileMapCollision() {
 		
 		currentCol = (int)x / tileSize;
 		currentRow = (int)y / tileSize;
 		
 		dxtemp = dx;
 		dytemp = dy;
 		
 		xtemp = x;
 		ytemp = y;
 		
 		//System.out.println("dx : " + dx + ", dy : " + dy);
 		calculateEdges(0, dy);
 
 		if(dy < 0) {
 			if(topCollided) {
 				dytemp = 0;
 				
 				// 即使下一刻就會撞上blocked tile，目前位置仍可能和blocked tile有極小差距 
 				// 所以算出合理座標帶入
 				ytemp = currentRow * tileSize + cheight / 2;
 				
 			}
 			else {
 				ytemp += dy;
 			}
 			
 		}
 		if(dy > 0) {
 			if(bottomCollided) {
 				dytemp = 0;
 				falling = false;
 				climbing = false;
 			    // if cheight /2 > tileSize , the MapObject will bounce 
 				ytemp = (currentRow + 1) * tileSize - cheight / 2;
 			}
 			else {
 				ytemp += dy;
 			}
 		}
 		
 		calculateEdges(dx, 0);
 		if(dx < 0) {
 			if(leftCollided) {
 				dxtemp = 0;
 				xtemp = currentCol * tileSize + cwidth / 2;
 			}
 			else {
 				xtemp += dx;
 			}
 		}
 		if(dx > 0) {
 			if(rightCollided) {
 				dxtemp = 0;
 				xtemp = (currentCol + 1) * tileSize - cwidth / 2;
 			}
 			else {
 				xtemp += dx;
 			}
 		}
 		
 		dx = dxtemp;
 		dy = dytemp;
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
		if(up && dy == 0) {
			up = false;
			down = true;
		}
		else if(down && dy == 0) {
			up = true;
			down = false;
		}
		
		// update animation
		animation.update();
		
		if(dead) remove = true;
	}
	
}

