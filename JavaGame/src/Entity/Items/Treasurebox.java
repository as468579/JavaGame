package Entity.Items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Item;
import TileMap.TileMap;

public class Treasurebox extends Item{
	
	private int money;
	private BufferedImage[] sprites;
	
	public Treasurebox(TileMap tm) {
		super(tm);
		
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		facingRight = false;
		falling = true;
		
		width = 48;
		height = 48;
		cwidth = 30;
		cheight = 30;
		
		remove = false;
		
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Items/treasurebox.png"
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
			
			// set animation
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(500);
			animation.setLoop(false);
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
		
		// update animation 
		// treasurebox will not disappear after touching
		if(touched) {
			animation.update();
		}
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		// if(notOnScreen) return ;
		
		// cuz treasurebox is facingLeft in the begining
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
