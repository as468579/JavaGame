package Entity.Object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.MapObject;
import GameState.LoadState;
import TileMap.TileMap;

public class FireBall extends MapObject{
	
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	private boolean used;
	
	private static final int SHOOTING = 0;
	private static final int HITTING = 1;

	public FireBall(TileMap tm, boolean right) {
		super(tm);
		
		moveSpeed = 3.8;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		facingRight = right;
		
		width = 30;
		height = 30;
		cwidth = 14;
		cheight = 14;
		

		
		// load sprites
		sprites = LoadState.Fireball[SHOOTING];
		hitSprites = LoadState.Fireball[HITTING];
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(30);
		
		// sprite direction
		correctSpriteDirection = true;
	}
	
	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
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
