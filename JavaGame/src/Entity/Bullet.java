package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import GameState.LoadState;
import TileMap.TileMap;

public class Bullet extends MapObject{
	
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private boolean used;
	
	private static final int SHOOTING = 0;

	public Bullet(TileMap tm, boolean right) {
		super(tm);
		
		moveSpeed = 3.8;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		facingRight = right;
		
		width = 20;
		height = 8;
		cwidth = 14;
		cheight = 14;
		

		
		// load sprites
		sprites = LoadState.Bullet[SHOOTING];
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}
	
	public void setHit() {
		if(hit) return;
		hit = true;
		dx = 0;  // when hit enemy dx should set to 0  
	}
	
	public boolean getHit() { return hit; }
	
	public boolean shouldRemove() { return remove; }
	
	public void update() {
		
		checkTileMapCollision();
		setPosition(xtemp,ytemp);
		
		
		// embeded in the wall 
		if(dx == 0 && !hit) {
			setHit();
		}
		
		animation.update();
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		super.draw(g);
	}

}
