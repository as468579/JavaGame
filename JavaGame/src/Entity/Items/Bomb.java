package Entity.Items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Item;
import TileMap.TileMap;

public class Bomb extends Item{
	
	private BufferedImage[] sprites;
	
	public Bomb(TileMap tm) {
		super(tm);
		
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		facingRight = false;
		falling = true;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Items/bomb.png"
				)
			);
			
			sprites = new BufferedImage[14];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
											i * width,
											0,
											width,
											height
										);
			}
			
			// set animation
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(200);
		}catch(Exception e) {
				e.printStackTrace();
		}
	}
	
	private void getNextPosition() {
		// falling
		if(falling) {
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	@Override 
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp,ytemp);
		
		// update animation and bomb will disappear after touching
		if(touched) {
			animation.update();
			if(animation.hasPlayedOnce()) {
				remove = true;
			}
		}
		

		
		// check explosion
		checkExplosion();
	}
	
	private void checkExplosion() {
		
		if(animation.getCurrentFrame() == 10) {
			
			// if cwidth, cheight become too large suddenly, could make object move
			//cwidth = 60;
			//cheight = 60;
		}
		if(animation.getCurrentFrame() == 13) {
			tileMap.explosion(currentRow, currentCol);
		}
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		// if(notOnScreen) return ;
		
		// resume normal size
		/*if(animation.getCurrentFrame() == 0) {
			width = 30;
			height = 30;
		}*/
		
		// cuz bomb is facingLeft in the begining
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
//		drawCollisionBox(g);
	}
	
	
}
