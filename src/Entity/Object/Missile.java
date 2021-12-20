package Entity.Object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.MapObject;
import GameState.LoadState;
import TileMap.TileMap;

public class Missile extends MapObject{
	
	private boolean hit;
	private boolean remove;
	private boolean used;
	
	// animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[] NUMFRAMES = {
		4, 7	
	};
	private final int[] FRAMEWIDTHS = {
		75, 50
	};
	
	private final int[] FRAMEHEIGHTS = {
		50, 50
	};
	private final int[] SPRITEDELAYS = {
		40, 40
	};
	
	private static final int SHOOTING = 0;
	private static final int HITTING = 1;

	public Missile(TileMap tm, boolean right) {
		super(tm);
		
		moveSpeed = 4.0;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		facingRight = right;
		
		width = 75;
		height = 60;
		cwidth = 75;
		cheight = 60;
		

		
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/missile.png"
				)
			);
			
			int imageY = 0;
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 2 ; i++) {
				BufferedImage[] bi = new BufferedImage[NUMFRAMES[i]];
				for(int j = 0; j < NUMFRAMES[i]; j++) {
					bi[j] = spritesheet.getSubimage(
							j * FRAMEWIDTHS[i],
							imageY,
							FRAMEWIDTHS[i],
							FRAMEHEIGHTS[i]
					);
				}
				sprites.add(bi);
				imageY += FRAMEHEIGHTS[i];
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites.get(SHOOTING));
		animation.setDelay(SPRITEDELAYS[SHOOTING]);
		
		// sprite direction
		correctSpriteDirection = true;
	}
	
	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(sprites.get(HITTING));
		animation.setDelay(SPRITEDELAYS[HITTING]);
		if(facingRight) x = x + 35;
		else x = x - 35;
		cwidth = 50;
		cheight = 50;
		dx = 0;  // when hit enemy dx should set to 0  
	}
	
	public boolean getHit() { return hit; }
	
	public boolean shouldRemove() { return remove; }
	
	public void update() {
		
		checkTileMapCollision();
		setPosition(xtemp,ytemp);
		
		if(dx == 0 && !hit) {
			setHit();
		}
		
		animation.update();
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
	}

}
