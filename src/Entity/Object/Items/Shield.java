package Entity.Object.Items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Object.Item;
import Main.GamePanel;
import TileMap.TileMap;

public class Shield extends Item{
	
	private int money;
	private BufferedImage[] sprites;
	private Font font;
	private Color color;
	private long startTimer;
	private boolean firstTouch;
	
	
	public Shield(TileMap tm) {
		super(tm);

		width = 40;
		height = 40;
		cwidth = 35;
		cheight = 35;
		
		firstTouch  = true;
		
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Items/shield.png"
				)
			);
			
			sprites = new BufferedImage[1];
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
			animation.setDelay(-1);
			
			// sprite direction
			correctSpriteDirection = true;
		}catch(Exception e) {
				e.printStackTrace();
		}
	}
	
	@Override 
	public void update() {
		
		// update animation and coin will disappear after touching
		if(touched) {
			
			if(firstTouch) {
				startTimer = System.nanoTime();
				firstTouch = false;
			}
			long elapsed = (System.nanoTime() - startTimer) / 1000000;
			if(animation.hasPlayedOnce() && elapsed > 500) {
				remove = true;
			}
		}
		
		animation.update();
	}
	
	
	
	public void draw(Graphics2D g) {
		super.draw(g);
		// draw for test
		drawCollisionBox(g);
	}
	
	
}