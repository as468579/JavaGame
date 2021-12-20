package Entity.Object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.MapObject;
import Entity.Object.Bullet;
import Entity.Object.Enemy;
import Entity.Object.FireBall;
import Entity.Object.Player;
import GameState.LoadState;
import TileMap.TileMap;

public class Tornado extends MapObject{
	
	// tornado stuff
	private int damage;
	private boolean remove;
	private boolean hit;
	
	// sprites
	private BufferedImage[] sprites;
	
	private final int[] SPRITEDELAYS = {
		40
	};
	public Tornado(TileMap tm, boolean right) {
		super(tm);
		
		moveSpeed = 4.3;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		facingRight = right;
		
		width = 40;
		height = 40;
		cwidth = 40;
		cheight = 40;

		// load sprites
		sprites = LoadState.Tornado[0];
		
		// set animation
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(40);
		animation.setLoop(true);
		
		// sprite direction
		correctSpriteDirection = true;
	}
	
	public void setHit() {
		if(hit) return;
		hit = true;
		dx = 0;  // when hit enemy dx should set to 0  
	}

	public void update() {
		
		checkTileMapCollision();
		setPosition(xtemp,ytemp);
		
		animation.update();
		
		// embeded in the wall 
		if(dx == 0 && !hit) {
			setHit();
		}
		
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	public boolean shouldRemove() { return remove; }
}



